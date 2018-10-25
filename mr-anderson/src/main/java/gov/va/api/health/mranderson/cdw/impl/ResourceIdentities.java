package gov.va.api.health.mranderson.cdw.impl;

import gov.va.api.health.ids.api.Registration;
import gov.va.api.health.ids.api.ResourceIdentity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ResourceIdentities {

  static boolean isCdw(@NonNull ResourceIdentity identity) {
    return cdw().equals(identity.system());
  }

  public static String cdw() {
    return "CDW";
  }

  static ResourceIdentity referenceToResourceIdentity(@NonNull String typeSlashId) {
    String[] parts = typeSlashId.split("/");
    if (parts.length != 2) {
      throw new IllegalArgumentException(
          "Invalid reference. Expected resource/identity. Got: " + typeSlashId);
    }
    String type = parts[0];
    String id = parts[1];
    return ResourceIdentity.builder()
        .system(ResourceIdentities.cdw())
        .resource(ResourceNameTranslation.create().fhirToIdentityService(type))
        .identifier(id)
        .build();
  }

  static ReferencePair referencesOf(@NonNull Registration registration) {
    ResourceIdentity identity = findCdwIdentity(registration);

    String resource = ResourceNameTranslation.create().identityServiceToFhir(identity.resource());

    return ReferencePair.builder()
        .cdw(resource + "/" + identity.identifier())
        .universal(resource + "/" + registration.uuid())
        .build();
  }

  private static ResourceIdentity findCdwIdentity(@NonNull Registration registration) {
    return registration
        .resourceIdentities()
        .stream()
        .filter(ResourceIdentities::isCdw)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No CDW identity for: " + registration));
  }

  @Value
  @Builder
  static class ReferencePair {
    String cdw;
    String universal;
  }
}
