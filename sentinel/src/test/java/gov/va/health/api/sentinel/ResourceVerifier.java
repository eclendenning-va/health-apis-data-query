package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.bundle.AbstractBundle;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;

public class ResourceRequest {

  public static Object[] assertRequest(
      int status, Class<?> response, String path, String... parameters) {
    return new Object[] {status, response, path, parameters};
  }

  private TestClient argonaut() {
    return Sentinel.get().clients().argonaut();
  }

  public void getResource(String path, String[] params, int status, Class<?> response) {
    argonaut().get(path, params).expect(status).expectValid(response);
  }

  /**
   * If the response is a bundle, then the query is a search. We want to verify paging parameters
   * restrict page >= 1, _count >=1, and _count <= 20
   */
  public void pagingParameterBounds(String path, String[] params, Class<?> response) {
    if (!AbstractBundle.class.isAssignableFrom(response)) {
      return;
    }
    argonaut().get(path + "&page=0", params).expect(400).expectValid(OperationOutcome.class);
    argonaut().get(path + "&_count=-1", params).expect(400).expectValid(OperationOutcome.class);
    argonaut().get(path + "&_count=0", params).expect(200).expectValid(response);
    argonaut().get(path + "&_count=21", params).expect(200).expectValid(response);
  }
}
