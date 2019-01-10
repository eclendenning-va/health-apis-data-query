package gov.va.api.health.argonaut.service.controller.medicationdispense;

import gov.va.api.health.argonaut.api.resources.MedicationDispense;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense;
import org.springframework.stereotype.Service;

@Service
public class MedicationDispenseTransformer implements MedicationDispenseController.Transformer {

    @Override
    public MedicationDispense apply(CdwMedicationDispense cdw) {

        return MedicationDispense.builder()
                .resourceType("MedicationDispense")
                .build();
    }
}
