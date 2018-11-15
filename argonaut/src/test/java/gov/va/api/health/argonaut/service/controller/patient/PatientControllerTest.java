package gov.va.api.health.argonaut.service.controller.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.CodeableConcept;
import gov.va.api.health.argonaut.api.Issue;
import gov.va.api.health.argonaut.api.Issue.IssueSeverity;
import gov.va.api.health.argonaut.api.Narrative;
import gov.va.api.health.argonaut.api.Narrative.NarrativeStatus;
import gov.va.api.health.argonaut.api.OperationOutcome;
import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.api.Patient.Bundle;
import gov.va.api.health.argonaut.api.bundle.AbstractBundle.BundleType;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Bundler.BundleContext;
import gov.va.api.health.argonaut.service.controller.PageLinks.LinkConfig;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.SneakyThrows;
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
    CdwPatient103Root root = new CdwPatient103Root();
    root.setPageNumber(1);
    root.setRecordsPerPage(10);
    root.setRecordCount(3);
    root.setPatients(new CdwPatients());
    CdwPatient xmlPatient1 = new CdwPatient();
    CdwPatient xmlPatient2 = new CdwPatient();
    CdwPatient xmlPatient3 = new CdwPatient();
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
    ArgumentCaptor<BundleContext<CdwPatient, Patient, Patient.Entry, Patient.Bundle>> captor =
        ArgumentCaptor.forClass(BundleContext.class);

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
    CdwPatient103Root root = new CdwPatient103Root();
    root.setPatients(new CdwPatients());
    CdwPatient xmlPatient = new CdwPatient();
    root.getPatients().getPatient().add(xmlPatient);
    Patient patient = Patient.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(tx.apply(xmlPatient)).thenReturn(patient);
    Patient actual = controller.read("hello");
    assertThat(actual).isSameAs(patient);
    ArgumentCaptor<Query<CdwPatient103Root>> captor = ArgumentCaptor.forClass(Query.class);
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

  @Test
  public void searchByGivenAndGender() {
    assertSearch(
        () -> controller.searchByGivenAndGender("f", "g", 1, 10, servletRequest),
        Parameters.builder()
            .add("given", "f")
            .add("gender", "g")
            .add("page", 1)
            .add("_count", 10)
            .build());
  }

  @Test
  public void searchById() {
    assertSearch(
        () -> controller.searchById("me", 1, 10, servletRequest),
        Parameters.builder().add("_id", "me").add("page", 1).add("_count", 10).build());
  }

  @Test
  public void searchByIdentifier() {
    assertSearch(
        () -> controller.searchByIdentifier("me", 1, 10, servletRequest),
        Parameters.builder().add("identifier", "me").add("page", 1).add("_count", 10).build());
  }

  @Test
  public void searchByNameAndBirthdate() {
    assertSearch(
        () ->
            controller.searchByNameAndBirthdate(
                "me", new String[] {"1975", "2005"}, 1, 10, servletRequest),
        Parameters.builder()
            .add("name", "me")
            .addAll("birthdate", "1975", "2005")
            .add("page", 1)
            .add("_count", 10)
            .build());
  }

  @Test
  public void searchByNameAndGender() {
    assertSearch(
        () -> controller.searchByNameAndGender("f", "g", 1, 10, servletRequest),
        Parameters.builder()
            .add("name", "f")
            .add("gender", "g")
            .add("page", 1)
            .add("_count", 10)
            .build());
  }

  @Test
  public void searchReturnsEmptyResults() {
    CdwPatient103Root root = new CdwPatient103Root();
    root.setPageNumber(1);
    root.setRecordsPerPage(10);
    root.setRecordCount(0);
    when(client.search(Mockito.any())).thenReturn(root);
    when(servletRequest.getRequestURI()).thenReturn("/api/Patient");
    Bundle mockBundle = new Bundle();
    when(bundler.bundle(Mockito.any())).thenReturn(mockBundle);
    Bundle actual = controller.searchById("me", 1, 10, servletRequest);
    assertThat(actual).isSameAs(mockBundle);
    ArgumentCaptor<BundleContext<CdwPatient, Patient, Patient.Entry, Patient.Bundle>> captor =
        ArgumentCaptor.forClass(BundleContext.class);

    verify(bundler).bundle(captor.capture());
    LinkConfig expectedLinkConfig =
        LinkConfig.builder()
            .page(1)
            .recordsPerPage(10)
            .totalRecords(0)
            .path("/api/Patient")
            .queryParams(
                Parameters.builder().add("_id", "me").add("page", 1).add("_count", 10).build())
            .build();
    assertThat(captor.getValue().linkConfig()).isEqualTo(expectedLinkConfig);
    assertThat(captor.getValue().xmlItems()).isEmpty();
  }

  @Test
  @SneakyThrows
  public void validateAcceptsValidBundle() {
    OperationOutcome expected =
        OperationOutcome.builder()
            .resourceType("OperationOutcome")
            .id("allok")
            .text(
                Narrative.builder()
                    .status(NarrativeStatus.additional)
                    .div("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p>ALL OK</p></div>")
                    .build())
            .issue(
                Collections.singletonList(
                    Issue.builder()
                        .severity(IssueSeverity.information)
                        .code("informational")
                        .details(CodeableConcept.builder().text("ALL OK").build())
                        .build()))
            .build();

    Patient patient =
        JacksonConfig.createMapper()
            .readValue(getClass().getResourceAsStream("/cdw/old-patient-1.03.json"), Patient.class);

    Bundle bundle =
        Patient.Bundle.builder()
            .type(BundleType.searchset)
            .resourceType("Bundle")
            .entry(
                Collections.singletonList(
                    Patient.Entry.builder()
                        .fullUrl("http://example.com")
                        .resource(patient)
                        .build()))
            .build();
    OperationOutcome actual = controller.validate(bundle);
    assertThat(actual).isEqualTo(expected);
  }

  @Test(expected = ConstraintViolationException.class)
  @SneakyThrows
  public void validateThrowsExceptionForInvalidBundle() {
    OperationOutcome expected =
        OperationOutcome.builder()
            .resourceType("OperationOutcome")
            .id("allok")
            .text(
                Narrative.builder()
                    .status(NarrativeStatus.additional)
                    .div("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p>ALL OK</p></div>")
                    .build())
            .issue(
                Collections.singletonList(
                    Issue.builder()
                        .severity(IssueSeverity.information)
                        .code("informational")
                        .details(CodeableConcept.builder().text("ALL OK").build())
                        .build()))
            .build();

    Patient patient =
        JacksonConfig.createMapper()
            .readValue(getClass().getResourceAsStream("/cdw/old-patient-1.03.json"), Patient.class);
    patient.resourceType(null);

    Bundle bundle =
        Patient.Bundle.builder()
            .type(BundleType.searchset)
            .resourceType("Bundle")
            .entry(
                Collections.singletonList(
                    Patient.Entry.builder()
                        .fullUrl("http://example.com")
                        .resource(patient)
                        .build()))
            .build();
    controller.validate(bundle);
  }
}
