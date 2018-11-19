package gov.va.api.health.argonaut.api.resources;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import gov.va.api.health.argonaut.api.Fhir;
import gov.va.api.health.argonaut.api.datatypes.Annotation;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Identifier;
import gov.va.api.health.argonaut.api.datatypes.SimpleQuantity;
import gov.va.api.health.argonaut.api.datatypes.SimpleResource;
import gov.va.api.health.argonaut.api.elements.BackboneElement;
import gov.va.api.health.argonaut.api.elements.Extension;
import gov.va.api.health.argonaut.api.elements.Meta;
import gov.va.api.health.argonaut.api.elements.Narrative;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Patient.Contact;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
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
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-immunization.html"
)
public class Immunization implements Resource {
  @Pattern(regexp = Fhir.ID)
  String id;

  @NotBlank String resourceType;
  @Valid Meta meta;

  @Pattern(regexp = Fhir.URI)
  String implicitRules;

  @Pattern(regexp = Fhir.CODE)
  String language;

  @Valid Narrative text;
  @Valid List<SimpleResource> contained;
  @Valid List<Extension> extension;
  @Valid List<Extension> modifierExtension;
  @NotEmpty @Valid List<Identifier> identifier;
  @NotNull Status status;
  @NotBlank @Pattern(regexp = Fhir.DATETIME)
  String date;
  @NotNull @Valid CodeableConcept vaccineCode;
  @NotNull @Valid Reference patient;
  @NotNull Boolean wasNotGiven;
  @NotNull Boolean reported;
  @Valid Reference performer;
  @Valid Reference requester;
  @Valid Reference encounter;
  @Valid Reference manufacturer;
  @Valid Reference location;
  String lotNumber;
  @Pattern(regexp = Fhir.DATE)
  String expirationDate;
  @Valid CodeableConcept site;
  @Valid CodeableConcept route;
  @Valid SimpleQuantity doseQuantity;
  @Valid List<Annotation> note;
  @Valid Explanation explanation;
  @Valid List<Reaction> reaction;
  @Valid List<VaccinationProtocol> vaccinationProtocol;

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Explanation implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;
    @Valid List<Extension> extension;
    @Valid List<Extension> modifierExtension;
    @Valid List<CodeableConcept> reason;
    @Valid List<CodeableConcept> reasonNotGiven;
  }

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Reaction implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;
    @Valid List<Extension> extension;
    @Valid List<Extension> modifierExtension;
    @Pattern(regexp = Fhir.DATETIME)
    String date;
    @Valid Reference detail;
    Boolean reported;
  }

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class VaccinationProtocol implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;
    @Valid List<Extension> extension;
    @Valid List<Extension> modifierExtension;
    @Pattern(regexp = Fhir.POSITIVEINT)
    @NotNull String doseSequence;
    String description;
    @Valid Reference authority;
    String series;
    @Pattern(regexp = Fhir.POSITIVEINT)
    String seriesDoses;
    @NotEmpty @Valid List<CodeableConcept> targetDisease;
    @NotNull @Valid CodeableConcept doseStatus;
    @Valid CodeableConcept doseStatusReason;
  }

  @SuppressWarnings("unused")
  public enum Status {
    @JsonProperty("in-progress")
    in_progress,
    @JsonProperty("on-hold")
    on_hold,
    completed,
    @JsonProperty("entered-in-error")
    entered_in_error,
    stopped
  }
}
