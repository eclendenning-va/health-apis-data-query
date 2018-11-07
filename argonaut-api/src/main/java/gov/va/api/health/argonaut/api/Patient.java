package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gov.va.api.health.argonaut.api.validation.RelatedFields;
import gov.va.api.health.argonaut.api.validation.ZeroOrOneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonDeserialize(builder = Patient.PatientBuilder.class)
@Schema(
  description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html"
)
@RelatedFields({
  @ZeroOrOneOf(
    fields = {"deceasedBoolean", "deceasedDateTime"},
    message = "Only one deceased value may be specified"
  ),
  @ZeroOrOneOf(
    fields = {"multipleBirthBoolean", "multipleBirthInteger"},
    message = "Only one multiple birth value may be specified"
  )
})
public class Patient {

  @NotBlank
  @Pattern(regexp = Fhir.ID)
  String id;

  @NotBlank String resourceType;
  @Valid Meta meta;

  @Pattern(regexp = Fhir.URI)
  @Schema()
  String implicitRules;

  @Pattern(regexp = Fhir.CODE)
  String language;

  @Valid Narrative text;
  @Valid List<SimpleResource> contained;
  @Valid List<Extension> extension;
  @NotBlank @Valid List<Identifier> identifier;

  @Valid List<Extension> modifierExtension;

  Boolean active;

  @NotBlank @Valid List<HumanName> name;

  @Valid List<ContactPoint> telecom;

  @Pattern(regexp = Fhir.CODE)
  @NotBlank
  Gender gender;

  @Pattern(regexp = Fhir.DATE)
  String birthDate;

  Boolean deceasedBoolean;

  @Pattern(regexp = Fhir.DATETIME)
  String deceasedDateTime;

  @Valid List<Address> address;

  @Valid CodeableConcept maritalStatus;

  Boolean multipleBirthBoolean;
  Integer multipleBirthInteger;

  @Valid List<Attachment> photo;

  @Valid List<Contact> contact;

  @NotBlank @Valid List<Communication> communication;

  @Valid List<Reference> careProvider;

  @Valid Reference managingOrganization;
  // link
  @Valid List<Link> link;

  public enum Gender {
    male,
    female,
    other,
    unknown
  }
}
