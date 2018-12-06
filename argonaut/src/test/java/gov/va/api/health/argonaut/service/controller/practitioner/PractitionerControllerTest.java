package gov.va.api.health.argonaut.service.controller.practitioner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.resources.Practitioner;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("WeakerAccess")
public class PractitionerControllerTest {

  @Mock MrAndersonClient client;

  @Mock PractitionerController.Transformer tx;

  PractitionerController controller;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new PractitionerController(tx, client);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void read() {
    CdwPractitioner100Root root = new CdwPractitioner100Root();
    root.setPractitioners(new CdwPractitioners());
    CdwPractitioner xmlPractitioner = new CdwPractitioner();
    root.getPractitioners().getPractitioner().add(xmlPractitioner);
    Practitioner item = Practitioner.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(tx.apply(xmlPractitioner)).thenReturn(item);
    Practitioner actual = controller.read("hello");
    assertThat(actual).isSameAs(item);
    ArgumentCaptor<Query<CdwPractitioner100Root>> captor = ArgumentCaptor.forClass(Query.class);
    verify(client).search(captor.capture());
    assertThat(captor.getValue().parameters()).isEqualTo(Parameters.forIdentity("hello"));
  }
}
