package gov.va.api.health.argonaut.service.controller.medicationorder;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateString;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;


import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.Duration;
import gov.va.api.health.argonaut.api.datatypes.SimpleQuantity;
import gov.va.api.health.argonaut.api.datatypes.Timing;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.DispenseRequest;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.DosageInstruction;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.Status;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwDuration;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDispenseRequest;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction.CdwRoute;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction.CdwTiming;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import gov.va.dvp.cdw.xsd.model.CdwSimpleQuantity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MedicationOrderTransformer implements MedicationOrderController.Transformer {
  public MedicationOrder apply(CdwMedicationOrder source) {
    return medicationOrder(source);
  }

  public MedicationOrder medicationOrder(CdwMedicationOrder source) {
    return MedicationOrder.builder()
        .id(source.getCdwId())
        .resourceType("Medication Order")
        .patient(reference(source.getPatient()))
        .dateWritten(asDateTimeString(source.getDateWritten()))
        /*.status(
            ifPresent(
                source.getStatus(),
                status -> EnumSearcher.of(MedicationOrder.Status.class).find(status.value())))*/
        .dateEnded(asDateString(source.getDateEnded()))
        .prescriber(reference(source.getPrescriber()))
        .medicationReference(reference(source.getMedicationReference()))
        .dosageInstruction(dosageInstruction(source.getDosageInstructions()))
        .dispenseRequest(dispenseRequest(source.getDispenseRequest()))
        .build();
  }

  public CodeableConcept additionalInstructions (CdwCodeableConcept source) {
    return CodeableConcept.builder()
        .text(source.getText())
        .build();
  }

  public List<DosageInstruction> dosageInstruction(CdwDosageInstructions source) {
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

  public SimpleQuantity doseQuantity(CdwSimpleQuantity source) {
    return SimpleQuantity.builder()
        .value(Double.valueOf(source.getValue()))
        .build();
  }

  public DispenseRequest dispenseRequest(CdwDispenseRequest source) {
    return DispenseRequest.builder()
        .numberOfRepeatsAllowed(source.getNumberOfRepeatsAllowed())
        .quantity(quantity(source.getQuantity()))
        .expectedSupplyDuration(expectedSupplyDuration(source.getExpectedSupplyDuration()))
        .build();
  }

  public Duration expectedSupplyDuration(CdwDuration source) {
    return Duration.builder()
        .value(Double.valueOf(source.getValue()))
        .unit(source.getUnit())
        .system(source.getSystem())
        .code(source.getCode())
        .build();
  }

  public SimpleQuantity quantity (String source) {
    return SimpleQuantity.builder()
        .value(Double.valueOf(source))
        .build();
  }

  public Reference reference(CdwReference maybeSource) {
    return convert(
        maybeSource,
        source ->
            Reference.builder()
                .display(source.getDisplay())
                .reference(source.getReference())
                .build());
  }

  public CodeableConcept route(CdwRoute source) {
    return CodeableConcept.builder()
        .text(source.getText())
        .build();
  }


  public Timing timing(CdwTiming source) {
    return Timing.builder()
        .code(timingCode(source.getCode()))
        .build();
  }

  public CodeableConcept timingCode(CdwCodeableConcept source) {
    return CodeableConcept.builder()
        .text(source.getText())
        .build();
  }
}
