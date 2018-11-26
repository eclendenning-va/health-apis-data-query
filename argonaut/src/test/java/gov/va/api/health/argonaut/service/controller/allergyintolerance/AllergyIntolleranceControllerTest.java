package gov.va.api.health.argonaut.service.controller.allergyintolerance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AllergyIntolleranceControllerTest {
  @Mock MrAndersonClient client;

  @Mock AllergyIntoleranceController.Transformer tx;

  AllergyIntoleranceController controller;
  @Mock HttpServletRequest servletRequest;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new AllergyIntoleranceController(tx, client);
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
}
