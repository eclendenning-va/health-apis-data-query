package gov.va.health.api.sentinel.nebuchadnezzar;

import io.restassured.RestAssured;

public class Crawler {

  RequestQueue requestQueue = new ConcurrentRequestQueue();
  String baseURL = "https://localhost:8090/api/";

  /** Crawler iterates through queue performing all queries. */
  public void crawl() {
    while (requestQueue.hasNext()) {
      String query = requestQueue.next();
      //Class resourceClass = getResourceClass(query);
      RestAssured.given().get(query).then().log().all();

    }
  }

  Class getResourceClass(String next) {
    String[] query = next.split(baseURL,2);
    String resourceType;
    Class resourceClass;
    if (query[1].contains("?")) {
      String[] searchQuery = query[1].split("\\?");
      resourceType = searchQuery[0];
    } else {
      String[] readQuery = query[1].split("\\/");
      resourceType = readQuery[0];
    }
    try{
      resourceClass = Class.forName(resourceType);
    } catch (ClassNotFoundException e) {
      throw new ClassCastException(resourceType + " not a valid resource.");
    }
    return resourceClass;
  }

}
