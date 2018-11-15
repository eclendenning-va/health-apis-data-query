package gov.va.api.health.argonaut.service.controller.medication;

import gov.va.api.health.argonaut.api.CodeableConcept;
import gov.va.api.health.argonaut.api.Coding;
import gov.va.api.health.argonaut.api.Medication;
import gov.va.api.health.argonaut.api.Medication.Product;
import gov.va.api.health.argonaut.api.Narrative;
import gov.va.api.health.argonaut.api.Narrative.NarrativeStatus;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MedicationTransformer implements MedicationController.Transformer {

  @Override
  public Medication apply(CdwMedication medication) {
    return medication(medication);
  }

  CodeableConcept code(gov.va.dvp.cdw.xsd.model.CdwCodeableConcept code) {
    if (code == null) {
      return null;
    }
    return CodeableConcept.builder().text(code.getText()).coding(coding(code.getCoding())).build();
  }

  List<Coding> coding(List<gov.va.dvp.cdw.xsd.model.CdwCoding> coding) {
    if (coding == null) {
      return null;
    }
    LinkedList<Coding> argoCodings = new LinkedList<>();
    for (gov.va.dvp.cdw.xsd.model.CdwCoding argoCoding : coding) {
      argoCodings.add(
          Coding.builder()
              .code(argoCoding.getCode())
              .system(argoCoding.getSystem())
              .display(argoCoding.getDisplay())
              .build());
    }
    return argoCodings;
  }

  private Medication medication(CdwMedication medication) {
    return Medication.builder()
        .id(medication.getCdwId())
        .resourceType("Medication")
        .text(text(medication.getText()))
        .code(code(medication.getCode()))
        .product(product(medication.getProduct()))
        .build();
  }

  Narrative text(String text) {
    if (StringUtils.isEmpty(text)) {
      return null;
    }
    return Narrative.builder()
        .div("<div>" + text + "</div>")
        .status(NarrativeStatus.additional)
        .build();
  }

  Product product(CdwMedication101Root.CdwMedications.CdwMedication.CdwProduct product) {
    if (product == null) {
      return null;
    }
    return Product.builder().id(product.getId()).form(productForm(product.getForm())).build();  
  }

  CodeableConcept productForm(gov.va.dvp.cdw.xsd.model.CdwCodeableConcept productForm) {
    return CodeableConcept.builder()
        .text(productForm.getText())
        .coding(coding(productForm.getCoding()))
        .build();
  }
}
