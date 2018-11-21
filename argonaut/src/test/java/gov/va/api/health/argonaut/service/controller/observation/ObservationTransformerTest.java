package gov.va.api.health.argonaut.service.controller.observation;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.Observation.Status;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwCategory;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwCategory.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwCode;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwComponents;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwComponents.CdwComponent;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwComponents.CdwComponent.CdwValueCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwInterpretation;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwPerformers;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwReferenceRanges;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwReferenceRanges.CdwReferenceRange;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwValueQuantity;
import gov.va.dvp.cdw.xsd.model.CdwObservationCategoryCode;
import gov.va.dvp.cdw.xsd.model.CdwObservationCategoryDisplay;
import gov.va.dvp.cdw.xsd.model.CdwObservationCategorySystem;
import gov.va.dvp.cdw.xsd.model.CdwObservationInterpretationSystem;
import gov.va.dvp.cdw.xsd.model.CdwObservationRefRangeQuantity;
import gov.va.dvp.cdw.xsd.model.CdwObservationStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

public class ObservationTransformerTest {

  private final ObservationTransformer tx = new ObservationTransformer();
  private final CdwSampleData cdw = CdwSampleData.get();
  private final Expected expected = Expected.get();

  @Test
  public void observation() {
    assertThat(tx.apply(cdw.observation())).isEqualTo(expected.observation());
  }

  @Test
  public void status() {
    assertThat(tx.status(CdwObservationStatus.REGISTERED)).isEqualTo(Status.registered);
    assertThat(tx.status(CdwObservationStatus.PRELIMINARY)).isEqualTo(Status.preliminary);
    assertThat(tx.status(CdwObservationStatus.FINAL)).isEqualTo(Status._final);
    assertThat(tx.status(CdwObservationStatus.AMENDED)).isEqualTo(Status.amended);
    assertThat(tx.status(CdwObservationStatus.CANCELLED)).isEqualTo(Status.cancelled);
    assertThat(tx.status(CdwObservationStatus.ENTERED_IN_ERROR)).isEqualTo(Status.amended);
    assertThat(tx.status(CdwObservationStatus.UNKNOWN)).isEqualTo(Status.unknown);
  }

  @NoArgsConstructor(staticName = "get")
  private static class CdwSampleData {

    private CdwCategory category() {
      CdwCategory cdw = new CdwCategory();
      cdw.getCoding().add(categoryCoding());
      return cdw;
    }

    private CdwCoding categoryCoding() {
      CdwCoding cdw = new CdwCoding();
      cdw.setSystem(CdwObservationCategorySystem.HTTP_HL_7_ORG_FHIR_OBSERVATION_CATEGORY);
      cdw.setCode(CdwObservationCategoryCode.VITAL_SIGNS);
      cdw.setDisplay(CdwObservationCategoryDisplay.VITAL_SIGNS);
      return cdw;
    }

    private CdwCode code() {
      CdwCode cdw = new CdwCode();
      cdw.setText("<3");
      cdw.getCoding().add(coding());
      return cdw;
    }

    private CdwCode.CdwCoding coding() {
      CdwCode.CdwCoding cdw = new CdwCode.CdwCoding();
      cdw.setSystem("http://loinc.org");
      cdw.setCode("8867-4");
      cdw.setDisplay("Heart rate");
      return cdw;
    }

    private CdwComponent.CdwCode componentCode() {
      CdwComponent.CdwCode cdw = new CdwComponent.CdwCode();
      cdw.setText("Systolic blood pressure");
      cdw.getCoding().add(componentCodeCoding());
      return cdw;
    }

    private CdwComponent.CdwCode.CdwCoding componentCodeCoding() {
      CdwComponent.CdwCode.CdwCoding cdw = new CdwComponent.CdwCode.CdwCoding();
      cdw.setCode("8480-6");
      cdw.setDisplay("Systolic blood pressure");
      cdw.setSystem("http://loinc.org");
      return cdw;
    }

    private CdwValueCodeableConcept componentCodeableConcept() {
      CdwValueCodeableConcept cdw = new CdwValueCodeableConcept();
      cdw.setText("component cc");
      cdw.getCoding().add(componentCodeableConceptCoding());
      return cdw;
    }

    private CdwValueCodeableConcept.CdwCoding componentCodeableConceptCoding() {
      CdwValueCodeableConcept.CdwCoding cdw = new CdwValueCodeableConcept.CdwCoding();
      cdw.setCode("cccc1");
      cdw.setSystem("http://example.com");
      cdw.setDisplay("component codeable concept coding");
      return cdw;
    }

    private CdwComponent.CdwValueQuantity componentQuantity() {
      CdwComponent.CdwValueQuantity cdw = new CdwComponent.CdwValueQuantity();
      cdw.setCode("mm[Hg]");
      cdw.setSystem("http://unitsofmeasure.org");
      cdw.setUnit("mm[Hg]");
      cdw.setValue(BigDecimal.valueOf(67));
      cdw.setComparator(">");
      return cdw;
    }

    private CdwComponent componentWithCodeableConcept() {
      CdwComponent cdw = new CdwComponent();
      cdw.setCode(componentCode());
      cdw.setId("component1");
      cdw.setValueCodeableConcept(componentCodeableConcept());
      return cdw;
    }

    private CdwComponent componentWithQuantity() {
      CdwComponent cdw = new CdwComponent();
      cdw.setCode(componentCode());
      cdw.setId("component1");
      cdw.setValueQuantity(componentQuantity());
      return cdw;
    }

    private CdwComponents components() {
      CdwComponents cdw = new CdwComponents();
      cdw.getComponent().add(componentWithQuantity());
      return cdw;
    }

    @SneakyThrows
    private XMLGregorianCalendar dateTime(String timestamp) {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(timestamp);
    }

    private CdwInterpretation interpretation() {
      CdwInterpretation cdw = new CdwInterpretation();
      cdw.setText("L");
      cdw.getCoding().add(interpretationCoding());
      return cdw;
    }

    private CdwInterpretation.CdwCoding interpretationCoding() {
      CdwInterpretation.CdwCoding cdw = new CdwInterpretation.CdwCoding();
      cdw.setSystem(CdwObservationInterpretationSystem.HTTP_HL_7_ORG_FHIR_V_2_0078);
      cdw.setCode("L");
      cdw.setDisplay("Low");
      return cdw;
    }

    CdwObservation observation() {
      CdwObservation cdw = new CdwObservation();
      cdw.setRowNumber(BigInteger.ONE);
      cdw.setCdwId("1201051417263:V");
      cdw.setStatus(CdwObservationStatus.FINAL);
      cdw.setCategory(category());
      cdw.setCode(code());
      cdw.setSubject(reference("Patient/185601V825290", "VETERAN,JOHN Q"));
      cdw.setEffectiveDateTime(dateTime("2015-04-15T14:16:38Z"));
      cdw.setIssued(dateTime("2015-04-15T14:19:45Z"));
      cdw.setPerformers(performers());
      cdw.setValueQuantity(valueQuantity());
      cdw.setComments("observe, ladies and gentlemen");
      cdw.setComponents(components());
      cdw.setEncounter(reference("Encounter/1234", "The 3rd Kind"));
      cdw.setInterpretation(interpretation());
      cdw.setReferenceRanges(referenceRanges());
      cdw.setSpecimen(reference("Specimen/1200004290", "BLOOD"));
      cdw.setSubject(reference("Patient/185601V825290", "VETERAN,JOHN Q"));
      cdw.setValueCodeableConcept(valueCodeableConcept());
      return cdw;
    }

    private CdwPerformers performers() {
      CdwPerformers cdw = new CdwPerformers();
      cdw.getPerformer().add(reference("Practitioner/1715142", "SMITH,ATTENDING D"));
      return cdw;
    }

    private CdwReference reference(String ref, String display) {
      CdwReference cdw = new CdwReference();
      cdw.setReference(ref);
      cdw.setDisplay(display);
      return cdw;
    }

    private CdwReferenceRange referenceRange() {
      CdwReferenceRange cdw = new CdwReferenceRange();
      cdw.setHigh(referenceRangeQuantity(10));
      cdw.setLow(referenceRangeQuantity(1));
      return cdw;
    }

    private CdwObservationRefRangeQuantity referenceRangeQuantity(long value) {
      CdwObservationRefRangeQuantity cdw = new CdwObservationRefRangeQuantity();
      cdw.setCode("k/cmm");
      cdw.setUnit("k/cmm");
      cdw.setSystem("http://unitsofmeasure.org");
      cdw.setValue(BigDecimal.valueOf(value));
      return cdw;
    }

    private CdwReferenceRanges referenceRanges() {
      CdwReferenceRanges cdw = new CdwReferenceRanges();
      cdw.getReferenceRange().add(referenceRange());
      return cdw;
    }

    private CdwObservation.CdwValueCodeableConcept valueCodeableConcept() {
      CdwObservation.CdwValueCodeableConcept cdw = new CdwObservation.CdwValueCodeableConcept();
      cdw.setText("value cc");
      cdw.getCoding().add(valueCodeableConceptCoding());
      return cdw;
    }

    private CdwObservation.CdwValueCodeableConcept.CdwCoding valueCodeableConceptCoding() {
      CdwObservation.CdwValueCodeableConcept.CdwCoding cdw =
          new CdwObservation.CdwValueCodeableConcept.CdwCoding();
      cdw.setCode("vcc");
      cdw.setDisplay("value codeable concept coding");
      cdw.setSystem("http://example.com");
      return cdw;
    }

    private CdwValueQuantity valueQuantity() {
      CdwValueQuantity cdw = new CdwValueQuantity();
      cdw.setCode("/min");
      cdw.setComparator("=");
      cdw.setSystem("http://unitsofmeasure.org");
      cdw.setUnit("/min");
      cdw.setValue(BigDecimal.valueOf(74));
      return cdw;
    }
  }

  @NoArgsConstructor(staticName = "get")
  private static class Expected {

    public Observation observation() {
      return Observation.builder()
          .resourceType("Observation")
          .id("1201051417263:V")
          .status(Status._final)
          .build();
    }
  }
}
