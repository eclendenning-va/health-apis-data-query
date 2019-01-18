package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.Procedure;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class ProcedureAdvancedIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(
            200,
            Procedure.Bundle.class,
            "api/Procedure?patient={patient}&date={onDate}",
            ids.patient(),
            ids.procedures().onDate()),
        assertRequest(
            200,
            Procedure.Bundle.class,
            "api/Procedure?patient={patient}&date={fromDate}&date={toDate}",
            ids.patient(),
            ids.procedures().fromDate(),
            ids.procedures().toDate()));
  }
}
