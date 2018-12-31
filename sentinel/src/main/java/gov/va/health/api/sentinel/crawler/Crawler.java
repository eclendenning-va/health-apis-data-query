package gov.va.health.api.sentinel.crawler;

import static java.util.stream.Collectors.joining;

import gov.va.api.health.argonaut.api.bundle.AbstractBundle;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry;
import gov.va.api.health.argonaut.api.bundle.BundleLink;
import gov.va.api.health.argonaut.api.bundle.BundleLink.LinkRelation;
import gov.va.health.api.sentinel.crawler.Result.Outcome;
import gov.va.health.api.sentinel.crawler.Result.ResultBuilder;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/** The Crawler will recursive request resources from an Argonaut server. I */
@Builder
@Slf4j
public class Crawler {

  private final RequestQueue requestQueue;
  private final ResultCollector results;
  private final Supplier<String> authenticationToken;

  private void addLinksFromBundle(Object payload) {
    if (!(payload instanceof AbstractBundle<?>)) {
      return;
    }
    AbstractBundle<?> bundle = (AbstractBundle<?>) payload;
    Optional<BundleLink> next =
        bundle.link().stream().filter(l -> l.relation() == LinkRelation.next).findFirst();
    if (next.isPresent()) {
      requestQueue.add(next.get().url());
    }
    bundle.entry().stream().map(AbstractEntry::fullUrl).forEach(requestQueue::add);
  }

  private String asAdditionalInfo(ConstraintViolation<?> v) {
    return v.getPropertyPath() + ": " + v.getMessage() + ", got: " + v.getInvalidValue();
  }

  private String asAdditionalInfo(Exception e) {
    StringBuilder info = new StringBuilder();
    info.append("Exception: ")
        .append(e.getClass().getName())
        .append("\nMessage: ")
        .append(e.getMessage())
        .append("\n----\n")
        .append(ExceptionUtils.getStackTrace(e));
    return info.toString();
  }

  /** Crawler iterates through queue performing all queries. */
  public void crawl() {
    results.init();
    while (requestQueue.hasNext()) {
      String url = requestQueue.next();
      ResultBuilder resultBuilder = Result.builder().timestamp(Instant.now()).query(url);
      try {
        process(url, resultBuilder);
      } catch (Exception e) {
        log.error("Failed to process {}", url, e);
        resultBuilder.outcome(Outcome.REQUEST_FAILED).additionalInfo(asAdditionalInfo(e));
      }
      results.add(resultBuilder.build());
    }
    results.done();
  }

  @SneakyThrows
  private void process(String url, ResultBuilder resultBuilder) {
    Class<?> type = new UrlToResourceConverter().apply(url);
    log.info("Requesting {} as {}", url, type.getName());
    Response response =
        RestAssured.given()
            .header("Authorization", "Bearer " + authenticationToken.get())
            .contentType("application/fhir+json")
            // TODO .header("jargonaut", USE_JARGONAUT)
            .get(url)
            .andReturn();
    resultBuilder.httpStatus(response.getStatusCode()).body(response.getBody().asString());

    if (response.getStatusCode() == 200) {
      ReferenceInterceptor interceptor = new ReferenceInterceptor();
      Object payload = interceptor.mapper().readValue(response.asByteArray(), type);
      interceptor.references().forEach(u -> requestQueue.add(u));

      Set<ConstraintViolation<Object>> violations =
          Validation.buildDefaultValidatorFactory().getValidator().validate(payload);
      if (violations.isEmpty()) {
        resultBuilder.outcome(Outcome.OK);
      } else {
        resultBuilder
            .outcome(Outcome.INVALID_PAYLOAD)
            .additionalInfo(violations.stream().map(this::asAdditionalInfo).collect(joining("\n")));
      }

      addLinksFromBundle(payload);

    } else {
      resultBuilder.outcome(Outcome.INVALID_STATUS);
    }
  }
}
