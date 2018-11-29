package gov.va.api.health.argonaut.service.controller.encounter;

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
import gov.va.api.health.argonaut.api.resources.Encounter.Location;
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
    if (maybeCdw == null) {
      return null;
    }
    if (maybeCdw.getCode() == null || maybeCdw.getDisplay() == null) {
      return null;
    }
    return convert(
        maybeCdw,
        cdw ->
            Collections.singletonList(Coding.builder()
                .code(cdw.getCode().value())
                .system(cdw.getSystem())
                .display(cdw.getDisplay().value())
                .build()));
  }

  Reference reference(CdwReference maybeCdw) {
    if (maybeCdw == null) {
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
    return convert(
        maybeCdw,
        source ->
            Period.builder()
                .start(asDateTimeString(source.getStart()))
                .end(asDateTimeString(source.getEnd()))
                .build());
  }

  List<Reference> episodeOfCare(CdwReference maybeCdw) {
    return convert(
        maybeCdw,
        source ->
            Collections.singletonList(Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build()));
  }

  List<Location> location(CdwLocations maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    if (maybeCdw.getLocation() == null) {
      return null;
    }
    return convertAll(
        maybeCdw.getLocation(),
        source ->
            Location.builder()
                .location(reference(source.getLocationReference()))
                .build());
  }

  List<Reference> indications(CdwIndications maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    if (maybeCdw.getIndication() == null) {
      return null;
    }
    return convertAll(
        maybeCdw.getIndication(),
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }

  List<CodeableConcept> encounterParticipantType(List<CdwEncounterParticipantType> maybeCdw){
    return convertAll(maybeCdw, source->CodeableConcept.builder().coding(encounterParticipantTypeCoding(source.getCoding())).text(source.getText()).build());
  }

  List<Participant> participant(CdwParticipants maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    if (maybeCdw.getParticipant() == null) {
      return null;
    }
    return convertAll(
        maybeCdw.getParticipant(),
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
    return ifPresent(source, status -> EnumSearcher.of(Encounter.Status.class).find(status.value()));
  }

  private Encounter encounter(CdwEncounter source) {
    return Encounter.builder()
        .id(source.getCdwId())
        .resourceType("Encounter")
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
