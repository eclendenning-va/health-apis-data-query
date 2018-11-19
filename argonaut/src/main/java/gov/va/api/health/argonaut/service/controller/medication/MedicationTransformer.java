package gov.va.api.health.argonaut.service.controller.medication;

import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertString;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Narrative;
import gov.va.api.health.argonaut.api.elements.Narrative.NarrativeStatus;
import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.api.resources.Medication.Product;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MedicationTransformer implements MedicationController.Transformer {

  @Override
  public Medication apply(CdwMedication medication) {
    return medication(medication);
  }

  CodeableConcept code(gov.va.dvp.cdw.xsd.model.CdwCodeableConcept optionalSource) {
    return convert(
        optionalSource,
        cdw ->
            CodeableConcept.builder().text(cdw.getText()).coding(coding(cdw.getCoding())).build());
  }

  List<Coding> coding(List<gov.va.dvp.cdw.xsd.model.CdwCoding> optionalSource) {
    return convertAll(
        optionalSource,
        cdw ->
            Coding.builder()
                .code(cdw.getCode())
                .system(cdw.getSystem())
                .display(cdw.getDisplay())
                .build());
  }

  private Medication medication(CdwMedication source) {
    return Medication.builder()
        .id(source.getCdwId())
        .resourceType("Medication")
        .text(text(source.getText()))
        .code(code(source.getCode()))
        .product(product(source.getProduct()))
        .build();
  }

  Product product(CdwMedication101Root.CdwMedications.CdwMedication.CdwProduct optionalSource) {
    return convert(
        optionalSource,
        cdw -> Product.builder().id(cdw.getId()).form(productForm(cdw.getForm())).build());
  }

  CodeableConcept productForm(gov.va.dvp.cdw.xsd.model.CdwCodeableConcept source) {
    return CodeableConcept.builder()
        .text(source.getText())
        .coding(coding(source.getCoding()))
        .build();
  }

  Narrative text(String optionalSource) {
    return convertString(
        optionalSource,
        cdw ->
            Narrative.builder()
                .div("<div>" + cdw + "</div>")
                .status(NarrativeStatus.additional)
                .build());
  }
}
