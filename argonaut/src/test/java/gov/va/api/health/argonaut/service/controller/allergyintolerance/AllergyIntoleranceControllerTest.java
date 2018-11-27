package gov.va.api.health.argonaut.service.controller.allergyintolerance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Bundle;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Bundler.BundleContext;
import gov.va.api.health.argonaut.service.controller.PageLinks.LinkConfig;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.controller.Validator;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.MultiValueMap;

public class AllergyIntoleranceControllerTest {
  @Mock MrAndersonClient client;

  @Mock AllergyIntoleranceController.Transformer tx;

  AllergyIntoleranceController controller;
  @Mock HttpServletRequest servletRequest;
  @Mock Bundler bundler;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new AllergyIntoleranceController(tx, client, bundler);
  }

  private void assertSearch(Supplier<Bundle> invocation, MultiValueMap<String, String> params) {
    CdwAllergyIntolerance103Root root = new CdwAllergyIntolerance103Root();
    root.setPageNumber(BigInteger.valueOf(1));
    root.setRecordsPerPage(BigInteger.valueOf(10));
    root.setRecordCount(BigInteger.valueOf(3));
    root.setAllergyIntolerances(new CdwAllergyIntolerances());
    CdwAllergyIntolerance xmlAllergyIntolerance1 = new CdwAllergyIntolerance();
    CdwAllergyIntolerance xmlAllergyIntolerance2 = new CdwAllergyIntolerance();
    CdwAllergyIntolerance xmlAllergyIntolerance3 = new CdwAllergyIntolerance();
    root.getAllergyIntolerances()
        .getAllergyIntolerance()
        .addAll(
            Arrays.asList(xmlAllergyIntolerance1, xmlAllergyIntolerance2, xmlAllergyIntolerance3));
    AllergyIntolerance allergyIntolerance1 = AllergyIntolerance.builder().build();
    AllergyIntolerance allergyIntolerance2 = AllergyIntolerance.builder().build();
    AllergyIntolerance allergyIntolerance3 = AllergyIntolerance.builder().build();
    when(tx.apply(xmlAllergyIntolerance1)).thenReturn(allergyIntolerance1);
    when(tx.apply(xmlAllergyIntolerance2)).thenReturn(allergyIntolerance2);
    when(tx.apply(xmlAllergyIntolerance3)).thenReturn(allergyIntolerance3);
    when(client.search(Mockito.any())).thenReturn(root);
    when(servletRequest.getRequestURI()).thenReturn("/api/AllergyIntolerance");

    Bundle mockBundle = new Bundle();
    when(bundler.bundle(Mockito.any())).thenReturn(mockBundle);

    Bundle actual = invocation.get();

    assertThat(actual).isSameAs(mockBundle);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<
            BundleContext<
                CdwAllergyIntolerance, AllergyIntolerance, AllergyIntolerance.Entry, Bundle>>
        captor = ArgumentCaptor.forClass(BundleContext.class);

    verify(bundler).bundle(captor.capture());

    LinkConfig expectedLinkConfig =
        LinkConfig.builder()
            .page(1)
            .recordsPerPage(10)
            .totalRecords(3)
            .path("/api/AllergyIntolerance")
            .queryParams(params)
            .build();
    assertThat(captor.getValue().linkConfig()).isEqualTo(expectedLinkConfig);
    assertThat(captor.getValue().xmlItems())
        .isEqualTo(root.getAllergyIntolerances().getAllergyIntolerance());
    assertThat(captor.getValue().newBundle().get()).isInstanceOf(AllergyIntolerance.Bundle.class);
    assertThat(captor.getValue().newEntry().get()).isInstanceOf(AllergyIntolerance.Entry.class);
    assertThat(captor.getValue().transformer()).isSameAs(tx);
  }

  @Test
  public void read() {
    CdwAllergyIntolerance103Root root = new CdwAllergyIntolerance103Root();
    root.setAllergyIntolerances(new CdwAllergyIntolerances());
    CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance
        xmlAllergyIntolerance =
            new CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance();
    root.getAllergyIntolerances().getAllergyIntolerance().add(xmlAllergyIntolerance);
    AllergyIntolerance allergyIntolerance = AllergyIntolerance.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(tx.apply(xmlAllergyIntolerance)).thenReturn(allergyIntolerance);
    AllergyIntolerance actual = controller.read("hello");
    assertThat(actual).isSameAs(allergyIntolerance);
    ArgumentCaptor<Query<CdwAllergyIntolerance103Root>> captor =
        ArgumentCaptor.forClass(Query.class);
    verify(client).search(captor.capture());
    assertThat(captor.getValue().parameters()).isEqualTo(Parameters.forIdentity("hello"));
  }

  @Test
  public void searchById() {
    assertSearch(
        () -> controller.searchById("me", 1, 10, servletRequest),
        Parameters.builder().add("_id", "me").add("page", 1).add("_count", 10).build());
  }

  @Test
  public void searchByPatient() {
    assertSearch(
        () -> controller.searchByPatient("me", 1, 10, servletRequest),
        Parameters.builder().add("patient", "me").add("page", 1).add("_count", 10).build());
  }

  @Test
  public void searchReturnsEmptyResults() {
    CdwAllergyIntolerance103Root root = new CdwAllergyIntolerance103Root();
    root.setPageNumber(BigInteger.valueOf(1));
    root.setRecordsPerPage(BigInteger.valueOf(10));
    root.setRecordCount(BigInteger.valueOf(0));
    when(client.search(Mockito.any())).thenReturn(root);
    when(servletRequest.getRequestURI()).thenReturn("/api/AllergyIntolerance");
    Bundle mockBundle = new Bundle();
    when(bundler.bundle(Mockito.any())).thenReturn(mockBundle);
    Bundle actual = controller.searchById("me", 1, 10, servletRequest);
    assertThat(actual).isSameAs(mockBundle);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<
            BundleContext<
                CdwAllergyIntolerance, AllergyIntolerance, AllergyIntolerance.Entry,
                AllergyIntolerance.Bundle>>
        captor = ArgumentCaptor.forClass(BundleContext.class);

    verify(bundler).bundle(captor.capture());
    LinkConfig expectedLinkConfig =
        LinkConfig.builder()
            .page(1)
            .recordsPerPage(10)
            .totalRecords(0)
            .path("/api/AllergyIntolerance")
            .queryParams(
                Parameters.builder().add("_id", "me").add("page", 1).add("_count", 10).build())
            .build();
    assertThat(captor.getValue().linkConfig()).isEqualTo(expectedLinkConfig);
    assertThat(captor.getValue().xmlItems()).isEmpty();
  }
}
