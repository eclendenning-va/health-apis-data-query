package gov.va.health.api.sentinel;

import gov.va.api.health.ids.api.Registration;
import gov.va.api.health.ids.api.ResourceIdentity;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * The ID Registrar will register CDW ids with the Identity Service then publish the public UUIDs.
 */
@Value
@AllArgsConstructor(staticName = "of")
@Slf4j
public class IdRegistrar {

  SystemDefinition system;

  @Getter(lazy = true)
  TestIds registeredIds = registerCdwIds();

  private String findUuid(List<Registration> registrations, ResourceIdentity resource) {
    return registrations
        .stream()
        .filter(r -> r.resourceIdentities().contains(resource))
        .findFirst()
        .orElseThrow(() -> new AssertionError("Failed to register: " + resource))
        .uuid();
  }

  /** Register a CDW identity and return the registered UUID. */
  public String register(String resource, String id) {
    ResourceIdentity identity =
        ResourceIdentity.builder().system("CDW").resource(resource).identifier(id).build();
    log.info("Registering {}", identity);
    List<Registration> registrations =
        system()
            .clients()
            .ids()
            .post("/api/v1/ids", Arrays.asList(identity))
            .expect(201)
            .expectListOf(Registration.class);
    return findUuid(registrations, identity);
  }

  private TestIds registerCdwIds() {
    TestIds cdwIds = system().cdwIds();
    ResourceIdentity patient =
        ResourceIdentity.builder()
            .system("CDW")
            .resource("PATIENT")
            .identifier(cdwIds.patient())
            .build();
    ResourceIdentity medication =
        ResourceIdentity.builder()
            .system("CDW")
            .resource("MEDICATION")
            .identifier(cdwIds.medication())
            .build();
    List<ResourceIdentity> identities = Arrays.asList(patient, medication);
    log.info("Registering {}", identities);
    List<Registration> registrations =
        system()
            .clients()
            .ids()
            .post("/api/v1/ids", identities)
            .expect(201)
            .expectListOf(Registration.class);
    TestIds publicIds =
        TestIds.builder()
            .unknown(cdwIds.unknown())
            .patient(findUuid(registrations, patient))
            .medication(findUuid(registrations, medication))
            .pii(cdwIds.pii())
            .build();
    log.info("Using {}", publicIds);
    return publicIds;
  }
}
