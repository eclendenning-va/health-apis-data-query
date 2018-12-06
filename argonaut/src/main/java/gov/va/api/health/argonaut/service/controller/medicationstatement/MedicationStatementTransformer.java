package gov.va.api.health.argonaut.service.controller.medicationstatement;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.Timing;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.MedicationStatement;
import gov.va.api.health.argonaut.api.resources.MedicationStatement.Dosage;
import gov.va.api.health.argonaut.api.resources.MedicationStatement.Status;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwMedicationStatement102Root.CdwMedicationStatements.CdwMedicationStatement;
import gov.va.dvp.cdw.xsd.model.CdwMedicationStatement102Root.CdwMedicationStatements.CdwMedicationStatement.CdwDosages;
import gov.va.dvp.cdw.xsd.model.CdwMedicationStatement102Root.CdwMedicationStatements.CdwMedicationStatement.CdwDosages.CdwDosage.CdwTiming;
import gov.va.dvp.cdw.xsd.model.CdwMedicationStatementStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class MedicationStatementTransformer implements MedicationStatementController.Transformer {

  @Override
  public MedicationStatement apply(CdwMedicationStatement cdw) {
    /*
     * Specimen reference is omitted since we do not support the a specimen resource and
     * do not want dead links
     */
    return MedicationStatement.builder()
        .resourceType("Medication Statement")
        .id(cdw.getCdwId())
        .dateAsserted(asDateTimeString(cdw.getDateAsserted()))
        .dosage(dosage(cdw.getDosages()))
        .effectiveDateTime(asDateTimeString(cdw.getEffectiveDateTime()))
        .medicationReference(reference(cdw.getMedicationReference()))
        .note(cdw.getNote())
        .patient(reference(cdw.getPatient()))
        .status(status(cdw.getStatus()))
        .build();
  }

  Status status(@NotNull CdwMedicationStatementStatus cdw) {
    return EnumSearcher.of(Status.class).find(cdw.value());
  }

  List<Dosage> dosage(CdwMedicationStatement.CdwDosages maybeCdw) {
    return convertAll(
        ifPresent(maybeCdw, CdwDosages::getDosage),
        source ->
            Dosage.builder()
                .route(codeableConcept(source.getRoute()))
                .text(source.getText())
                .timing(timing(source.getTiming()))
                .build());
  }

  CodeableConcept codeableConcept(CdwCodeableConcept maybeSource) {
    return convert(
        maybeSource,
        source ->
            CodeableConcept.builder()
                .text(source.getText())
                .coding(codings(source.getCoding()))
                .build());
  }

  Timing timing(CdwTiming maybeSource) {
    return convert(
        maybeSource, source -> Timing.builder().code(codeableConcept(source.getCode())).build());
  }

  List<Coding> codings(List<CdwCoding> source) {
    return convertAll(
        source,
        item ->
            Coding.builder()
                .system(item.getSystem())
                .code(item.getCode())
                .display(item.getDisplay())
                .build());
  }

  Reference reference(CdwReference maybeCdw) {
    return convert(
        maybeCdw,
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }
}
