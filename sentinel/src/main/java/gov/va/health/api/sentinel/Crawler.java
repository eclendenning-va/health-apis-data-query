package gov.va.health.api.sentinel;

import io.restassured.RestAssured;

public class Crawler {

  RequestQueue requestQueue = new ConcurrentRequestQueue();

  /** Crawler iterates through queue performing all queries. */
  public void crawl() {
    if (requestQueue.hasNext()) {
      RestAssured.given().get(requestQueue.next()).then().log().all();
    }
  }
}
