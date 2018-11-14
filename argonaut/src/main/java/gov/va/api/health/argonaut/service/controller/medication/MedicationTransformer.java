package gov.va.api.health.argonaut.service.controller.medication;

import gov.va.api.health.argonaut.api.CodeableConcept;
import gov.va.api.health.argonaut.api.Coding;
import gov.va.api.health.argonaut.api.Medication;
import gov.va.api.health.argonaut.api.Medication.Product;
import gov.va.api.health.argonaut.api.Narrative;
import gov.va.dvp.cdw.xsd.pojos.Medication101Root;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MedicationTransformer implements MedicationController.Transformer {

  @Override
  public Medication apply(Medication101Root.Medications.Medication medication) {
    return medication(medication);
  }

  CodeableConcept code(gov.va.dvp.cdw.xsd.pojos.CodeableConcept code) {
    if (code == null) {
      return null;
    }
    return CodeableConcept.builder().text(code.getText()).coding(coding(code.getCoding())).build();
  }

  private List<Coding> coding(List<gov.va.dvp.cdw.xsd.pojos.Coding> coding) {
    if (coding == null) {
      return null;
    }
    LinkedList<Coding> argoCodings = new LinkedList<>();
    for (gov.va.dvp.cdw.xsd.pojos.Coding argoCoding : coding) {
      argoCodings.add(
          Coding.builder()
              .code(argoCoding.getCode())
              .system(argoCoding.getSystem())
              .display(argoCoding.getDisplay())
              .build());
    }
    return argoCodings;
  }

  private Medication medication(Medication101Root.Medications.Medication medication) {
    return Medication.builder()
        .id(medication.getCdwId())
        .resourceType("Medication")
        .text(narrative(medication.getText()))
        .code(code(medication.getCode()))
        .product(product(medication.getProduct()))
        .build();
  }

  Narrative narrative(String text) {
    return Narrative.builder().div("<div>" + text + "</div>").build();
  }

  Product product(Medication101Root.Medications.Medication.Product product) {
    if (product == null) {
      return null;
    }
    return Product.builder().id(product.getId()).form(productForm(product.getForm())).build();
  }

  CodeableConcept productForm(gov.va.dvp.cdw.xsd.pojos.CodeableConcept productForm) {
    // return null;
    return CodeableConcept.builder()
        .text(productForm.getText())
        .coding(coding(productForm.getCoding()))
        .build();
  }
}
