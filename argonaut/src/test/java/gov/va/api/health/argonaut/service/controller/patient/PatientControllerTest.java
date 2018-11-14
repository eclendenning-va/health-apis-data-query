package gov.va.api.health.argonaut.service.controller.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.api.Patient.Bundle;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Bundler.BundleContext;
import gov.va.api.health.argonaut.service.controller.PageLinks.LinkConfig;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root.Patients;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.MultiValueMap;

public class PatientControllerTest {

  @Mock MrAndersonClient client;
  @Mock PatientController.Transformer tx;
  @Mock Bundler bundler;
  @Mock HttpServletRequest servletRequest;

  PatientController controller;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new PatientController(tx, client, bundler);
  }

  private void assertSearch(Supplier<Bundle> invocation, MultiValueMap<String, String> params) {
    Patient103Root root = new Patient103Root();
    root.setPageNumber(1);
    root.setRecordsPerPage(10);
    root.setRecordCount(3);
    root.setPatients(new Patients());
    Patient103Root.Patients.Patient xmlPatient1 = new Patient103Root.Patients.Patient();
    Patient103Root.Patients.Patient xmlPatient2 = new Patient103Root.Patients.Patient();
    Patient103Root.Patients.Patient xmlPatient3 = new Patient103Root.Patients.Patient();
    root.getPatients().getPatient().addAll(Arrays.asList(xmlPatient1, xmlPatient2, xmlPatient3));
    Patient patient1 = Patient.builder().build();
    Patient patient2 = Patient.builder().build();
    Patient patient3 = Patient.builder().build();
    when(tx.apply(xmlPatient1)).thenReturn(patient1);
    when(tx.apply(xmlPatient2)).thenReturn(patient2);
    when(tx.apply(xmlPatient3)).thenReturn(patient3);
    when(client.search(Mockito.any())).thenReturn(root);
    when(servletRequest.getRequestURI()).thenReturn("/api/Patient");

    Bundle mockBundle = new Bundle();
    when(bundler.bundle(Mockito.any())).thenReturn(mockBundle);

    Bundle actual = invocation.get();

    assertThat(actual).isSameAs(mockBundle);
    ArgumentCaptor<
            BundleContext<Patient103Root.Patients.Patient, Patient, Patient.Entry, Patient.Bundle>>
        captor = ArgumentCaptor.forClass(BundleContext.class);

    verify(bundler).bundle(captor.capture());

    LinkConfig expectedLinkConfig =
        LinkConfig.builder()
            .page(1)
            .recordsPerPage(10)
            .totalRecords(3)
            .path("/api/Patient")
            .queryParams(params)
            .build();
    assertThat(captor.getValue().linkConfig()).isEqualTo(expectedLinkConfig);
    assertThat(captor.getValue().xmlItems()).isEqualTo(root.getPatients().getPatient());
    assertThat(captor.getValue().newBundle().get()).isInstanceOf(Patient.Bundle.class);
    assertThat(captor.getValue().newEntry().get()).isInstanceOf(Patient.Entry.class);
    assertThat(captor.getValue().transformer()).isSameAs(tx);
  }

  @Test
  public void read() {
    Patient103Root root = new Patient103Root();
    root.setPatients(new Patients());
    Patient103Root.Patients.Patient xmlPatient = new Patient103Root.Patients.Patient();
    root.getPatients().getPatient().add(xmlPatient);
    Patient patient = Patient.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(tx.apply(xmlPatient)).thenReturn(patient);
    Patient actual = controller.read("hello");
    assertThat(actual).isSameAs(patient);
    ArgumentCaptor<Query<Patient103Root>> captor = ArgumentCaptor.forClass(Query.class);
    verify(client).search(captor.capture());
    Entry<? extends String, ? extends List<String>> e;
    assertThat(captor.getValue().parameters()).isEqualTo(Parameters.forIdentity("hello"));
  }

  @Test
  public void searchByFamilyAndGender() {
    assertSearch(
        () -> controller.searchByFamilyAndGender("f", "g", 1, 10, servletRequest),
        Parameters.builder()
            .add("family", "f")
            .add("gender", "g")
            .add("page", 1)
            .add("_count", 10)
            .build());
  }
}
