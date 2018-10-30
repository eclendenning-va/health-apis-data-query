package gov.va.api.health.mranderson.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.mranderson.Samples;
import gov.va.api.health.mranderson.cdw.Profile;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources;
import gov.va.api.health.mranderson.util.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest
public class MrAndersonV1ApiControllerTest {

  @MockBean Resources resources;
  @Autowired private WebTestClient client;

  @Test
  public void searchesAreForwardedToResourceRepository() {
    when(resources.search(Mockito.any())).thenReturn(Samples.create().patient());
    client
        .get()
        .uri("/api/v1/resources/argonaut/Patient/1.01?identity=123")
        .exchange()
        .expectStatus()
        .isOk();
    verify(resources).search(query());
  }

  private Query query() {
    return Query.builder()
        .profile(Profile.ARGONAUT)
        .resource("Patient")
        .version("1.01")
        .count(15)
        .page(1)
        .parameters(Parameters.builder().add("identity", "123").build())
        .build();
  }
}
