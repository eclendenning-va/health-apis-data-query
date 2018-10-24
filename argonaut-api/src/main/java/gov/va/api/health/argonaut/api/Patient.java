package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

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

  @Valid ArgoRaceExtension argoRace;

  @Valid ArgoEthnicityExtension argoEthnicity;

  @Valid ArgoBirthSexExtension argoBirthSex;

  @Valid Identifier identifier;

  @Valid Extension extension;

  @Valid Extension modifierExtension;

  Boolean active;

  @Valid HumanName name;

  @Valid ContactPoint telecom;

  @Pattern(regexp = Fhir.CODE)
  Gender gender;

  @Pattern(regexp = Fhir.DATE )
  String birthDate;

  // TODO mutualExclusive validator
  boolean deceasedBoolean;
  @Pattern(regexp = Fhir.DATETIME)
  String deceasedDateTime;

  @Valid Address address;

  @Valid CodeableConcept maritalStatus;
  
  Boolean multipleBirthBoolean;
  Integer multipleBirthInteger;

  @Valid List<Attachment> photo;

  @Valid List<Contact> contact;

  @Valid List<Communication> communication;

  @Valid List<Reference> careProvider;

  @Valid Reference managingOrganization;
  //link
  @Valid List<Link> link;

  public enum Gender {
    male,
    female,
    other,
    unknown
  }
}
