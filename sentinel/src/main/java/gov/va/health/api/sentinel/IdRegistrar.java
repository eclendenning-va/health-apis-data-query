package gov.va.health.api.sentinel;

import static java.util.Collections.singletonList;

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
class IdRegistrar {

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

  private ResourceIdentity id(String type, String id) {
    return ResourceIdentity.builder().system("CDW").resource(type).identifier(id).build();
  }

  /** Register a CDW identity and return the registered UUID. */
  @SuppressWarnings("SameParameterValue")
  String register(String resource, String id) {
    ResourceIdentity identity = id(resource, id);
    log.info("Registering {}", identity);
    List<Registration> registrations =
        system()
            .clients()
            .ids()
            .post("/api/v1/ids", singletonList(identity))
            .expect(201)
            .expectListOf(Registration.class);
    return findUuid(registrations, identity);
  }

  private TestIds registerCdwIds() {
    TestIds cdwIds = system().cdwIds();
    ResourceIdentity patient = id("PATIENT", cdwIds.patient());
    ResourceIdentity medication = id("MEDICATION", cdwIds.medication());
    ResourceIdentity observation = id("OBSERVATION", cdwIds.observation());
    ResourceIdentity condition = id("CONDITION", cdwIds.condition());

    List<ResourceIdentity> identities = Arrays.asList(patient, medication, observation, condition);
    log.info("Registering {}", identities);
    List<Registration> registrations =
        system()
            .clients()
            .ids()
            .post("/api/v1/ids", identities)
            .expect(201)
            .expectListOf(Registration.class);
    TestIds publicIds =
        cdwIds
            .toBuilder()
            .patient(findUuid(registrations, patient))
            .medication(findUuid(registrations, medication))
            .observation(findUuid(registrations, observation))
            .condition(findUuid(registrations, condition))
            .build();
    log.info("Using {}", publicIds);
    return publicIds;
  }
}
