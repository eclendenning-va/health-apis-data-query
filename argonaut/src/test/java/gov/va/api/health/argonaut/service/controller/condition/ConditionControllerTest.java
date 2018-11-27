package gov.va.api.health.argonaut.service.controller.condition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwCondition103Root;
import gov.va.dvp.cdw.xsd.model.CdwCondition103Root.CdwConditions;
import gov.va.dvp.cdw.xsd.model.CdwCondition103Root.CdwConditions.CdwCondition;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ConditionControllerTest {

  @Mock MrAndersonClient client;

  @Mock ConditionController.Transformer transformer;

  ConditionController controller;
  @Mock HttpServletRequest servletRequest;
  @Mock Bundler bundler;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new ConditionController(transformer, client);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void read() {
    CdwCondition103Root root = new CdwCondition103Root();
    root.setConditions(new CdwConditions());
    CdwCondition xmlObservation = new CdwCondition();
    root.getConditions().getCondition().add(xmlObservation);
    Condition item = Condition.builder().build();
    when(client.search(Mockito.any())).thenReturn(root);
    when(transformer.apply(xmlObservation)).thenReturn(item);
    Condition actual = controller.read("hello");
    assertThat(actual).isSameAs(item);
    ArgumentCaptor<Query<CdwCondition103Root>> captor = ArgumentCaptor.forClass(Query.class);
    verify(client).search(captor.capture());
    assertThat(captor.getValue().parameters()).isEqualTo(Parameters.forIdentity("hello"));
  }
}
