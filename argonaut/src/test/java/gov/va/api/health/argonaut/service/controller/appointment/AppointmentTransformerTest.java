package gov.va.api.health.argonaut.service.controller.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import gov.va.api.health.argonaut.api.resources.Appointment;
import gov.va.api.health.argonaut.api.resources.Appointment.Status;
import gov.va.dvp.cdw.xsd.model.CdwAppointment101Root.CdwAppointments.CdwAppointment;
import gov.va.dvp.cdw.xsd.model.CdwAppointmentStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.Test;

public class AppointmentTransformerTest {

  private final AppointmentTransformer tx = new AppointmentTransformer();
  private final CdwSampleData cdw = CdwSampleData.get();
  private final Expected expected = Expected.get();

  @Test
  public void REMOVE_ME_WHEN_DONE() {
    fail();
  }

  @Test
  public void appointment() {
    assertThat(tx.apply(cdw.appointment())).isEqualTo(expected.appointment());
  }

  @Test
  public void status() {
    assertThat(tx.status(CdwAppointmentStatus.PROPOSED)).isEqualTo(Status.proposed);
    assertThat(tx.status(CdwAppointmentStatus.PENDING)).isEqualTo(Status.pending);
    assertThat(tx.status(CdwAppointmentStatus.BOOKED)).isEqualTo(Status.booked);
    assertThat(tx.status(CdwAppointmentStatus.ARRIVED)).isEqualTo(Status.arrived);
    assertThat(tx.status(CdwAppointmentStatus.FULFILLED)).isEqualTo(Status.fulfilled);
    assertThat(tx.status(CdwAppointmentStatus.CANCELLED)).isEqualTo(Status.cancelled);
    assertThat(tx.status(CdwAppointmentStatus.NOSHOW)).isEqualTo(Status.noshow);
    // TODO Entered in error should not exist ... how should it be mapped
    assertThat(tx.status(CdwAppointmentStatus.ENTERED_IN_ERROR)).isEqualTo(Status.arrived);
  }

  //  @Test
  //  public void category() {
  //    assertThat(tx.category(null)).isNull();
  //    assertThat(tx.category(new CdwCategory())).isNull();
  //    assertThat(tx.category(cdw.category())).isEqualTo(expected.category());
  //  }
  //
  //  @Test
  //  public void code() {
  //    assertThat(tx.code(null)).isNull();
  //    assertThat(tx.code(new CdwCode())).isNull();
  //    assertThat(tx.code(cdw.code())).isEqualTo(expected.code());
  //  }
  //
  //  @Test
  //  public void component() {
  //    assertThat(tx.component(null)).isNull();
  //    assertThat(tx.component(new CdwComponent())).isNull();
  //    assertThat(tx.component(cdw.componentWithCodeableConcept()))
  //        .isEqualTo(expected.componentWithCodeableConcept());
  //    assertThat(tx.component(cdw.componentWithQuantity()))
  //        .isEqualTo(expected.componentWithQuantity());
  //  }
  //
  //  @Test
  //  public void componentCode() {
  //    assertThat(tx.componentCode(null)).isNull();
  //    assertThat(tx.componentCode(new CdwComponent.CdwCode())).isNull();
  //    assertThat(tx.componentCode(cdw.componentCode())).isEqualTo(expected.componentCode());
  //  }
  //
  //  @Test
  //  public void componentValueCodeableConcept() {
  //    assertThat(tx.componentValueCodeableConcept(null)).isNull();
  //    assertThat(tx.componentValueCodeableConcept(new CdwValueCodeableConcept())).isNull();
  //    assertThat(tx.componentValueCodeableConcept(cdw.componentCodeableConcept()))
  //        .isEqualTo(expected.componentCodeableConcept());
  //  }
  //
  //  @Test
  //  public void componentValueQuantity() {
  //    assertThat(tx.componentValueQuantity(null)).isNull();
  //    assertThat(tx.componentValueQuantity(new CdwComponent.CdwValueQuantity())).isNull();
  //    assertThat(tx.componentValueQuantity(cdw.componentQuantity()))
  //        .isEqualTo(expected.componentQuantity());
  //  }
  //
  //  @Test
  //  public void components() {
  //    assertThat(tx.components(null)).isNull();
  //    assertThat(tx.components(new CdwComponents())).isNull();
  //    assertThat(tx.components(cdw.components())).isEqualTo(expected.components());
  //  }
  //
  //  @Test
  //  public void interpretation() {
  //    assertThat(tx.interpretation(null)).isNull();
  //    assertThat(tx.interpretation(cdw.interpretation())).isEqualTo(expected.interpretation());
  //  }
  //
  //  @Test
  //  public void observation() {
  //    assertThat(tx.apply(cdw.observation())).isEqualTo(expected.observation());
  //    assertThat(tx.apply(cdw.observationWithValueCodeableConcept()))
  //        .isEqualTo(expected.observationWithValueCodeableConcept());
  //  }
  //
  //  @Test
  //  public void performers() {
  //    assertThat(tx.performers(null)).isNull();
  //    assertThat(tx.performers(new CdwPerformers())).isNull();
  //    assertThat(tx.performers(cdw.performers())).isEqualTo(expected.performers());
  //  }
  //
  //  @Test
  //  public void reference() {
  //    assertThat(tx.reference(null)).isNull();
  //    assertThat(tx.reference(cdw.reference("x", "y"))).isEqualTo(expected.reference("x", "y"));
  //  }
  //
  //  @Test
  //  public void referenceRange() {
  //    assertThat(tx.referenceRange(null)).isNull();
  //    assertThat(tx.referenceRange(new CdwReferenceRange())).isNull();
  //    assertThat(tx.referenceRange(cdw.referenceRange())).isEqualTo(expected.referenceRange());
  //  }
  //
  //  @Test
  //  public void referenceRangeQuantity() {
  //    assertThat(tx.referenceRangeQuantity(null)).isNull();
  //    assertThat(tx.referenceRangeQuantity(new CdwObservationRefRangeQuantity())).isNull();
  //    assertThat(tx.referenceRangeQuantity(cdw.referenceRangeQuantity(1)))
  //        .isEqualTo(expected.simpleQuantity(1));
  //  }
  //
  //  @Test
  //  public void referenceRanges() {
  //    assertThat(tx.referenceRanges(null)).isNull();
  //    assertThat(tx.referenceRanges(new CdwReferenceRanges())).isNull();
  //    assertThat(tx.referenceRanges(cdw.referenceRanges())).isEqualTo(expected.referenceRanges());
  //  }
  //
  //  @Test
  //  public void status() {
  //    assertThat(tx.status(CdwObservationStatus.REGISTERED)).isEqualTo(Status.registered);
  //    assertThat(tx.status(CdwObservationStatus.PRELIMINARY)).isEqualTo(Status.preliminary);
  //    assertThat(tx.status(CdwObservationStatus.FINAL)).isEqualTo(Status._final);
  //    assertThat(tx.status(CdwObservationStatus.AMENDED)).isEqualTo(Status.amended);
  //    assertThat(tx.status(CdwObservationStatus.CANCELLED)).isEqualTo(Status.cancelled);
  //
  // assertThat(tx.status(CdwObservationStatus.ENTERED_IN_ERROR)).isEqualTo(Status.entered_in_error);
  //    assertThat(tx.status(CdwObservationStatus.UNKNOWN)).isEqualTo(Status.unknown);
  //  }
  //
  //  @Test
  //  public void valueCodeableConcept() {
  //    assertThat(tx.valueCodeableConcept(null)).isNull();
  //    assertThat(tx.valueCodeableConcept(cdw.valueCodeableConcept()))
  //        .isEqualTo(expected.valueCodeableConcept());
  //  }
  //
  //  @Test
  //  public void valueQuantity() {
  //    assertThat(tx.valueQuantity(null)).isNull();
  //    assertThat(tx.valueQuantity(cdw.valueQuantity())).isEqualTo(expected.valueQuantity());
  //  }

  @NoArgsConstructor(staticName = "get", access = AccessLevel.PUBLIC)
  static class CdwSampleData {
    CdwAppointment appointment() {
      CdwAppointment cdw = new CdwAppointment();
      cdw.setCdwId("1200438317388");
      cdw.setStatus(CdwAppointmentStatus.FULFILLED);
      // TODO
      return cdw;
    }
  }

  @NoArgsConstructor(staticName = "get")
  public static class Expected {

    Appointment appointment() {
      return Appointment.builder()
          .resourceType("Appointment")
          .id("1200438317388")
          .status(Status.fulfilled)
          // TODO
          .build();
    }

    //    CodeableConcept category() {
    //      return codeableConcept(
    //          coding("http://hl7.org/fhir/observation-category", "vital-signs", "Vital Signs"));
    //    }
    //
    //    CodeableConcept code() {
    //      return codeableConcept(coding("http://loinc.org", "8867-4", "Heart rate")).text("<3");
    //    }
    //
    //    CodeableConcept codeableConcept(Coding coding) {
    //      return CodeableConcept.builder().coding(singletonList(coding)).build();
    //    }
    //
    //    Coding coding(String system, String code, String display) {
    //      return Coding.builder().system(system).code(code).display(display).build();
    //    }
    //
    //    private CodeableConcept componentCode() {
    //      return CodeableConcept.builder()
    //          .text("Systolic blood pressure")
    //          .coding(singletonList(componentCodeCoding()))
    //          .build();
    //    }
    //
    //    private Coding componentCodeCoding() {
    //      return Coding.builder()
    //          .code("8480-6")
    //          .display("Systolic blood pressure")
    //          .system("http://loinc.org")
    //          .build();
    //    }
    //
    //    private CodeableConcept componentCodeableConcept() {
    //      return CodeableConcept.builder()
    //          .text("component cc")
    //          .coding(singletonList(componentCodeableConceptCoding()))
    //          .build();
    //    }
    //
    //    private Coding componentCodeableConceptCoding() {
    //      return coding("http://example.com", "cccc1", "component codeable concept coding");
    //    }
    //
    //    private Quantity componentQuantity() {
    //      return Quantity.builder()
    //          .code("mm[Hg]")
    //          .system("http://unitsofmeasure.org")
    //          .unit("mm[Hg]")
    //          .value(67D)
    //          .comparator(">")
    //          .build();
    //    }
    //
    //    private ObservationComponent componentWithCodeableConcept() {
    //      return ObservationComponent.builder()
    //          .id("component1")
    //          .code(componentCode())
    //          .valueCodeableConcept(componentCodeableConcept())
    //          .build();
    //    }
    //
    //    private ObservationComponent componentWithQuantity() {
    //      return ObservationComponent.builder()
    //          .id("component1")
    //          .code(componentCode())
    //          .valueQuantity(componentQuantity())
    //          .build();
    //    }
    //
    //    List<ObservationComponent> components() {
    //      return singletonList(componentWithQuantity());
    //    }
    //
    //    public CodeableConcept interpretation() {
    //      return codeableConcept(coding("http://hl7.org/fhir/v2/0078", "L", "Low")).text("L");
    //    }
    //
    //    public Observation observation() {
    //      return Observation.builder()
    //          .resourceType("Observation")
    //          .id("1201051417263:V")
    //          .status(Status._final)
    //          .category(category())
    //          .code(code())
    //          .subject(reference("Patient/185601V825290", "VETERAN,JOHN Q"))
    //          .encounter(reference("Encounter/1234", "The 3rd Kind"))
    //          .effectiveDateTime("2015-04-15T14:16:38Z")
    //          .issued("2015-04-15T14:19:45Z")
    //          .performer(performers())
    //          .valueQuantity(valueQuantity())
    //          .interpretation(interpretation())
    //          .comments("observe, ladies and gentlemen")
    //          // .specimen() // Intentionally omitted since specimen resources are not supported
    //          .referenceRange(referenceRanges())
    //          .component(components())
    //          .build();
    //    }
    //
    //    Observation observationWithValueCodeableConcept() {
    //      return observation().valueQuantity(null).valueCodeableConcept(valueCodeableConcept());
    //    }
    //
    //    List<Reference> performers() {
    //      return singletonList(reference("Practitioner/1715142", "SMITH,ATTENDING D"));
    //    }
    //
    //    Reference reference(String ref, String display) {
    //      return Reference.builder().reference(ref).display(display).build();
    //    }
    //
    //    private ObservationReferenceRange referenceRange() {
    //      return ObservationReferenceRange.builder()
    //          .high(simpleQuantity(10))
    //          .low(simpleQuantity(1))
    //          .build();
    //    }
    //
    //    private List<ObservationReferenceRange> referenceRanges() {
    //      return singletonList(referenceRange());
    //    }
    //
    //    private SimpleQuantity simpleQuantity(int value) {
    //      return SimpleQuantity.builder()
    //          .system("http://unitsofmeasure.org")
    //          .code("k/cmm")
    //          .unit("k/cmm")
    //          .value((double) value)
    //          .build();
    //    }
    //
    //    CodeableConcept valueCodeableConcept() {
    //      return codeableConcept(coding("http://example.com", "vcc", "value codeable concept
    // coding"))
    //          .text("value cc");
    //    }
    //
    //    Quantity valueQuantity() {
    //      return Quantity.builder()
    //          .system("http://unitsofmeasure.org")
    //          .code("/min")
    //          .comparator("<")
    //          .unit("/min")
    //          .value(74D)
    //          .build();
    //    }
  }
}
