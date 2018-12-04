package gov.va.api.health.argonaut.service.controller.medicationorder;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Duration;
import gov.va.api.health.argonaut.api.datatypes.SimpleQuantity;
import gov.va.api.health.argonaut.api.datatypes.Timing;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.DispenseRequest;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.DosageInstruction;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwDuration;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDispenseRequest;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction.CdwRoute;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction.CdwTiming;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import gov.va.dvp.cdw.xsd.model.CdwSimpleQuantity;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MedicationOrderTransformer implements MedicationOrderController.Transformer {
  @Override
  public MedicationOrder apply(CdwMedicationOrder source) {
    return medicationOrder(source);
  }

  MedicationOrder medicationOrder(CdwMedicationOrder source) {
    return MedicationOrder.builder()
        .id(source.getCdwId())
        .resourceType("Medication Order")
        .patient(reference(source.getPatient()))
        .dateWritten(asDateTimeString(source.getDateWritten()))
        .status(
            convert(
                source.getStatus(),
                status -> EnumSearcher.of(MedicationOrder.Status.class).find(source.getStatus())))
        .dateEnded(asDateTimeString(source.getDateEnded()))
        .prescriber(reference(source.getPrescriber()))
        .medicationReference(reference(source.getMedicationReference()))
        .dosageInstruction(dosageInstruction(source.getDosageInstructions()))
        .dispenseRequest(dispenseRequest(source.getDispenseRequest()))
        .build();
  }

  CodeableConcept additionalInstructions(CdwCodeableConcept source) {
    if (source == null) {
      return null;
    }
    return CodeableConcept.builder().text(source.getText()).build();
  }

  List<DosageInstruction> dosageInstruction(CdwDosageInstructions source) {
    if (source == null || source.getDosageInstruction().isEmpty()) {
      return null;
    }
    return convertAll(
        ifPresent(source, CdwDosageInstructions::getDosageInstruction),
        cdw ->
            DosageInstruction.builder()
                .text(cdw.getText())
                .additionalInstructions(additionalInstructions(cdw.getAdditionalInstructions()))
                .timing(timing(cdw.getTiming()))
                .asNeededBoolean(Boolean.valueOf(cdw.getAsNeededBoolean()))
                .route(route(cdw.getRoute()))
                .doseQuantity(doseQuantity(cdw.getDoseQuantity()))
                .build());
  }

  SimpleQuantity doseQuantity(CdwSimpleQuantity source) {
    return SimpleQuantity.builder()
        .code(source.getCode())
        .unit(source.getUnit())
        .system(source.getSystem())
        .value(Double.valueOf(source.getValue()))
        .build();
  }

  DispenseRequest dispenseRequest(CdwDispenseRequest source) {
    if (source == null) {
      return null;
    }
    return DispenseRequest.builder()
        .numberOfRepeatsAllowed(source.getNumberOfRepeatsAllowed())
        .quantity(quantity(source.getQuantity()))
        .expectedSupplyDuration(expectedSupplyDuration(source.getExpectedSupplyDuration()))
        .build();
  }

  Duration expectedSupplyDuration(CdwDuration source) {
    return Duration.builder()
        .value(Double.valueOf(source.getValue()))
        .unit(source.getUnit())
        .system(source.getSystem())
        .code(source.getCode())
        .build();
  }

  SimpleQuantity quantity(String source) {
    return SimpleQuantity.builder().value(Double.valueOf(source)).build();
  }

  Reference reference(CdwReference maybeSource) {
    return convert(
        maybeSource,
        source ->
            Reference.builder()
                .display(source.getDisplay())
                .reference(source.getReference())
                .build());
  }

  CodeableConcept route(CdwRoute source) {
    return CodeableConcept.builder().text(source.getText()).build();
  }

  Timing timing(CdwTiming source) {
    return Timing.builder().code(timingCode(source.getCode())).build();
  }

  CodeableConcept timingCode(CdwCodeableConcept source) {
    if (source == null) {
      return null;
    }
    return CodeableConcept.builder().text(source.getText()).build();
  }
}
