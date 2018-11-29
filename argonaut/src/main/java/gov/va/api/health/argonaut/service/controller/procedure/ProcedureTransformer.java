package gov.va.api.health.argonaut.service.controller.procedure;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Procedure;
import gov.va.api.health.argonaut.api.resources.Procedure.Status;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwCodeSystem;
import gov.va.dvp.cdw.xsd.model.CdwProcedure101Root.CdwProcedures.CdwProcedure;
import gov.va.dvp.cdw.xsd.model.CdwProcedure101Root.CdwProcedures.CdwProcedure.CdwCode;
import gov.va.dvp.cdw.xsd.model.CdwProcedure101Root.CdwProcedures.CdwProcedure.CdwReasonNotPerformed;
import gov.va.dvp.cdw.xsd.model.CdwProcedureStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProcedureTransformer implements ProcedureController.Transformer {

  @Override
  public Procedure apply(CdwProcedure cdw) {
    return Procedure.builder()
        .resourceType("Procedure")
        .id(cdw.getCdwId())
        .subject(reference(cdw.getSubject()))
        .status(status(cdw.getStatus()))
        .code(code(cdw.getCode()))
        .notPerformed(cdw.isNotPerformed())
        .reasonNotPerformed(reasonNotPerformed(cdw.getReasonNotPerformed()))
        .performedDateTime(asDateTimeString(cdw.getPerformedDateTime()))
        .encounter(reference(cdw.getEncounter()))
        .location(reference(cdw.getLocation()))
        .build();
  }

  List<CodeableConcept> reasonNotPerformed(CdwReasonNotPerformed maybeReason) {
    return Collections.singletonList(CodeableConcept.builder().text(maybeReason.getText()).build());
  }

  CodeableConcept code(CdwCode maybeCode) {
    return convert(
        maybeCode,
        source ->
            CodeableConcept.builder()
                .coding(
                    convertAll(
                        source.getCoding(),
                        codeSource ->
                            Coding.builder()
                                .system(convert(codeSource.getSystem(), CdwCodeSystem::value))
                                .code(codeSource.getCode())
                                .display(codeSource.getDisplay())
                                .build()))
                .build());
  }

  Status status(CdwProcedureStatus maybeStatus) {
    if (maybeStatus == null) {
      return null;
    }
    return EnumSearcher.of(Procedure.Status.class).find(maybeStatus.value());
  }

  Reference reference(CdwReference maybeReference) {
    return convert(
        maybeReference,
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }
}
