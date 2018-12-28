package gov.va.health.api.sentinel.crawler;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/** The Crawler will recursive request resources from an Argonaut server. I */
@Builder
@Slf4j
public class Crawler {

  private final RequestQueue requestQueue;
  private final Supplier<String> authenticationToken;

  /** Crawler iterates through queue performing all queries. */
  public void crawl() {
    int count = 0; // TODO remove me
    while (requestQueue.hasNext()) {
      count++;
      String url = requestQueue.next();
      try {
        process(url);
      } catch (Exception e) {
        log.error("Failed to process {}", url, e);
        // TODO return result
      }
    }
    log.info("Made {} requests", count); // TODO remove me
  }

  @SneakyThrows
  private void process(String url) {
    // TODO return result
    Class<?> type = new UrlToResourceConverter().apply(url);
    log.info("Requesting {} as {}", url, type.getName());
    Response response =
        RestAssured.given()
            .header("Authorization", "Bearer " + authenticationToken.get())
            .contentType("application/fhir+json")
            // TODO .header("jargonaut", USE_JARGONAUT)
            .get(url)
            .andReturn();
    if (response.getStatusCode() == 200) {
      ReferenceInterceptor interceptor = new ReferenceInterceptor();
      Object payload = interceptor.mapper().readValue(response.asByteArray(), type);
      log.info(
          "Found {} references in {}",
          interceptor.references().size(),
          payload.getClass().getName());
      interceptor.references().forEach(u -> requestQueue.add(u));
    } else {
      // TODO return sad result
    }
  }
}
