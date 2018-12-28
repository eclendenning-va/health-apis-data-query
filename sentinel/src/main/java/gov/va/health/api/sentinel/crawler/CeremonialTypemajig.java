package gov.va.health.api.sentinel.crawler;

import gov.va.api.health.argonaut.api.bundle.AbstractBundle;

public class CeremonialTypemajig {

  Class getResourceClass(String next) {
    String baseUrl = "https://localhost:8090/api/";
    String resourceType;
    Class resourceClass;
    if (next.contains("?")) {
      return AbstractBundle.class;
    } else {
      String[] query = next.split(baseUrl, 2);
      String[] readQuery = query[1].split("\\/");
      resourceType = readQuery[0];
    }
    try {
      resourceClass = Class.forName(resourceType);
    } catch (ClassNotFoundException e) {
      throw new ClassCastException(resourceType + " not a valid resource.");
    }
    return resourceClass;
  }
}
