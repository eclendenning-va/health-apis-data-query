package gov.va.api.health.argonaut.api.resources;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import gov.va.api.health.argonaut.api.Fhir;
import gov.va.api.health.argonaut.api.datatypes.*;
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
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
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
    //Does this need to be an explicit enum?
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
        //Picked ratio because the xsd contract did not specify, is this okay?
        @Valid Ratio rateRatio;
        @Valid Ratio maxDosePerPeriod;
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
