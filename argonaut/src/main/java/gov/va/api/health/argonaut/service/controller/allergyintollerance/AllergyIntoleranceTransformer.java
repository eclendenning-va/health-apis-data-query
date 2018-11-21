package gov.va.api.health.argonaut.service.controller.allergyintollerance;

import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.Annotation;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwNotes;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwNotes.CdwNote;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwSubstance;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.sql.Ref;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AllergyIntoleranceTransformer implements AllergyIntoleranceController.Transformer {
  @Override
  public AllergyIntolerance apply(CdwAllergyIntolerance allergyIntolerance) {
    return allergyIntolerance(allergyIntolerance);
  }

  private AllergyIntolerance allergyIntolerance(CdwAllergyIntolerance source) {
    return AllergyIntolerance.builder()
        .id(source.getCdwId())
        .resourceType("AllergyIntolerance")
        //.onset(source.getOnset())
        //.recordedDate(source.getRecordedDate())
        .recorder(recorder(source.getRecorder()))
        .patient(patient(source.getPatient()))
        .status(ifPresent(source.getStatus(), status -> AllergyIntolerance.Status.valueOf(status.value())))
        .criticality(ifPresent(source.getCriticality(), criticality -> AllergyIntolerance.Criticality.valueOf(criticality.value())))
        .type(ifPresent(source.getType(), type -> AllergyIntolerance.Type.valueOf(type.value())))
        .category(ifPresent(source.getCategory(), category -> AllergyIntolerance.Category.valueOf(category.value())))
        .build();
  }

  Reference patient (CdwReference source) {
    return Reference.builder()
        .display(source.getDisplay())
        .display(source.getReference())
        .build();
  }

  Reference recorder (CdwReference source) {
    return Reference.builder()
        .display(source.getDisplay())
        .reference(source.getReference())
        .build();
  }
}
