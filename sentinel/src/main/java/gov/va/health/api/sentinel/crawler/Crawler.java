package gov.va.health.api.sentinel.crawler;

import io.restassured.RestAssured;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/** The Crawler will recursive request resources from an Argonaut server. I */
@Builder
@Slf4j
public class Crawler {

  private final RequestQueue requestQueue;
  private final Supplier<String> authenticationToken;

  /** Crawler iterates through queue performing all queries. */
  public void crawl() {
    while (requestQueue.hasNext()) {
      String url = requestQueue.next();

      Class<?> type = new UrlToResourceConverter().apply(url);

      log.info("Requesting {} as {}", url, type.getName());

      RestAssured.given()
          .header("Authorization", "Bearer " + authenticationToken.get())
          .contentType("application/fhir+json")
          // TODO .header("jargonaut", USE_JARGONAUT)
          .get(url)
          .then()
          .log()
          .all();
    }
  }
}
