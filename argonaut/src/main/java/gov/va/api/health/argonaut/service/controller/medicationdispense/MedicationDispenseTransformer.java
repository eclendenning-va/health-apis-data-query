package gov.va.api.health.argonaut.service.controller.medicationdispense;

import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.MedicationDispense;
import gov.va.api.health.argonaut.api.resources.MedicationDispense.Status;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispenseStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import org.springframework.stereotype.Service;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;

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

  private boolean isUsable(CdwReference reference) {
    return reference != null && !allNull(reference.getReference(), reference.getDisplay());
  }
}
