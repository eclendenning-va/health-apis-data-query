package gov.va.api.health.argonaut.service.healthcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.Medication;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Status;

public class HealthCheckTest {
  @Mock MrAndersonClient client;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void healthCheckHappyPath() {
    CdwMedication101Root root = new CdwMedication101Root();
    root.setMedications(new CdwMedication101Root.CdwMedications());
    CdwMedication101Root.CdwMedications.CdwMedication xmlMedication =
        new CdwMedication101Root.CdwMedications.CdwMedication();
    root.getMedications().getMedication().add(xmlMedication);
    SteelThreadSystemCheck test = new SteelThreadSystemCheck(client, "123");
    when(client.search(Mockito.any())).thenReturn(root);
    assertThat(test.health().getStatus().equals(Status.UP));
  }

  @Test
  public void healthCheckSadPath() {
    SteelThreadSystemCheck test = new SteelThreadSystemCheck(client, "123");
    when(client.search(Mockito.any()))
        .thenThrow(
            new MrAndersonClient.SearchFailed(
                Query.forType(Medication.class)
                    .parameters(Parameters.forIdentity("x"))
                    .profile(Query.Profile.ARGONAUT)
                    .resource("Medication")
                    .version("999")
                    .build()));
    assertThat(test.health().getStatus().equals(Status.DOWN));
  }
}
