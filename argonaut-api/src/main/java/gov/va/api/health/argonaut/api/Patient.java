package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
@Schema
public class Patient {

  @NotBlank
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid Meta meta;

  @Pattern(regexp = Fhir.URI)
  @Schema()
  String implicitRules;

  @Pattern(regexp = Fhir.CODE)
  String language;

  @Valid Narrative text;

  @Valid List<Resource> contained;

  @NotBlank @Valid ArgoRaceExtension argoRace;

  @NotBlank @Valid ArgoEthnicityExtension argoEthnicity;

  @NotBlank @Valid ArgoBirthSexExtension argoBirthSex;

  @NotBlank @Valid Identifier identifier;

  @Valid Extension extension;

  @Valid List<Extension> modifierExtension;

  Boolean active;

  @NotBlank @Valid List<HumanName> name;

  @Valid List<ContactPoint> telecom;

  @Pattern(regexp = Fhir.CODE)
  @NotBlank
  Gender gender;

  @Pattern(regexp = Fhir.DATE)
  String birthDate;

  // TODO mutualExclusive validator
  boolean deceasedBoolean;

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
