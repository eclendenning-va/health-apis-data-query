package gov.va.health.api.sentinel.crawler;

import io.restassured.RestAssured;
import java.util.function.Supplier;
import lombok.Builder;

/** The Crawler will recursive request resources from an Argonaut server. I */
@Builder
public class Crawler {

  private final RequestQueue requestQueue;
  private final Supplier<String> authenticationToken;

  /** Crawler iterates through queue performing all queries. */
  public void crawl() {
    while (requestQueue.hasNext()) {
      RestAssured.given()
          .header("Authorization", "Bearer " + authenticationToken.get())
          .contentType("application/fhir+json")
          // TODO .header("jargonaut", USE_JARGONAUT)
          .get(requestQueue.next())
          .then()
          .log()
          .all();
    }
  }
}
