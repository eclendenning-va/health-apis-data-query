package gov.va.health.api.sentinel;

import static org.hamcrest.CoreMatchers.equalTo;

import gov.va.health.api.sentinel.categories.Local;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class HealthCheckIT {
  @Category(Local.class)
  @Test
  public void argonautIsHealthy() {
    DataQueryTestClients.argonaut()
        .get("/actuator/health")
        .response()
        .then()
        .body("status", equalTo("UP"));
  }

  @Category(Local.class)
  @Test
  public void idsIsHealthy() {
    DataQueryTestClients.ids()
        .get("/actuator/health")
        .response()
        .then()
        .body("status", equalTo("UP"));
  }

  @Category(Local.class)
  @Test
  public void mrAndersonIsHealthy() {
    DataQueryTestClients.mrAnderson()
        .get("/actuator/health")
        .response()
        .then()
        .body("status", equalTo("UP"));
  }
}
