package gov.va.api.health.ids.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;

@RunWith(SpringRunner.class)
@WebFluxTest({IdServiceHomeController.class})
public class IdServiceHomeControllerTest {

  @Autowired private WebTestClient client;

  @Test
  @SneakyThrows
  public void openapiYaml() {
    byte[] responseBody =
        client
            .get()
            .uri("/openapi.yaml")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .returnResult()
            .getResponseBody();

    assertThat(new String(responseBody, StandardCharsets.UTF_8))
        .isEqualTo(
            StreamUtils.copyToString(
                getClass().getResourceAsStream("/api-v1.yaml"), StandardCharsets.UTF_8));
  }

  @Test
  @SneakyThrows
  public void openapiJson() {
    client
        .get()
        .uri("/openapi.json")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.openapi", "3.0.1");
  }

  @Test
  @SneakyThrows
  public void openapiYamlFromIndex() {
    client
        .get()
        .uri("/")
        .exchange()
        .expectStatus()
        .isTemporaryRedirect()
        .expectHeader()
        .valueMatches("Location", ".*/openapi.json");
  }
}
