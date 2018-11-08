package gov.va.api.health.mranderson.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.mranderson.Samples;
import gov.va.api.health.mranderson.cdw.Profile;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources;
import gov.va.api.health.mranderson.util.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

public class MrAndersonV1ApiControllerTest {

  @Mock Resources resources;

  @Mock ServerWebExchange exchange;
  @Mock ServerHttpRequest request;

  MrAndersonV1ApiController controller;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new MrAndersonV1ApiController(resources);
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

  @Test
  public void searchesAreForwardedToResourceRepository() {
    when(resources.search(Mockito.any())).thenReturn(Samples.create().patient());
    when(exchange.getRequest()).thenReturn(request);
    when(request.getQueryParams()).thenReturn(Parameters.builder().add("identity", "123").build());
    controller.queryResourceVersion(Profile.ARGONAUT, "Patient", "1.01", 1, 15, exchange);
    verify(resources).search(query());
  }
}
