package gov.va.api.health.argonaut.service.controller.allergyintollerance;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Reaction;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwReactions;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwReactions.CdwReaction.CdwManifestations.CdwManifestation;
import gov.va.dvp.cdw.xsd.model.CdwAllergyManifestationSystem;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
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
        .onset(asDateTimeString(source.getOnset()))
        .recordedDate(asDateTimeString(source.getRecordedDate()))
        .recorder(recorder(source.getRecorder()))
        .patient(patient(source.getPatient()))
        .status(ifPresent(source.getStatus(), status -> AllergyIntolerance.Status.valueOf(status.value())))
        .criticality(ifPresent(source.getCriticality(), criticality -> AllergyIntolerance.Criticality.valueOf(criticality.value())))
        .type(ifPresent(source.getType(), type -> AllergyIntolerance.Type.valueOf(type.value())))
        .category(ifPresent(source.getCategory(), category -> AllergyIntolerance.Category.valueOf(category.value())))
        //come back to note
        .reaction(reactions(source.getReactions()))
        .build();
  }

  Reference patient (CdwReference source) {
    return Reference.builder()
        .display(source.getDisplay())
        .display(source.getReference())
        .build();
  }

  List<CodeableConcept> reactionManifestation(CdwManifestation source) {
    if (source == null) {
      return Collections.emptyList();
    }
    return Collections.singletonList(
        CodeableConcept.builder()
            .coding(reactionManifestationCoding(source.getCoding()))
            .text(source.getText())
            .build());
  }

  List<Coding> reactionManifestationCoding(CdwManifestation.CdwCoding source) {
    return Collections.singletonList(
        Coding.builder()
            .system(ifPresent(source.getSystem(), CdwAllergyManifestationSystem::value))
            .code(source.getCode())
            .display(source.getDisplay())
            .build());
  }

  List<Reaction> reactions(CdwReactions optionalSource) {
    return convertAll(
        ifPresent(optionalSource, CdwReactions::getReaction),
        cdw ->
            Reaction.builder()
                .certainty(ifPresent(cdw.getCertainty(), certainty -> AllergyIntolerance.Certainty.valueOf(certainty.value())))
                .manifestation(reactionManifestation(cdw.getManifestations().getManifestation().get(0))) // won't work, needs to be incremented... loop?
                .build());
  }

  Reference recorder (CdwReference source) {
    return Reference.builder()
        .display(source.getDisplay())
        .reference(source.getReference())
        .build();
  }
}
