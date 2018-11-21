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
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ConditionTransformer implements ConditionController.Transformer {

  @Override
  public Condition apply(CdwCondition condition) {
    return condition(condition);
  }

  private Condition condition(CdwCondition source) {
    return Condition.builder()
        .abatementDateTime(asDateTimeString(source.getAbatementDateTime()))
        .asserter(reference(source.getAsserter()))
        .category(category(source.getCategory()))
        .id(source.getCdwId())
        .clinicalStatus(clinicalStatusCode(source.getClinicalStatus()))
        .code(code(source.getCode().get(0)))
        .dateRecorded(asDateString(source.getDateRecorded()))
        .encounter(reference(source.getEncounter()))
        .onsetDateTime(asDateTimeString(source.getOnsetDateTime()))
        .patient(reference(source.getPatient()))
        .verificationStatus(verificationStatusCode(source.getVerificationStatus()))
        .build();
  }

  Reference reference(gov.va.dvp.cdw.xsd.model.CdwReference source) {
    return convert(
        source,
        cdw ->
            Reference.builder()
                .display(source.getDisplay())
                .reference(source.getReference())
                .build());
  }

  CodeableConcept code(CdwCodeableConcept source) {
    return convert(
        source,
        cdw ->
            CodeableConcept.builder().text(cdw.getText()).coding(coding(cdw.getCoding())).build());
  }

  List<Coding> coding(List<gov.va.dvp.cdw.xsd.model.CdwCoding> source) {
    return convertAll(
        source,
        cdw ->
            Coding.builder()
                .code(cdw.getCode())
                .system(cdw.getSystem())
                .display(cdw.getDisplay())
                .build());
  }

  CodeableConcept category(CdwCategory source) {
    return CodeableConcept.builder()
        .coding(categoryCodings(source.getCoding()))
        .text(source.getText())
        .build();
  }

  ClinicalStatusCode clinicalStatusCode(CdwConditionClinicalStatus source) {
    EnumSearcher<ClinicalStatusCode> e = EnumSearcher.of(ClinicalStatusCode.class).build();
    return ifPresent(source, status -> e.find(status.value()));
  }

  VerificationStatusCode verificationStatusCode(CdwConditionVerificationStatus source) {
    EnumSearcher<VerificationStatusCode> e = EnumSearcher.of(VerificationStatusCode.class).build();
    return ifPresent(source, status -> e.find(status.value()));
  }

  List<Coding> categoryCodings(List<CdwConditionCategoryCoding> optionalSource) {
    return convertAll(
        optionalSource,
        cdw ->
            Coding.builder()
                .system(cdw.getSystem())
                .code(cdw.getCode())
                .display(cdw.getDisplay())
                .build());
  }
}
