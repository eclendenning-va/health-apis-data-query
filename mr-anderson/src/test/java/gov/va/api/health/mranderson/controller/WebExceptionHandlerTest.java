package gov.va.api.health.mranderson.controller;

import static org.mockito.Mockito.when;

import gov.va.api.health.ids.api.IdentityService.LookupFailed;
import gov.va.api.health.ids.api.IdentityService.RegistrationFailed;
import gov.va.api.health.ids.api.IdentityService.UnknownIdentity;
import gov.va.api.health.mranderson.cdw.Profile;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources;
import gov.va.api.health.mranderson.cdw.Resources.MissingSearchParameters;
import gov.va.api.health.mranderson.cdw.Resources.SearchFailed;
import gov.va.api.health.mranderson.cdw.Resources.UnknownIdentityInSearchParameter;
import gov.va.api.health.mranderson.cdw.Resources.UnknownResource;
import gov.va.api.health.mranderson.util.Parameters;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec;

@RunWith(Parameterized.class)
@WebFluxTest
public class WebExceptionHandlerTest {

  @ClassRule public static final SpringClassRule spring = new SpringClassRule();
  @Rule public final SpringMethodRule springMethod = new SpringMethodRule();

  @Parameter(0)
  public HttpStatus status;

  @Parameter(1)
  public Exception exception;

  @MockBean Resources resources;
  @Autowired private WebTestClient client;

  @Parameterized.Parameters(name = "{index}:{0} - {1}")
  public static List<Object[]> parameters() {
    Query query =
        Query.builder()
            .profile(Profile.ARGONAUT)
            .resource("Patient")
            .version("1.01")
            .count(15)
            .page(1)
            .parameters(Parameters.builder().add("identity", "123").build())
            .build();
    return Arrays.asList(
        test(HttpStatus.NOT_FOUND, new UnknownResource(query)),
        test(HttpStatus.NOT_FOUND, new UnknownIdentityInSearchParameter(query, null)),
        test(HttpStatus.BAD_REQUEST, new MissingSearchParameters(query)),
        test(HttpStatus.BAD_REQUEST, new ConstraintViolationException(new HashSet<>())),
        test(HttpStatus.INTERNAL_SERVER_ERROR, new SearchFailed(query, "")),
        test(HttpStatus.INTERNAL_SERVER_ERROR, new LookupFailed("1", "")),
        test(HttpStatus.INTERNAL_SERVER_ERROR, new RegistrationFailed("")),
        test(HttpStatus.INTERNAL_SERVER_ERROR, new UnknownIdentity("1")),
        test(HttpStatus.INTERNAL_SERVER_ERROR, new RuntimeException())
        //
        );
  }

  private static Object[] test(HttpStatus status, Exception exception) {
    return new Object[] {status, exception};
  }

  @Test
  public void expectStatus() {
    when(resources.search(Mockito.any())).thenThrow(exception);
    BodySpec<ErrorResponse, ?> body =
        client
            .get()
            .uri("/api/v1/resources/argonaut/Patient/1.01?identity=123")
            .exchange()
            .expectStatus()
            .isEqualTo(status)
            .expectBody(ErrorResponse.class);
    ErrorResponse error = body.returnResult().getResponseBody();
    error.hashCode();
    error.toString();
    error.equals(error);
  }
}
