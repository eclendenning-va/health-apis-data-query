package gov.va.api.health.argonaut.service.controller.medicationdispense;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.SimpleQuantity;
import gov.va.api.health.argonaut.api.datatypes.Timing;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.MedicationDispense;
import gov.va.api.health.argonaut.api.resources.MedicationDispense.DosageInstruction;
import gov.va.api.health.argonaut.api.resources.MedicationDispense.Status;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense.CdwDosageInstructions;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense.CdwDosageInstructions.CdwDosageInstruction.CdwRoute;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense.CdwDosageInstructions.CdwDosageInstruction;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense.CdwDosageInstructions.CdwDosageInstruction.CdwTiming;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispenseStatus;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispenseType;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispenseTypeCoding;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import gov.va.dvp.cdw.xsd.model.CdwSimpleQuantity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class MedicationDispenseTransformer implements MedicationDispenseController.Transformer {

  @Override
  public MedicationDispense apply(CdwMedicationDispense cdw) {

    return MedicationDispense.builder()
        .resourceType("MedicationDispense")
        .id(cdw.getCdwId())
        .status(status(cdw.getStatus()))
        .patient(reference(cdw.getPatient()))
        .dispenser(reference(cdw.getDispenser()))
        .type(typeCodeableConcept(cdw.getType()))
        .quantity(simpleQuantity(cdw.getQuantity()))
        .daysSupply(simpleQuantity(cdw.getDaysSupply()))
        .medicationReference(reference(cdw.getMedicationReference()))
        .whenPrepared(asDateTimeString(cdw.getWhenPrepared()))
        .whenHandedOver(asDateTimeString(cdw.getWhenHandedOver()))
        .note(cdw.getNote())
        .build();
  }

  Status status(CdwMedicationDispenseStatus source) {
    return EnumSearcher.of(MedicationDispense.Status.class).find(source.value());
  }

  Reference reference(CdwReference maybeSource) {
    if (!isUsable(maybeSource)) {
      return null;
    }
    return convert(
        maybeSource,
        source ->
            Reference.builder()
                .display(source.getDisplay())
                .reference(source.getReference())
                .build());
  }

  /*    List<Reference> authorizingPrescriptions(List<CdwAuthorizingPrescriptions> maybeCdw) {
         return convertAll(ifPresent(maybeCdw,
  CdwAuthorizingPrescriptions::getAuthorizingPrescription), this::reference);
     }*/
  CodeableConcept typeCodeableConcept(CdwMedicationDispenseType maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    if (maybeCdw.getCoding() == null && isBlank(maybeCdw.getText())) {
      return null;
    }
    return CodeableConcept.builder()
        .text(maybeCdw.getText())
        .coding(Collections.singletonList(typeCoding(maybeCdw.getCoding())))
        .build();
  }

  Coding typeCoding(CdwMedicationDispenseTypeCoding source) {
    if (source == null || allNull(source.getCode(), source.getDisplay(), source.getSystem())) {
      return null;
    }
    return Coding.builder()
        .code(source.getCode().value())
        .display(source.getDisplay().value())
        .system(source.getSystem())
        .build();
  }

  /*simpleQuantity and quantityValue might be useful to take out to Transformers? This same pattern is in Observation*/
  SimpleQuantity simpleQuantity(CdwSimpleQuantity source) {
    if (source == null
        || allNull(source.getCode(), source.getSystem(), source.getUnit(), source.getValue())) {
      return null;
    }
    return SimpleQuantity.builder()
        .value(quantityValue(source.getValue()))
        .unit(source.getUnit())
        .code(source.getCode())
        .system(source.getSystem())
        .build();
  }

  Double quantityValue(String source) {
    Double value;
    if (source == null || isBlank(source)) {
      return null;
    }
    try {
      value = Double.valueOf(source);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Cannot create double value from " + source, e);
    }
    return value;
  }

  List<DosageInstruction> dosageInstructions(CdwDosageInstructions cdw) {
    if (cdw == null || cdw.getDosageInstruction().isEmpty()) {
      return null;
    }
    return convertAll(
        ifPresent(cdw, CdwDosageInstructions::getDosageInstruction), this::dosageInstruction);
  }

  CodeableConcept additionalInstructions(CdwCodeableConcept source) {
    if (source == null) {
      return null;
    }
    if (source.getCoding().isEmpty() && isBlank(source.getText())) {
      return null;
    }
    return CodeableConcept.builder()
        .coding(additionalInstructionsCodings(source.getCoding()))
        .text(source.getText())
        .build();
  }

  List<Coding> additionalInstructionsCodings(List<CdwCoding> source) {
    List<Coding> codings = convertAll(source, this::additionalInstructionsCoding);
    return codings == null || codings.isEmpty() ? null : codings;
  }

  private Coding additionalInstructionsCoding(CdwCoding cdw) {
    if (cdw == null || allNull(cdw.getCode(), cdw.getDisplay(), cdw.getSystem())) {
      return null;
    }
    return Coding.builder()
        .system(cdw.getSystem())
        .code(cdw.getCode())
        .display(cdw.getDisplay())
        .build();
  }

  Timing timing(CdwTiming maybeCdw) {
    if (maybeCdw == null || allNull(maybeCdw.getCode())) {
      return null;
    }
    return Timing.builder().code(timingCodeableConcept(maybeCdw.getCode())).build();
  }

  /**
   * Not a fan of having multiple methods for building codeable concepts just because of these one
   * off cdw versions. Maybe make more generic so this isn't like this?
   */
  CodeableConcept timingCodeableConcept(CdwCodeableConcept maybeCdw) {
    if (maybeCdw == null || allNull(maybeCdw.getCoding(), maybeCdw.getText())) {
      return null;
    }
    return CodeableConcept.builder()
        .text(maybeCdw.getText())
        .coding(timingCodings(maybeCdw.getCoding()))
        .build();
  }

  List<Coding> timingCodings(List<CdwCoding> source) {
    List<Coding> codings = convertAll(source, this::timingCoding);
    return codings == null || codings.isEmpty() ? null : codings;
  }

  private Coding timingCoding(CdwCoding cdw) {
    if (cdw == null || allNull(cdw.getCode(), cdw.getDisplay(), cdw.getSystem())) {
      return null;
    }
    return Coding.builder()
        .system(cdw.getSystem())
        .code(cdw.getCode())
        .display(cdw.getDisplay())
        .build();
  }

  CodeableConcept routeCodeableConcept(CdwRoute maybeCdw) {
    return null;
  }

  DosageInstruction dosageInstruction(CdwDosageInstruction cdw) {
    if (cdw == null
        || allNull(
            cdw.getAdditionalInstructions(),
            cdw.isAsNeededBoolean(),
            cdw.getDoseQuantity(),
            cdw.getRoute(),
            cdw.getText(),
            cdw.getTiming())) {
      return null;
    }
    return convert(
        cdw,
        source ->
            DosageInstruction.builder()
                .text(source.getText())
                .additionalInstructions(additionalInstructions(source.getAdditionalInstructions()))
                .doseQuantity(simpleQuantity(source.getDoseQuantity()))
                .timing(timing(source.getTiming()))
                .asNeededBoolean(Boolean.valueOf(source.isAsNeededBoolean()))
                .route(routeCodeableConcept(source.getRoute()))
                .build());
  }

  private boolean isUsable(CdwReference reference) {
    return reference != null && !allNull(reference.getReference(), reference.getDisplay());
  }
}
