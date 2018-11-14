package gov.va.api.health.argonaut.service.controller.medication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.Medication;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.pojos.Medication101Root;
import gov.va.dvp.cdw.xsd.pojos.Medication101Root.Medications;
import java.util.List;
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MedicationControllerTest {
  @Mock MrAndersonClient client;

  @Mock MedicationController.Transformer tx;

  MedicationController controller;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new MedicationController(tx, client);
  }

  @Test
  public void read() {
    Medication101Root root = new Medication101Root();
    root.setMedications(new Medications());
    Medication101Root.Medications.Medication xmlMedication =
        new Medication101Root.Medications.Medication();
    root.getMedications().getMedication().add(xmlMedication);
    Medication medication = Medication.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(tx.apply(xmlMedication)).thenReturn(medication);
    Medication actual = controller.read("hello");
    assertThat(actual).isSameAs(medication);
    ArgumentCaptor<Query<Medication101Root>> captor = ArgumentCaptor.forClass(Query.class);
    verify(client).search(captor.capture());
    Entry<? extends String, ? extends List<String>> e;
    assertThat(captor.getValue().parameters()).isEqualTo(Parameters.forIdentity("hello"));
  }
}
