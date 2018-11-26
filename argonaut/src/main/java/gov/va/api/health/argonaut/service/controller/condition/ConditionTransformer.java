package gov.va.api.health.argonaut.service.controller.condition;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateString;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.api.resources.Condition.ClinicalStatusCode;
import gov.va.api.health.argonaut.api.resources.Condition.VerificationStatusCode;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCondition103Root.CdwConditions.CdwCondition;
import gov.va.dvp.cdw.xsd.model.CdwCondition103Root.CdwConditions.CdwCondition.CdwCategory;
import gov.va.dvp.cdw.xsd.model.CdwConditionCategoryCoding;
import gov.va.dvp.cdw.xsd.model.CdwConditionClinicalStatus;
import gov.va.dvp.cdw.xsd.model.CdwConditionVerificationStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ConditionTransformer implements ConditionController.Transformer {

  @Override
  public Condition apply(CdwCondition condition) {
    return condition(condition);
  }

  private Condition condition(CdwCondition source) {
    return Condition.builder()
        .resourceType("Condition")
        .abatementDateTime(asDateTimeString(source.getAbatementDateTime()))
        .asserter(reference(source.getAsserter()))
        .category(category(source.getCategory()))
        .id(source.getCdwId())
        .clinicalStatus(clinicalStatusCode(source.getClinicalStatus()))
        .code(code(source.getCode()))
        .dateRecorded(asDateString(source.getDateRecorded()))
        .encounter(reference(source.getEncounter()))
        .onsetDateTime(asDateTimeString(source.getOnsetDateTime()))
        .patient(reference(source.getPatient()))
        .verificationStatus(verificationStatusCode(source.getVerificationStatus()))
        .build();
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

  CodeableConcept code(List<CdwCodeableConcept> maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    if (maybeCdw.get(0).getText() == null && maybeCdw.get(0).getCoding().isEmpty()) {
      return null;
    }
    return CodeableConcept.builder()
        .text(maybeCdw.get(0).getText())
        .coding(
            convertAll(
                maybeCdw.get(0).getCoding(),
                source -> coding(source.getSystem(), source.getCode(), source.getDisplay())))
        .build();
  }

  private Coding coding(String system, String code, String display) {
    return Coding.builder().system(system).code(code).display(display).build();
  }

  CodeableConcept category(CdwCategory maybeSource) {
    if (maybeSource == null || maybeSource.getCoding().isEmpty()) {
      return null;
    }
    return CodeableConcept.builder()
        .coding(categoryCodings(maybeSource.getCoding()))
        .text(maybeSource.getText())
        .build();
  }

  ClinicalStatusCode clinicalStatusCode(@NotNull CdwConditionClinicalStatus source) {
    EnumSearcher<ClinicalStatusCode> e = EnumSearcher.of(ClinicalStatusCode.class);
    return ifPresent(source, status -> e.find(status.value()));
  }

  VerificationStatusCode verificationStatusCode(@NotNull CdwConditionVerificationStatus source) {
    EnumSearcher<VerificationStatusCode> e = EnumSearcher.of(VerificationStatusCode.class);
    return ifPresent(source, status -> e.find(status.value()));
  }

  List<Coding> categoryCodings(List<CdwConditionCategoryCoding> maybeSource) {
    if (maybeSource == null) {
      return null;
    }
    return convertAll(
        maybeSource,
        cdw ->
            Coding.builder()
                .system(cdw.getSystem())
                .code(cdw.getCode())
                .display(cdw.getDisplay())
                .build());
  }
}
