package gov.va.health.api.sentinel.ids;

public interface TestClient {
  ServiceDefinition service();

  ExpectedResponse get(String path, String params);

  ExpectedResponse post(String path, Object body);
}
