package gov.va.api.health.argonaut.service.controller.medicationorder;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;

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
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction.CdwRoute;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction.CdwTiming;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import gov.va.dvp.cdw.xsd.model.CdwSimpleQuantity;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.datatype.XMLGregorianCalendar;
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
        .dateWritten(dateTimeString(source.getDateWritten()))
        .status(
            convert(
                source.getStatus(),
                status -> EnumSearcher.of(MedicationOrder.Status.class).find(source.getStatus())))
        .dateEnded(dateTimeString(source.getDateEnded()))
        .prescriber(reference(source.getPrescriber()))
        .medicationReference(reference(source.getMedicationReference()))
        .dosageInstruction(dosageInstructions(source.getDosageInstructions()))
        .dispenseRequest(dispenseRequest(source.getDispenseRequest()))
        .build();
  }

  CodeableConcept additionalInstructions(CdwCodeableConcept source) {
    if (source == null) {
      return null;
    }
    return CodeableConcept.builder().text(source.getText()).build();
  }

  String dateTimeString(XMLGregorianCalendar source) {
    if (source == null) {
      return null;
    }
    return asDateTimeString(source);
  }

  DispenseRequest dispenseRequest(CdwDispenseRequest source) {
    if (source == null
        || allNull(
            source.getNumberOfRepeatsAllowed(),
            source.getQuantity(),
            source.getExpectedSupplyDuration())) {
      return null;
    }
    return DispenseRequest.builder()
        .numberOfRepeatsAllowed(numberOfRepeatsAllowed(source.getNumberOfRepeatsAllowed()))
        .quantity(quantity(source.getQuantity()))
        .expectedSupplyDuration(expectedSupplyDuration(source.getExpectedSupplyDuration()))
        .build();
  }

  Integer numberOfRepeatsAllowed(Integer source) {
    if (source == null || source <= 0) {
      return null;
    }
    return source;
  }

  List<DosageInstruction> dosageInstructions(CdwDosageInstructions source) {
    if (source == null || source.getDosageInstruction() == null) {
      return null;
    }
    List<DosageInstruction> dosageInstructions =
        source
            .getDosageInstruction()
            .stream()
            .map(this::dosageInstruction)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    if (dosageInstructions.isEmpty()) {
      return null;
    }
    return dosageInstructions;
  }

  DosageInstruction dosageInstruction(CdwDosageInstruction source) {
    if (source == null
        || allNull(
            source.getAdditionalInstructions(),
            source.getAsNeededBoolean(),
            source.getDoseQuantity(),
            source.getRoute(),
            source.getText(),
            source.getTiming())) {
      return null;
    }
    return DosageInstruction.builder()
        .text(source.getText())
        .additionalInstructions(additionalInstructions(source.getAdditionalInstructions()))
        .timing(timing(source.getTiming()))
        .asNeededBoolean(Boolean.valueOf(source.getAsNeededBoolean()))
        .route(route(source.getRoute()))
        .doseQuantity(doseQuantity(source.getDoseQuantity()))
        .build();
  }

  SimpleQuantity doseQuantity(CdwSimpleQuantity source) {
    if (source == null || source.getValue() == null) {
      return null;
    }
    return SimpleQuantity.builder().value(doseQuantityValue(source.getValue())).build();
  }

  Double doseQuantityValue(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    return Double.valueOf(source);
  }

  Duration expectedSupplyDuration(CdwDuration source) {
    if (source == null
        || allNull(source.getValue(), source.getCode(), source.getSystem(), source.getUnit())) {
      return null;
    }
    return Duration.builder()
        .value(Double.valueOf(source.getValue()))
        .unit(source.getUnit())
        .system(source.getSystem())
        .code(source.getCode())
        .build();
  }

  SimpleQuantity quantity(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    return SimpleQuantity.builder().value(Double.valueOf(source)).build();
  }

  Reference reference(CdwReference maybeSource) {
    if (maybeSource == null || allNull(maybeSource.getDisplay(), maybeSource.getReference())) {
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

  CodeableConcept route(CdwRoute source) {
    if (source == null || source.getText() == null) {
      return null;
    }
    return CodeableConcept.builder().text(source.getText()).build();
  }

  Timing timing(CdwTiming source) {
    if (source == null || source.getCode() == null) {
      return null;
    }
    return Timing.builder().code(timingCode(source.getCode())).build();
  }

  CodeableConcept timingCode(CdwCodeableConcept source) {
    if (source == null) {
      return null;
    }
    return CodeableConcept.builder().text(source.getText()).build();
  }
}
