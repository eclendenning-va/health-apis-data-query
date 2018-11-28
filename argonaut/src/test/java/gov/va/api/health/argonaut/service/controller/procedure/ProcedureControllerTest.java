package gov.va.api.health.argonaut.service.controller.procedure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.bundle.AbstractBundle.BundleType;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.Observation.Entry;
import gov.va.api.health.argonaut.api.resources.Procedure;
import gov.va.api.health.argonaut.api.resources.Procedure.Bundle;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Bundler.BundleContext;
import gov.va.api.health.argonaut.service.controller.PageLinks.LinkConfig;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.controller.observation.ObservationController;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwProcedure101Root.CdwProcedures.CdwProcedure104Root;
import gov.va.dvp.cdw.xsd.model.CdwProcedure101Root.CdwProcedures.CdwProcedures;
import gov.va.dvp.cdw.xsd.model.CdwProcedure101Root.CdwProcedures.CdwProcedure;
import gov.va.dvp.cdw.xsd.model.CdwProcedure101Root;
import gov.va.dvp.cdw.xsd.model.CdwProcedure101Root.CdwProcedures;
import gov.va.dvp.cdw.xsd.model.CdwProcedure101Root.CdwProcedures.CdwProcedure;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.MultiValueMap;

public class ProcedureControllerTest {

  @Mock
  MrAndersonClient client;


  @Mock ProcedureController.Transformer tx;

  ProcedureController controller;
  @Mock
  HttpServletRequest servletRequest;
  @Mock
  Bundler bundler;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new ProcedureController(tx, client, bundler);
  }

  private void assertSearch(
      Supplier<Procedure.Bundle> invocation, MultiValueMap<String, String> params) {
    CdwProcedure101Root root = new CdwProcedure101Root();
    root.setPageNumber(1);
    root.setRecordsPerPage(10);
    root.setRecordCount(3);
    root.setProcedures(new CdwProcedures());
    CdwProcedure cdwItem1 = new CdwProcedure();
    CdwProcedure cdwItem2 = new CdwProcedure();
    CdwProcedure cdwItem3 = new CdwProcedure();
    root.getProcedures().getProcedure().addAll(Arrays.asList(cdwItem1, cdwItem2, cdwItem3));
    Procedure patient1 = Procedure.builder().build();
    Procedure patient2 = Procedure.builder().build();
    Procedure patient3 = Procedure.builder().build();
    when(tx.apply(cdwItem1)).thenReturn(patient1);
    when(tx.apply(cdwItem2)).thenReturn(patient2);
    when(tx.apply(cdwItem3)).thenReturn(patient3);
    when(client.search(Mockito.any())).thenReturn(root);
    when(servletRequest.getRequestURI()).thenReturn("/api/Procedure");

    Procedure.Bundle mockBundle = new Procedure.Bundle();
    when(bundler.bundle(Mockito.any())).thenReturn(mockBundle);

    Procedure.Bundle actual = invocation.get();

    assertThat(actual).isSameAs(mockBundle);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<BundleContext<CdwProcedure, Procedure, Procedure.Entry, Procedure.Bundle>> captor =
        ArgumentCaptor.forClass(BundleContext.class);

    verify(bundler).bundle(captor.capture());

    LinkConfig expectedLinkConfig =
        LinkConfig.builder()
            .page(1)
            .recordsPerPage(10)
            .totalRecords(3)
            .path("/api/Procedure")
            .queryParams(params)
            .build();
    assertThat(captor.getValue().linkConfig()).isEqualTo(expectedLinkConfig);
    assertThat(captor.getValue().xmlItems()).isEqualTo(root.getProcedures().getProcedure());
    assertThat(captor.getValue().newBundle().get()).isInstanceOf(Procedure.Bundle.class);
    assertThat(captor.getValue().newEntry().get()).isInstanceOf(Procedure.Entry.class);
    assertThat(captor.getValue().transformer()).isSameAs(tx);
  }

  private Bundle bundleOf(Procedure resource) {
    return Bundle.builder()
        .type(BundleType.searchset)
        .resourceType("Bundle")
        .entry(
            Collections.singletonList(
                Procedure.Entry.builder()
                .fullUrl("http://example.com")
                .resource(resource)
                .build()
            )
        )
        .build();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void read() {
    CdwProcedure101Root root = new CdwProcedure101Root();
    root.setProcedures(new CdwProcedures());
    CdwProcedure xmlProcedure = new CdwProcedure();
    root.getProcedures().getProcedure().add(xmlProcedure);
    Procedure item = Procedure.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(tx.apply(xmlProcedure)).thenReturn(item);
    Procedure actual = controller.read("hello");
    assertThat(actual).isSameAs(item);
    ArgumentCaptor<Query<CdwProcedure101Root>> captor = ArgumentCaptor.forClass(Query.class);
    verify(client).search(captor.capture());
    assertThat(captor.getValue().parameters()).isEqualTo(Parameters.forIdentity("hello"));
  }
}
