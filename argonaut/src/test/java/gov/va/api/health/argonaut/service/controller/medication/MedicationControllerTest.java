package gov.va.api.health.argonaut.service.controller.medication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("WeakerAccess")
public class MedicationControllerTest {
  @Mock MrAndersonClient client;

  @Mock MedicationController.Transformer tx;

  MedicationController controller;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new MedicationController(tx, client);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void read() {
    CdwMedication101Root root = new CdwMedication101Root();
    root.setMedications(new CdwMedications());
    CdwMedication101Root.CdwMedications.CdwMedication xmlMedication =
        new CdwMedication101Root.CdwMedications.CdwMedication();
    root.getMedications().getMedication().add(xmlMedication);
    Medication medication = Medication.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(tx.apply(xmlMedication)).thenReturn(medication);
    Medication actual = controller.read("hello");
    assertThat(actual).isSameAs(medication);
    ArgumentCaptor<Query<CdwMedication101Root>> captor = ArgumentCaptor.forClass(Query.class);
    verify(client).search(captor.capture());
    assertThat(captor.getValue().parameters()).isEqualTo(Parameters.forIdentity("hello"));
  }
}
