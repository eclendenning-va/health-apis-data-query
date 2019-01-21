package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Practitioner;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@SuppressWarnings({"DefaultAnnotationParam", "WeakerAccess"})
@RunWith(Parameterized.class)
@Category({Local.class, Prod.class, Qa.class})
@Slf4j
public class PractitionerIT {

  @Parameter(0)
  public int status;

  @Parameter(1)
  public Class<?> response;

  @Parameter(2)
  public String path;

  @Parameter(3)
  public String[] params;

  ResourceRequest resourceRequest = new ResourceRequest();

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(200, Practitioner.class, "/api/Practitioner/{id}", ids.practitioner()),
        assertRequest(404, OperationOutcome.class, "/api/Practitioner/{id}", ids.unknown()),
        assertRequest(
            200, Practitioner.Bundle.class, "/api/Practitioner?_id={id}", ids.practitioner()),
        assertRequest(
            200,
            Practitioner.Bundle.class,
            "/api/Practitioner?identifier={id}",
            ids.practitioner()),
        assertRequest(404, OperationOutcome.class, "/api/Practitioner?_id={id}", ids.unknown()));
  }

  @Test
  public void getResource() {
    resourceRequest.getResource(path, params, status, response);
  }

  @Test
  public void pagingParameterBounds() {
    resourceRequest.pagingParameterBounds(path, params, response);
  }
}
