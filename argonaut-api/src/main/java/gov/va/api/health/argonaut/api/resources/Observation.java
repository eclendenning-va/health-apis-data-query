package gov.va.api.health.argonaut.api.resources;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import gov.va.api.health.argonaut.api.Fhir;
import gov.va.api.health.argonaut.api.datatypes.Attachment;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Identifier;
import gov.va.api.health.argonaut.api.datatypes.Period;
import gov.va.api.health.argonaut.api.datatypes.Quantity;
import gov.va.api.health.argonaut.api.datatypes.Range;
import gov.va.api.health.argonaut.api.datatypes.Ratio;
import gov.va.api.health.argonaut.api.datatypes.SampledData;
import gov.va.api.health.argonaut.api.datatypes.SimpleQuantity;
import gov.va.api.health.argonaut.api.datatypes.SimpleResource;
import gov.va.api.health.argonaut.api.elements.BackboneElement;
import gov.va.api.health.argonaut.api.elements.Extension;
import gov.va.api.health.argonaut.api.elements.Meta;
import gov.va.api.health.argonaut.api.elements.Narrative;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.validation.RelatedFields;
import gov.va.api.health.argonaut.api.validation.ZeroOrOneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
@JsonAutoDetect(
  fieldVisibility = JsonAutoDetect.Visibility.ANY,
  isGetterVisibility = Visibility.NONE
)
@Schema(
  description =
      "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-observationresults.html"
)
@RelatedFields({
  @ZeroOrOneOf(
    fields = {"effectiveDateTime", "effectivePeriod"},
    message = "Only one effective value may be specified"
  ),
  @ZeroOrOneOf(
    fields = {
      "valueAttachment",
      "valueCodeableConcept",
      "valueDateTime",
      "valuePeriod",
      "valueQuantity",
      "valueRange",
      "valueRatio",
      "valueSampledData",
      "valueString",
      "valueTime"
    },
    message = "Only one value value may be specified"
  )
})
public class Observation implements Resource {
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

  @Valid List<Identifier> identifier;

  @Valid
  @NotBlank
  @Pattern(regexp = Fhir.CODE)
  Status status;

  @Valid @NotBlank CodeableConcept category;
  @Valid @NotBlank CodeableConcept code;
  @Valid @NotBlank Reference subject;
  @Valid Reference encounter;

  @Valid Period effectivePeriod;

  @Pattern(regexp = Fhir.DATETIME)
  String effectiveDateTime;

  @Pattern(regexp = Fhir.INSTANT)
  String issued;

  @Valid Reference performer;

  @Valid Quantity valueQuantity;
  @Valid CodeableConcept valueCodeableConcept;
  @Valid String valueString;
  @Valid Range valueRange;
  @Valid Ratio valueRation;
  @Valid SampledData valueSampledData;
  @Valid Attachment valueAttachment;

  @Pattern(regexp = Fhir.TIME)
  String valueTime;

  @Pattern(regexp = Fhir.DATETIME)
  String valueDateTime;

  @Valid Period valuePeriod;

  @Valid CodeableConcept dataAbsentReason;
  @Valid CodeableConcept interpretation;
  String comments;
  @Valid CodeableConcept bodySite;
  @Valid CodeableConcept method;

  @Valid Reference specimen;
  @Valid Reference device;

  @Valid List<ObservationReferenceRange> referenceRange;
  @Valid List<ObservationRelated> related;
  @Valid List<ObservationComponent> component;

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class ObservationComponent implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> modifierExtension;
    @Valid List<Extension> extension;

    @Valid @NotBlank CodeableConcept code;

    @Valid Quantity valueQuantity;
    @Valid CodeableConcept valueCodeableConcept;
    @Valid String valueString;
    @Valid Range valueRange;
    @Valid Ratio valueRation;
    @Valid SampledData valueSampledData;
    @Valid Attachment valueAttachment;

    @Pattern(regexp = Fhir.TIME)
    String valueTime;

    @Pattern(regexp = Fhir.DATETIME)
    String valueDateTime;

    @Valid Period valuePeriod;

    @Valid CodeableConcept dataAbsentReason;

    @Valid List<ObservationReferenceRange> referenceRange;
  }

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class ObservationReferenceRange implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> modifierExtension;
    @Valid List<Extension> extension;

    @Valid SimpleQuantity low;
    @Valid SimpleQuantity high;
    @Valid CodeableConcept meaning;
    @Valid Range age;
    String text;
  }

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class ObservationRelated implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> modifierExtension;
    @Valid List<Extension> extension;

    @Pattern(regexp = Fhir.CODE)
    Type type;

    @Valid @NotBlank Reference target;
  }

  public enum Status {
    registered,
    preliminary,
    amended,
    cancelled,
    unknown,
    @JsonProperty("final") _final,
    @JsonProperty("entered-in-error") entered_in_error
  }

  public enum Type {
    @JsonProperty("has-member") has_member,
    @JsonProperty("derived-from") derived_from,
    @JsonProperty("sequel-to") sequel_to,
    replaces,
    @JsonProperty("qualified-by") qualified_by,
    @JsonProperty("interfered-by") interfered_by
  }
}
