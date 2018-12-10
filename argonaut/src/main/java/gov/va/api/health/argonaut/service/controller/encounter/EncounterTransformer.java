package gov.va.api.health.argonaut.service.controller.encounter;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.Period;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Encounter;
import gov.va.api.health.argonaut.api.resources.Encounter.EncounterClass;
import gov.va.api.health.argonaut.api.resources.Encounter.EncounterLocation;
import gov.va.api.health.argonaut.api.resources.Encounter.Participant;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter.CdwIndications;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter.CdwLocations;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter.CdwParticipants;
import gov.va.dvp.cdw.xsd.model.CdwEncounterClass;
import gov.va.dvp.cdw.xsd.model.CdwEncounterParticipantType;
import gov.va.dvp.cdw.xsd.model.CdwEncounterPeriod;
import gov.va.dvp.cdw.xsd.model.CdwEncounterStatus;
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

  List<Coding> encounterParticipantTypeCoding(CdwEncounterParticipantType.CdwCoding maybeCdw) {
    if (maybeCdw == null
        || allNull(maybeCdw.getCode(), maybeCdw.getDisplay(), maybeCdw.getSystem())) {
      return null;
    }
    return convert(
        maybeCdw,
        cdw ->
            Collections.singletonList(
                Coding.builder()
                    .code(cdw.getCode().value())
                    .system(cdw.getSystem())
                    .display(cdw.getDisplay().value())
                    .build()));
  }

  Reference reference(CdwReference maybeCdw) {
    if (maybeCdw == null || allNull(maybeCdw.getReference(), maybeCdw.getDisplay())) {
      return null;
    }
    return convert(
        maybeCdw,
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }

  Period period(CdwEncounterPeriod maybeCdw) {
    if (maybeCdw == null || allNull(maybeCdw.getEnd(), maybeCdw.getStart())) {
      return null;
    }
    return convert(
        maybeCdw,
        source ->
            Period.builder()
                .start(asDateTimeString(source.getStart()))
                .end(asDateTimeString(source.getEnd()))
                .build());
  }

  List<Reference> episodeOfCare(CdwReference maybeCdw) {
    if (maybeCdw == null || allNull(maybeCdw.getDisplay(), maybeCdw.getReference())) {
      return null;
    }
    return convert(
        maybeCdw,
        source ->
            Collections.singletonList(
                Reference.builder()
                    .reference(source.getReference())
                    .display(source.getDisplay())
                    .build()));
  }

  List<EncounterLocation> location(CdwLocations maybeCdw) {
    return convertAll(
        ifPresent(maybeCdw, CdwLocations::getLocation),
        source ->
            EncounterLocation.builder().location(reference(source.getLocationReference())).build());
  }

  List<Reference> indications(CdwIndications maybeCdw) {
    return convertAll(
        ifPresent(maybeCdw, CdwIndications::getIndication),
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }

  List<CodeableConcept> encounterParticipantType(List<CdwEncounterParticipantType> maybeCdw) {
    if (maybeCdw == null || maybeCdw.isEmpty()) {
      return null;
    }
    return convertAll(
        maybeCdw,
        source ->
            CodeableConcept.builder()
                .coding(encounterParticipantTypeCoding(source.getCoding()))
                .text(source.getText())
                .build());
  }

  List<Participant> participant(CdwParticipants maybeCdw) {
    return convertAll(
        ifPresent(maybeCdw, CdwParticipants::getParticipant),
        source ->
            Participant.builder()
                .individual(reference(source.getIndividual()))
                .type(encounterParticipantType(source.getType()))
                .build());
  }

  EncounterClass encounterClass(CdwEncounterClass source) {
    return ifPresent(source, status -> EnumSearcher.of(EncounterClass.class).find(status.value()));
  }

  Encounter.Status encounterStatus(CdwEncounterStatus source) {
    return ifPresent(
        source, status -> EnumSearcher.of(Encounter.Status.class).find(status.value()));
  }

  private Encounter encounter(CdwEncounter source) {
    return Encounter.builder()
        .resourceType("Encounter")
        .id(source.getCdwId())
        .appointment(reference(source.getAppointment()))
        .encounterClass(encounterClass(source.getClazz()))
        .episodeOfCare(episodeOfCare(source.getEpisodeOfCare()))
        .indication(indications(source.getIndications()))
        .location(location(source.getLocations()))
        .participant(participant(source.getParticipants()))
        .patient(reference(source.getPatient()))
        .serviceProvider(reference(source.getServiceProvider()))
        .status(encounterStatus(source.getStatus()))
        .period(period(source.getPeriod()))
        .build();
  }
}
