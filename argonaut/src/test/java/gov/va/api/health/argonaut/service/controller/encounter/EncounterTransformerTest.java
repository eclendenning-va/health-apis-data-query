package gov.va.api.health.argonaut.service.controller.encounter;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.Period;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Encounter;
import gov.va.api.health.argonaut.api.resources.Encounter.EncounterClass;
import gov.va.api.health.argonaut.api.resources.Encounter.Location;
import gov.va.api.health.argonaut.api.resources.Encounter.Participant;
import gov.va.api.health.argonaut.api.resources.Encounter.Status;

import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter.CdwIndications;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter.CdwLocations;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter.CdwLocations.CdwLocation;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter.CdwParticipants;
import gov.va.dvp.cdw.xsd.model.CdwEncounter101Root.CdwEncounters.CdwEncounter.CdwParticipants.CdwParticipant;
import gov.va.dvp.cdw.xsd.model.CdwEncounterClass;
import gov.va.dvp.cdw.xsd.model.CdwEncounterParticipantType;
import gov.va.dvp.cdw.xsd.model.CdwEncounterParticipantType.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwEncounterParticipantTypeCode;
import gov.va.dvp.cdw.xsd.model.CdwEncounterParticipantTypeDisplay;
import gov.va.dvp.cdw.xsd.model.CdwEncounterPeriod;
import gov.va.dvp.cdw.xsd.model.CdwEncounterStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.math.BigInteger;
import java.util.Collections;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

public class EncounterTransformerTest {

  private EncounterTransformer tx = new EncounterTransformer();
  private CdwSampleData cdw = new CdwSampleData();
  private Expected expected = new Expected();

  @Test
  public void encounterClass() {
    assertThat(tx.encounterClass(CdwEncounterClass.AMBULATORY)).isEqualTo(EncounterClass.ambulatory);
    assertThat(tx.encounterClass(CdwEncounterClass.DAYTIME)).isEqualTo(EncounterClass.daytime);
    assertThat(tx.encounterClass(CdwEncounterClass.EMERGENCY)).isEqualTo(EncounterClass.emergency);
    assertThat(tx.encounterClass(CdwEncounterClass.FIELD)).isEqualTo(EncounterClass.field);
    assertThat(tx.encounterClass(CdwEncounterClass.HOME)).isEqualTo(EncounterClass.home);
    assertThat(tx.encounterClass(CdwEncounterClass.INPATIENT)).isEqualTo(EncounterClass.inpatient);
    assertThat(tx.encounterClass(CdwEncounterClass.OTHER)).isEqualTo(EncounterClass.other);
    assertThat(tx.encounterClass(CdwEncounterClass.OUTPATIENT)).isEqualTo(EncounterClass.outpatient);
    assertThat(tx.encounterClass(CdwEncounterClass.VIRTUAL)).isEqualTo(EncounterClass.virtual);
  }

  @Test
  public void encounterStatus() {
    assertThat(tx.encounterStatus(CdwEncounterStatus.ARRIVED)).isEqualTo(Status.arrived);
    assertThat(tx.encounterStatus(CdwEncounterStatus.CANCELLED)).isEqualTo(Status.cancelled);
    assertThat(tx.encounterStatus(CdwEncounterStatus.FINISHED)).isEqualTo(Status.finished);
    assertThat(tx.encounterStatus(CdwEncounterStatus.IN_PROGRESS)).isEqualTo(Status.in_progress);
    assertThat(tx.encounterStatus(CdwEncounterStatus.ONLEAVE)).isEqualTo(Status.onleave);
    assertThat(tx.encounterStatus(CdwEncounterStatus.PLANNED)).isEqualTo(Status.planned);
  }



  @NoArgsConstructor(staticName = "get")
  private static class CdwSampleData {

    private CdwEncounter encounter(){
     CdwEncounter cdw = new CdwEncounter();
     cdw.setAppointment(reference("Appointment/615f31df-f0c7-5100-ac42-7fb952c630d0","Appointment"));
     cdw.setCdwId("db001de0-6ca8-50b3-9063-acea4bf5df5d");
     cdw.setClazz(CdwEncounterClass.EMERGENCY);
     cdw.setStatus(CdwEncounterStatus.FINISHED);
     cdw.setServiceProvider(reference("",""));
     cdw.setEpisodeOfCare(reference("EpisodeOfCare/1234","Episode 3"));
     cdw.setIndications(indications());
     cdw.setLocations(locations());
     cdw.setParticipants(participants());
     cdw.setPatient(reference("Patient/185601V825290","VETERAN,JOHN Q"));
     cdw.setPeriod(period());
     cdw.setRowNumber(BigInteger.ONE);
     return cdw;
    }

    @SneakyThrows
    private XMLGregorianCalendar dateTime(String timestamp) {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(timestamp);
    }

    private CdwEncounterPeriod period(){
      CdwEncounterPeriod cdw = new CdwEncounterPeriod();
      cdw.setStart(dateTime("2015-04-15T14:25:00Z"));
      cdw.setEnd(dateTime("2015-04-15T17:16:00Z"));
      return cdw;
    }

    private CdwParticipants participants() {
      CdwParticipants cdw = new CdwParticipants();
      cdw.getParticipant().add(participant());
      return cdw;
    }

    private  CdwParticipant participant(){
      CdwParticipant cdw = new CdwParticipant();
      cdw.setIndividual(reference("Patient/185601V825290", "VETERAN,JOHN Q"));
      cdw.getType().add(participantType());
      return cdw;
    }

    private CdwIndications indications(){
      CdwIndications cdw = new CdwIndications();
      cdw.getIndication().add(reference("Condition/37d89dc5-45f5-5a2e-9db9-2b17c0d7f318","Chronic asthmatic bronchitis (SNOMED CT 195949008)"));
      return cdw;
    }

    private CdwLocations locations(){
      CdwLocations cdw = new CdwLocations();
      cdw.getLocation().add(location());
      return cdw;
    }

    private CdwLocation location(){
      CdwLocation cdw = new CdwLocation();
      cdw.setLocationReference(reference("Location/eb094a51-ad31-5b6b-b627-96aac4b02b1c","GNV ED"));
      return cdw;
    }

    private CdwReference reference(String ref, String display) {
      CdwReference cdw = new CdwReference();
      cdw.setReference(ref);
      cdw.setDisplay(display);
      return cdw;
    }

    private CdwEncounterParticipantType participantType(){
    CdwEncounterParticipantType cdw = new CdwEncounterParticipantType();
    cdw.setCoding(coding());
    cdw.setText("Translates language");
    return cdw;
        }

    private CdwCoding coding() {
      CdwCoding cdw = new CdwCoding();
      cdw.setDisplay(CdwEncounterParticipantTypeDisplay.ADMITTER);
      cdw.setCode(CdwEncounterParticipantTypeCode.ADM);
      cdw.setSystem("http://hl7.org/fhir/participant-type");
      return cdw;
    }
  }

  @NoArgsConstructor(staticName = "get")
  private static class Expected {
    private Encounter encounter(){
     return Encounter.builder()
          .resourceType("Encounter")
          .period(period())
          .encounterClass(EncounterClass.emergency)
          .status(Status.finished)
          .location(singletonList(location()))
          .participant(singletonList(participant()))
//          .indication()
//          .episodeOfCare()
//          .appointment()
//          .patient()
          .build();
    }

    private Location location(){
      return Location.builder()
          .location(reference("Location/eb094a51-ad31-5b6b-b627-96aac4b02b1c","GNV ED"))
          .build();
    }

    private Period period(){
      return Period.builder()
          .start("2015-04-15T14:25:00Z")
          .end("2015-04-15T17:16:00Z")
          .build();
    }

    private Reference reference(String ref, String display) {
      return Reference.builder().reference(ref).display(display).build();
    }

    private Participant participant(){
      return Participant.builder()
          .individual(reference("Patient/185601V825290", "VETERAN,JOHN Q"))
          .type(singletonList(type()))
          .build();
    }

    private CodeableConcept type(){
      return CodeableConcept.builder()
          .coding(singletonList(coding()))
          .text("Translate Language")
          .build();
    }

    private Coding coding(){
      return Coding.builder()
          .system("http://hl7.org/fhir/participant-type")
          .display("Admitter")
          .code("ADM")
          .build();
    }

  }

}
