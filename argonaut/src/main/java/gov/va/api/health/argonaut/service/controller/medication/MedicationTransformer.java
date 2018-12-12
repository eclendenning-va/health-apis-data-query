package gov.va.api.health.argonaut.service.controller.medication;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Narrative;
import gov.va.api.health.argonaut.api.elements.Narrative.NarrativeStatus;
import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.api.resources.Medication.Product;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication.CdwProduct;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MedicationTransformer implements MedicationController.Transformer {

  @Override
  public Medication apply(CdwMedication medication) {
    return medication(medication);
  }

  CodeableConcept code(CdwCodeableConcept optionalSource) {
    if (optionalSource == null
        || (optionalSource.getCoding().isEmpty() && optionalSource.getText() == null)) {
      return null;
    }
    return convert(
        optionalSource,
        cdw ->
            CodeableConcept.builder()
                .text(cdw.getText())
                .coding(codeCodings(cdw.getCoding()))
                .build());
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

  CodeableConcept form(CdwCodeableConcept source) {
    if (source == null || (source.getCoding().isEmpty() && source.getText() == null)) {
      return null;
    }
    return CodeableConcept.builder()
        .text(source.getText())
        .coding(formCodings(source.getCoding()))
        .build();
  }

  List<Coding> formCodings(List<CdwCoding> source) {
    List<Coding> formCodings = convertAll(source, this::formCoding);
    if (formCodings == null) {
      return null;
    }
    return formCodings.isEmpty() ? null : formCodings;
  }

  private Coding formCoding(CdwCoding cdw) {
    if (cdw == null || allNull(cdw.getCode(), cdw.getDisplay(), cdw.getSystem())) {
      return null;
    }
    return Coding.builder()
        .system(cdw.getSystem())
        .code(cdw.getCode())
        .display(cdw.getDisplay())
        .build();
  }

  Medication medication(CdwMedication source) {
    return Medication.builder()
        .id(source.getCdwId())
        .resourceType("Medication")
        .text(text(source.getText()))
        .code(code(source.getCode()))
        .product(product(source.getProduct()))
        .build();
  }

  Product product(CdwProduct optionalSource) {
    if (optionalSource == null || allNull(optionalSource.getForm(), optionalSource.getId())) {
      return null;
    }
    return convert(
        optionalSource, cdw -> Product.builder().id(cdw.getId()).form(form(cdw.getForm())).build());
  }

  Narrative text(String optionalSource) {
    if (optionalSource == null || StringUtils.isBlank(optionalSource)) {
      return null;
    }
    return convert(
        optionalSource,
        cdw ->
            Narrative.builder()
                .div("<div>" + cdw + "</div>")
                .status(NarrativeStatus.additional)
                .build());
  }
}
