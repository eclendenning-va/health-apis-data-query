package gov.va.health.api.sentinel;

import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;

public class HealthCheckIT {

  @Test
  public void argonautIsHealthy() {
    Sentinel.get()
        .clients()
        .argonaut()
        .get("/actuator/health")
        .response()
        .then()
        .body("status", equalTo("UP"));
  }

  @Test
  public void idsIsHealthy() {
    Sentinel.get()
        .clients()
        .ids()
        .get("/actuator/health")
        .response()
        .then()
        .body("status", equalTo("UP"));
  }

  @Test
  public void mrAndersonIsHealthy() {
    Sentinel.get()
        .clients()
        .mrAnderson()
        .get("/actuator/health")
        .response()
        .then()
        .body("status", equalTo("UP"));
  }
}
