package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.va.api.health.argonaut.api.validation.RelatedFields;
import gov.va.api.health.argonaut.api.validation.ZeroOrOneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
  @NotEmpty @Valid List<Identifier> identifier;

  @Valid List<Extension> modifierExtension;

  Boolean active;

  @NotEmpty @Valid List<HumanName> name;

  @Valid List<ContactPoint> telecom;

  @NotNull Gender gender;

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
  @Valid List<Communication> communication;
  @Valid List<Reference> careProvider;
  @Valid Reference managingOrganization;
  @Valid List<Link> link;

  public enum Gender {
    male,
    female,
    other,
    unknown
  }

  @JsonIgnore
  @AssertTrue(message = "Argo-Ethnicity extension is not valid")
  private boolean isValidEthnicityExtension() {
    if (extension == null) {
      return true;
    }
    Optional<Extension> ethnicityExtension =
        extension
            .stream()
            .filter(
                e ->
                    "http://fhir.org/guides/argonaut/StructureDefinition/argo-ethnicity"
                        .equals(e.url))
            .findFirst();
    if (!ethnicityExtension.isPresent()) {
      return true;
    }
    int ombExtensionCount = 0;
    int textExtensionCount = 0;
    for (Extension e : ethnicityExtension.get().extension) {
      switch (e.url) {
        case "ombCategory":
          ombExtensionCount++;
          break;
        case "text":
          textExtensionCount++;
          break;
        default:
          break;
      }
    }
    return ombExtensionCount <= 1 && textExtensionCount == 1;
  }
}
