package gov.va.api.health.argonaut.service.healthcheck;

import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.api.health.argonaut.service.mranderson.client.RestMrAndersonClient;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import javax.xml.bind.annotation.XmlRootElement;



@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Component

public class SteelThreadSystemCheck implements HealthIndicator {

  RestMrAndersonClient client;

  @Value("${health.resource}")
  String resource;
  @Value("${health.version}")
  String version;
  @Value("${health.id}")
  String id;

  @Override
  public Health health() {
    try {
      client.search(query());
      return Health.up().withDetail("Status", "JArgonaut, MR-Anderson, IDS, and DB Stored Procedure all working as expected").build();
    } catch (Exception e) {
      return Health.down().withDetail("Error", client.search(query())).build();
    }
  }

  private Query<PatientSearchResultsRoot> query() {
    return Query.forType(PatientSearchResultsRoot.class)
        .resource(resource)
        .profile(Query.Profile.ARGONAUT)
        .version(version)
        .parameters(Parameters.forIdentity(id))
        .build();
  }
  @XmlRootElement(name = "root")
  public static class PatientSearchResultsRoot extends Patient103Root {}
}
