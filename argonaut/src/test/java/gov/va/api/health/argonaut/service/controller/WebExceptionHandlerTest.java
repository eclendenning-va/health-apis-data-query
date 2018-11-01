package gov.va.api.health.argonaut.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import gov.va.api.health.argonaut.api.OperationOutcome;
import gov.va.api.health.argonaut.service.controller.patient.PatientController;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient.BadRequest;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient.NotFound;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient.SearchFailed;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.api.health.argonaut.service.mranderson.client.Query.Profile;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec;

@RunWith(Parameterized.class)
@WebFluxTest
@Import({JacksonConfig.class})
public class WebExceptionHandlerTest {

  @ClassRule public static final SpringClassRule spring = new SpringClassRule();
  @Rule public final SpringMethodRule springMethod = new SpringMethodRule();

  @Parameter(0)
  public HttpStatus status;

  @Parameter(1)
  public Exception exception;

  @MockBean MrAndersonClient mrAnderson;
  @MockBean PatientController.PatientTransformer tx;
  @Autowired private WebTestClient client;

  @Parameterized.Parameters(name = "{index}:{0} - {1}")
  public static List<Object[]> parameters() {
    Query query =
        Query.builder()
            .profile(Profile.ARGONAUT)
            .resource("Patient")
            .version("1.01")
            .parameters(Parameters.forIdentity("123"))
            .build();
    return Arrays.asList(
        test(HttpStatus.NOT_FOUND, new NotFound(query)),
        test(HttpStatus.BAD_REQUEST, new BadRequest(query)),
        test(HttpStatus.BAD_REQUEST, new ConstraintViolationException(new HashSet<>())),
        test(HttpStatus.INTERNAL_SERVER_ERROR, new SearchFailed(query)),
        test(HttpStatus.INTERNAL_SERVER_ERROR, new RuntimeException())
        //
        );
  }

  private static Object[] test(HttpStatus status, Exception exception) {
    return new Object[] {status, exception};
  }

  @Test
  public void expectStatus() {

    when(mrAnderson.search(Mockito.any())).thenThrow(exception);
    BodySpec<OperationOutcome, ?> body =
        client
            .get()
            .uri("/api/Patient/123")
            .exchange()
            .expectStatus()
            .isEqualTo(status)
            .expectBody(OperationOutcome.class);
    OperationOutcome error = body.returnResult().getResponseBody();
    assertThat(error).isNotNull();
  }
}
