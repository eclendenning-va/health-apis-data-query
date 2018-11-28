package gov.va.api.health.argonaut.service.controller.encounter;

import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Encounter;
import gov.va.api.health.argonaut.api.resources.Encounter.Clazz;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter.CdwIndications;
import gov.va.dvp.cdw.xsd.model.CdwEncounterClass;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EncounterTransformer implements EncounterController.Transformer {

  @Override
  public Encounter apply(CdwEncounter encounter) {
    return encounter(encounter);
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

  Reference reference(CdwReference maybeCdw) {
    return convert(
        maybeCdw,
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }

  List<Reference> indications(CdwIndications maybeCdw) {
    return convertAll(
        maybeCdw.getIndication(),
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }

  Clazz clazz(CdwEncounterClass source) {
    return ifPresent(source, status -> EnumSearcher.of(Clazz.class).find(status.value()));
  }

  private Encounter encounter(CdwEncounter source) {
    return Encounter.builder()
        .id(source.getCdwId())
        .resourceType("Encounter")
        .appointment(reference(source.getAppointment()))
        .clazz(clazz(source.getClazz()))
        .episodeOfCare(Collections.singletonList(reference(source.getEpisodeOfCare())))
        .indication(indications(source.getIndications()))
        .build();
  }
}
