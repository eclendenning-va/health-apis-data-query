package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DiagnosticReportControllerTest {

  @Mock MrAndersonClient client;
  @Mock DiagnosticReportController.Transformer tx;
  @Mock HttpServletRequest servletRequest;
  @Mock Bundler bundler;

  DiagnosticReportController controller;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new DiagnosticReportController(tx, client, bundler);
  }


  @Test
  public void read() {
    CdwDiagnosticReport102Root root = new CdwDiagnosticReport102Root();
    root.setDiagnosticReports(new CdwDiagnosticReport102Root.CdwDiagnosticReports());
    CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport cdwDR =
        new CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport();
    root.getDiagnosticReports().getDiagnosticReport().add(cdwDR);
    DiagnosticReport dr = DiagnosticReport.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(tx.apply(cdwDR)).thenReturn(dr);
    DiagnosticReport actual = controller.read("hello");
    assertThat(actual).isSameAs(dr);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<Query<CdwDiagnosticReport102Root>> captor = ArgumentCaptor.forClass(Query.class);
    verify(client).search(captor.capture());
    assertThat(captor.getValue().parameters()).isEqualTo(Parameters.forIdentity("hello"));
  }
}
