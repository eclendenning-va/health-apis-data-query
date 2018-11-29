package gov.va.api.health.argonaut.service.controller.medicationorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MedicationOrderControllerTest {
  @Mock MrAndersonClient client;

  @Mock MedicationOrderController.Transformer tx;

  MedicationOrderController controller;
  @Mock HttpServletRequest servletRequest;
  @Mock Bundler bundler;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new MedicationOrderController(tx, client);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void read() {
    CdwMedicationOrder103Root root = new CdwMedicationOrder103Root();
    root.setMedicationOrders(new CdwMedicationOrders());
    CdwMedicationOrder xmlMedicationOrder = new CdwMedicationOrder();
    root.getMedicationOrders().getMedicationOrder().add(xmlMedicationOrder);
    MedicationOrder item = MedicationOrder.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(tx.apply(xmlMedicationOrder)).thenReturn(item);
    MedicationOrder actual = controller.read("hello");
    assertThat(actual).isSameAs(item);
    ArgumentCaptor<Query<CdwMedicationOrder103Root>> captor = ArgumentCaptor.forClass(Query.class);
    verify(client).search(captor.capture());
    assertThat(captor.getValue().parameters()).isEqualTo(Parameters.forIdentity("hello"));
  }
}
