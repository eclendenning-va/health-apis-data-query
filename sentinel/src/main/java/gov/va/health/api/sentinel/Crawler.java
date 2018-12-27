package gov.va.health.api.sentinel;

import io.restassured.RestAssured;
import lombok.Builder;

/** The Crawler will recursive request resources from an Argonaut server. I */
@Builder
public class Crawler {

  private final RequestQueue requestQueue;

  /** Crawler iterates through queue performing all queries. */
  public void crawl() {
    while (requestQueue.hasNext()) {
      RestAssured.given().get(requestQueue.next()).then().log().all();
    }
  }
}
