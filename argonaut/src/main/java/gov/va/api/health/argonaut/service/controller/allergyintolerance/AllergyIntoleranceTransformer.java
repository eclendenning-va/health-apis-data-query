package gov.va.api.health.argonaut.service.controller.allergyintolerance;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;
import static java.util.Collections.singletonList;

import gov.va.api.health.argonaut.api.datatypes.Annotation;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Reaction;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwNotes;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwNotes.CdwNote;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwReactions;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwReactions.CdwReaction.CdwManifestations;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwReactions.CdwReaction.CdwManifestations.CdwManifestation;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwSubstance;
import gov.va.dvp.cdw.xsd.model.CdwAllergyManifestationSystem;
import gov.va.dvp.cdw.xsd.model.CdwAllergySubstanceSystem;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AllergyIntoleranceTransformer implements AllergyIntoleranceController.Transformer {

  private AllergyIntolerance allergyIntolerance(CdwAllergyIntolerance source) {
    return AllergyIntolerance.builder()
        .id(source.getCdwId())
        .resourceType("AllergyIntolerance")
        .onset(asDateTimeString(source.getOnset()))
        .recordedDate(asDateTimeString(source.getRecordedDate()))
        .recorder(reference(source.getRecorder()))
        .substance(substance(source.getSubstance()))
        .patient(reference(source.getPatient()))
        .status(
            ifPresent(
                source.getStatus(),
                status -> EnumSearcher.of(AllergyIntolerance.Status.class).find(status.value())))
        .criticality(
            ifPresent(
                source.getCriticality(),
                criticality ->
                    EnumSearcher.of(AllergyIntolerance.Criticality.class)
                        .find(criticality.value())))
        .type(
            ifPresent(
                source.getType(),
                type -> EnumSearcher.of(AllergyIntolerance.Type.class).find(type.value())))
        .category(
            ifPresent(
                source.getCategory(),
                category ->
                    EnumSearcher.of(AllergyIntolerance.Category.class).find(category.value())))
        .note(note(source.getNotes()))
        .reaction(reaction(source.getReactions()))
        .build();
  }

  @Override
  public AllergyIntolerance apply(CdwAllergyIntolerance allergyIntolerance) {
    return allergyIntolerance(allergyIntolerance);
  }

  Annotation note(CdwNotes source) {
    if (source == null || source.getNote().isEmpty()) {
      return null;
    }
    CdwNote firstNote = source.getNote().get(0);
    return Annotation.builder()
        .text(firstNote.getText())
        .time(asDateTimeString(firstNote.getTime()))
        .authorReference(reference(firstNote.getAuthor()))
        .build();
  }

  List<Reaction> reaction(CdwReactions optionalSource) {
    if (optionalSource == null) {
      return Collections.emptyList();
    }
    return convertAll(
        ifPresent(optionalSource, CdwReactions::getReaction),
        cdw ->
            Reaction.builder()
                .certainty(
                    ifPresent(
                        cdw.getCertainty(),
                        certainty -> AllergyIntolerance.Certainty.valueOf(certainty.value())))
                .manifestation(reactionManifestation(cdw.getManifestations()))
                .build());
  }

  List<CodeableConcept> reactionManifestation(CdwManifestations source) {
    if (source == null) {
      return Collections.emptyList();
    }
    return convertAll(
        ifPresent(source, CdwManifestations::getManifestation),
        cdwManifestation ->
            CodeableConcept.builder()
                .text(cdwManifestation.getText())
                .coding(reactionManifestationCoding(cdwManifestation.getCoding()))
                .build());
  }

  List<Coding> reactionManifestationCoding(CdwManifestation.CdwCoding maybeSource) {
    return convert(
        maybeSource,
        source ->
            singletonList(
                Coding.builder()
                    .system(ifPresent(source.getSystem(), CdwAllergyManifestationSystem::value))
                    .code(source.getCode())
                    .display(source.getDisplay())
                    .build()));
  }

  Reference reference(CdwReference maybeSource) {
    return convert(
        maybeSource,
        source ->
            Reference.builder()
                .display(source.getDisplay())
                .reference(source.getReference())
                .build());
  }

  CodeableConcept substance(CdwSubstance source) {
    return CodeableConcept.builder()
        .coding(substanceCoding(source.getCoding()))
        .text(source.getText())
        .build();
  }

  List<Coding> substanceCoding(CdwSubstance.CdwCoding source) {
    return singletonList(
        Coding.builder()
            .system(ifPresent(source.getSystem(), CdwAllergySubstanceSystem::value))
            .code(source.getCode())
            .display(source.getDisplay())
            .build());
  }
}
