package gov.va.api.health.argonaut.service.healthcheck;

import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Component
@Slf4j
public class SteelThreadSystemCheck implements HealthIndicator {

  private final MrAndersonClient client;

  @Value("${health.id}")
  private final String id;

  @Override
  @SneakyThrows
  public Health health() {
    try {
      client.search(query());
      return Health.up().withDetail("Status", "JArgonaut, MR-Anderson, IDS, and DB Stored Procedure all working as expected").build();
    } catch (MrAndersonClient.MrAndersonServiceException e) {
      return Health.down().withDetail("Error", e).build();
    } catch (Exception e) {
      log.error("Failed to complete health check.",e);
      throw e;
    }

  }

  private Query<CdwMedication101Root> query() {
    return Query.forType(CdwMedication101Root.class)
        .resource("Medication")
        .profile(Query.Profile.ARGONAUT)
        .version("1.01")
        .parameters(Parameters.forIdentity(id))
        .build();
  }
}
