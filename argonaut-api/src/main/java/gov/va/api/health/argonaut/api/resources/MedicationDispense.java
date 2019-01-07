package gov.va.api.health.argonaut.api.resources;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gov.va.api.health.argonaut.api.Fhir;
import gov.va.api.health.argonaut.api.bundle.AbstractBundle;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry;
import gov.va.api.health.argonaut.api.bundle.BundleLink;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Identifier;
import gov.va.api.health.argonaut.api.datatypes.Ratio;
import gov.va.api.health.argonaut.api.datatypes.Signature;
import gov.va.api.health.argonaut.api.datatypes.SimpleQuantity;
import gov.va.api.health.argonaut.api.datatypes.SimpleResource;
import gov.va.api.health.argonaut.api.datatypes.Timing;
import gov.va.api.health.argonaut.api.elements.BackboneElement;
import gov.va.api.health.argonaut.api.elements.Extension;
import gov.va.api.health.argonaut.api.elements.Meta;
import gov.va.api.health.argonaut.api.elements.Narrative;
import gov.va.api.health.argonaut.api.elements.Reference;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@Schema(description = "https://www.hl7.org/fhir/DSTU2/medicationdispense.html")
public class MedicationDispense implements DomainResource {

  @NotBlank String resourceType;

  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid Meta meta;

  @Pattern(regexp = Fhir.URI)
  String implicitRules;

  @Pattern(regexp = Fhir.CODE)
  String language;

  @Valid List<SimpleResource> contained;
  @Valid List<Extension> extension;
  @Valid List<Extension> modifierExtension;
  @Valid List<Identifier> identifier;
  @Valid Narrative text;
  @NotNull Status status;
  @Valid Reference patient;
  @Valid Reference dispenser;
  @Valid List<Reference> authorizingPrescription;
  @Valid CodeableConcept type;
  @Valid SimpleQuantity quantity;
  @Valid SimpleQuantity daysSupply;
  @NotNull @Valid Reference medicationReference;

  @Pattern(regexp = Fhir.DATETIME)
  String whenPrepared;

  @Pattern(regexp = Fhir.DATETIME)
  String whenHandedOver;

  @Valid Reference destination;
  @Valid List<Reference> receiver;
  String note;
  @Valid List<DosageInstruction> dosageInstruction;
  @Valid Substitution substitution;

  /**
   * Medication dispense must be prepared before being handed over. This constraint verifies that if
   * both are fields are set, the handed over time is chronologically after the prepared time. See
   * Constraint mdd-1: whenHandedOver cannot be before whenPrepared.
   */
  @JsonIgnore
  @AssertTrue(message = "whenPrepared must be chronologically before whenHandedOver.")
  private boolean isPreparedBeforeHandedOver() {
    if (whenPrepared == null || whenHandedOver == null) {
      return true;
    }
    /*
     * This catch is to avoid having redundant validation errors thrown We'd like the Pattern regex
     * to be the only one thrown instead of this one with a more generic message.
     */
    try {
      Instant prepared = Fhir.parseDateTime(whenPrepared);
      Instant handedOver = Fhir.parseDateTime(whenHandedOver);
      return !prepared.isAfter(handedOver);
    } catch (IllegalArgumentException e) {
      /*
       * We were unable to understand at least one of the dates. We're going to say this is OK since
       * the regex parser should fail.
       */
      return true;
    }
  }

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

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @JsonDeserialize(builder = MedicationDispense.Bundle.BundleBuilder.class)
  @Schema(name = "MedicationDispenseBundle")
  public static class Bundle extends AbstractBundle<Entry> {

    @Builder
    public Bundle(
        @NotBlank String resourceType,
        @Pattern(regexp = Fhir.ID) String id,
        @Valid Meta meta,
        @Pattern(regexp = Fhir.URI) String implicitRules,
        @Pattern(regexp = Fhir.CODE) String language,
        @NotNull BundleType type,
        @Min(0) Integer total,
        @Valid List<BundleLink> link,
        @Valid List<MedicationDispense.Entry> entry,
        @Valid Signature signature) {
      super(resourceType, id, meta, implicitRules, language, type, total, link, entry, signature);
    }
  }

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @Schema(name = "MedicationDispenseDosageInstruction")
  public static class DosageInstruction implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> modifierExtension;
    @Valid List<Extension> extension;
    String text;
    @Valid CodeableConcept additionalInstructions;
    @Valid Timing timing;
    boolean asNeededBoolean;
    @Valid CodeableConcept siteCodeableConcept;
    @Valid CodeableConcept route;
    @Valid CodeableConcept method;
    @Valid SimpleQuantity doseQuantity;
    // Picked ratio because the xsd contract did not specify, is this okay?
    @Valid Ratio rateRatio;
    @Valid Ratio maxDosePerPeriod;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @JsonDeserialize(builder = MedicationDispense.Entry.EntryBuilder.class)
  @Schema(name = "MedicationDispenseEntry")
  public static class Entry extends AbstractEntry<MedicationDispense> {

    @Builder
    public Entry(
        @Pattern(regexp = Fhir.ID) String id,
        @Valid List<Extension> extension,
        @Valid List<Extension> modifierExtension,
        @Valid List<BundleLink> link,
        @Pattern(regexp = Fhir.URI) String fullUrl,
        @Valid MedicationDispense resource,
        @Valid Search search,
        @Valid Request request,
        @Valid Response response) {
      super(id, extension, modifierExtension, link, fullUrl, resource, search, request, response);
    }
  }

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @Schema(name = "MedicationDispenseSubstitution")
  public static class Substitution implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> modifierExtension;
    @Valid List<Extension> extension;
    @NotNull @Valid CodeableConcept type;
    @Valid List<CodeableConcept> reason;
    @Valid List<Reference> responsibleParty;
  }
}
