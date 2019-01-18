package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Procedure;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class ProcedureIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(200, Procedure.class, "/api/Procedure/{id}", ids.procedure()),
        assertRequest(404, OperationOutcome.class, "/api/Procedure/{id}", ids.unknown()),
        assertRequest(200, Procedure.Bundle.class, "/api/Procedure?_id={id}", ids.procedure()),
        assertRequest(404, OperationOutcome.class, "/api/Procedure?_id={id}", ids.unknown()),
        assertRequest(
            200, Procedure.Bundle.class, "/api/Procedure?identifier={id}", ids.procedure()),
        assertRequest(
            200, Procedure.Bundle.class, "/api/Procedure?patient={patient}", ids.patient()));
  }
}
