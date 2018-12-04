package gov.va.api.health.argonaut.api.resources;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import gov.va.api.health.argonaut.api.Fhir;
import gov.va.api.health.argonaut.api.datatypes.Address;
import gov.va.api.health.argonaut.api.datatypes.Attachment;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint;
import gov.va.api.health.argonaut.api.datatypes.HumanName;
import gov.va.api.health.argonaut.api.datatypes.Identifier;
import gov.va.api.health.argonaut.api.datatypes.Period;
import gov.va.api.health.argonaut.api.datatypes.SimpleResource;
import gov.va.api.health.argonaut.api.elements.BackboneElement;
import gov.va.api.health.argonaut.api.elements.Extension;
import gov.va.api.health.argonaut.api.elements.Meta;
import gov.va.api.health.argonaut.api.elements.Narrative;
import gov.va.api.health.argonaut.api.elements.Reference;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(
    description = "https://www.hl7.org/fhir/DSTU2/practitioner.html"
)
public class Practitioner implements DomainResource {
  @NotBlank String resourceType;

  @Pattern(regexp = Fhir.ID)
  String id;
  @Valid Meta meta;
  @Pattern(regexp = Fhir.URI)
  String implicitRules;
  @Pattern(regexp = Fhir.CODE)
  String language;
  @Valid Narrative text;
  @Valid List<SimpleResource> contained;
  @Valid List<Extension> extension;
  @Valid List<Extension> modifierExtension;
  @Valid List<Identifier> identifier;

  boolean active;
  @Valid HumanName name;
  @Valid List<ContactPoint> telecom;
  @Valid List<Address> address;
  Gender gender;
  @Pattern(regexp = Fhir.DATE)
  String birthDate;
  @Valid List<Attachment> photo;
  @Valid List<PractitionerRole> practitionerRole;
  @Valid List<Qualification> qualification;
  @Valid List<CodeableConcept> communication;

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Qualification implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;
    @Valid List<Extension> extension;
    @Valid List<Extension> modifierExtension;

    @Valid List<Identifier> identifier;
    @Valid @NotNull CodeableConcept code;
    @Valid Period period;
    @Valid Reference issuer;
  }


  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class PractitionerRole implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;
    @Valid List<Extension> extension;
    @Valid List<Extension> modifierExtension;

    @Valid Reference managingOrganization;
    @Valid CodeableConcept role;
    @Valid List<CodeableConcept> specialty;
    @Valid Period period;
    @Valid List<Reference> location;
    @Valid List<Reference> healthcareService;
  }
  public enum Gender{
    male,
    female,
    other,
    unknown
  }

}
