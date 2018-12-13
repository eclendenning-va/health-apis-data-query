package gov.va.api.health.argonaut.service.controller.condition;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateString;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;
import static org.apache.commons.lang3.StringUtils.isBlank;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.api.resources.Condition.ClinicalStatusCode;
import gov.va.api.health.argonaut.api.resources.Condition.VerificationStatusCode;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
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

  CodeableConcept category(CdwCategory maybeSource) {
    if (maybeSource == null) {
      return null;
    }
    if (maybeSource.getCoding().isEmpty() && isBlank(maybeSource.getText())) {
      return null;
    }
    return CodeableConcept.builder()
        .coding(categoryCodings(maybeSource.getCoding()))
        .text(maybeSource.getText())
        .build();
  }

  List<Coding> categoryCodings(List<CdwConditionCategoryCoding> source) {
    List<Coding> codings = convertAll(source, this::categoryCoding);
    if (codings == null) {
      return null;
    }
    return codings.isEmpty() ? null : codings;
  }

  private Coding categoryCoding(CdwConditionCategoryCoding cdw) {
    if (cdw == null || allNull(cdw.getCode(), cdw.getDisplay(), cdw.getSystem())) {
      return null;
    }
    return Coding.builder()
        .system(cdw.getSystem())
        .code(cdw.getCode())
        .display(cdw.getDisplay())
        .build();
  }

  ClinicalStatusCode clinicalStatusCode(@NotNull CdwConditionClinicalStatus source) {
    return ifPresent(
        source, status -> EnumSearcher.of(ClinicalStatusCode.class).find(status.value()));
  }

  CodeableConcept code(List<CdwCodeableConcept> maybeCdw) {
    if (maybeCdw == null || maybeCdw.isEmpty()) {
      return null;
    }
    CdwCodeableConcept firstCode = maybeCdw.get(0);
    if (firstCode.getCoding().isEmpty() && isBlank(firstCode.getText())) {
      return null;
    }
    return CodeableConcept.builder()
        .text(firstCode.getText())
        .coding(codeCodings(firstCode.getCoding()))
        .build();
  }

  List<Coding> codeCodings(List<CdwCoding> source) {
    List<Coding> codings = convertAll(source, this::codeCoding);
    if (codings == null) {
      return null;
    }
    return codings.isEmpty() ? null : codings;
  }

  private Coding codeCoding(CdwCoding cdw) {
    if (cdw == null || allNull(cdw.getCode(), cdw.getDisplay(), cdw.getSystem())) {
      return null;
    }
    return Coding.builder()
        .system(cdw.getSystem())
        .code(cdw.getCode())
        .display(cdw.getDisplay())
        .build();
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
    if (maybeCdw == null || allNull(maybeCdw.getDisplay(), maybeCdw.getReference())) {
      return null;
    }
    return convert(
        maybeCdw,
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }

  VerificationStatusCode verificationStatusCode(@NotNull CdwConditionVerificationStatus source) {
    return ifPresent(
        source, status -> EnumSearcher.of(VerificationStatusCode.class).find(status.value()));
  }
}
