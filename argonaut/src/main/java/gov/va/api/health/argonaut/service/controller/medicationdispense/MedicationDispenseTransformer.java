package gov.va.api.health.argonaut.service.controller.medicationdispense;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.SimpleQuantity;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.MedicationDispense;
import gov.va.api.health.argonaut.api.resources.MedicationDispense.Status;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense.CdwAuthorizingPrescriptions;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispenseStatus;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispenseType;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispenseTypeCoding;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import gov.va.dvp.cdw.xsd.model.CdwSimpleQuantity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
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

  //    List<Reference> authorizingPrescriptions(List<CdwAuthorizingPrescriptions> maybeCdw) {
  //        return convertAll(ifPresent(maybeCdw,
  // CdwAuthorizingPrescriptions::getAuthorizingPrescription), this::reference);
  //    }
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

  private boolean isUsable(CdwReference reference) {
    return reference != null && !allNull(reference.getReference(), reference.getDisplay());
  }
}
