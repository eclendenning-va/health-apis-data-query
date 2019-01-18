package gov.va.health.api.sentinel;

public class ResourceRequest {

  public static Object[] assertRequest(
      int status, Class<?> response, String path, String... parameters) {
    return new Object[] {status, response, path, parameters};
  }
}
