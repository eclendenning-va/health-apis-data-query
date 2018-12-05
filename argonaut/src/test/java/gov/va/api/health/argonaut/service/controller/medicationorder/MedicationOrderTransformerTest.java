package gov.va.api.health.argonaut.service.controller.medicationorder;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Duration;
import gov.va.api.health.argonaut.api.datatypes.SimpleQuantity;
import gov.va.api.health.argonaut.api.datatypes.Timing;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.DispenseRequest;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.DosageInstruction;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.Status;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwDuration;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDispenseRequest;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction.CdwRoute;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder.CdwDosageInstructions.CdwDosageInstruction.CdwTiming;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import gov.va.dvp.cdw.xsd.model.CdwSimpleQuantity;
import java.util.Collections;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

public class MedicationOrderTransformerTest {

  MedicationOrderTransformer tx = new MedicationOrderTransformer();
  private CdwSampleData cdw = new CdwSampleData();
  private Expected expected = new Expected();

  @Test
  public void additionalInstructions() {
    assertThat(tx.additionalInstructions(cdw.additionalInstructions()))
        .isEqualTo(expected.additionalInstructions());
    assertThat(tx.additionalInstructions(null)).isNull();
  }

  @Test
  public void dispenseRequest() {
    assertThat(tx.dispenseRequest(cdw.dispenseRequest())).isEqualTo(expected.dispenseRequest());
    assertThat(tx.dispenseRequest(null)).isNull();
  }

  @Test
  public void dosageInstructions() {
    assertThat(tx.dosageInstruction(null)).isNull();
    assertThat(tx.dosageInstruction(new CdwDosageInstructions())).isNull();
    assertThat(tx.dosageInstruction(cdw.dosageInstructions()))
        .isEqualTo(expected.dosageInstructions());
  }

  @Test
  public void doseQuantity() {
    assertThat(tx.doseQuantity(null)).isNull();
    assertThat(tx.doseQuantity(new CdwSimpleQuantity())).isNull();
    assertThat(tx.doseQuantity(cdw.doseQuantity())).isEqualTo(expected.doseQuantity());
  }

  @Test
  public void doseQuantityValue() {
    assertThat(tx.doseQuantityValue(null)).isNull();
    assertThat(tx.doseQuantityValue("")).isNull();
    assertThat(tx.doseQuantityValue(cdw.doseQuantity().getValue()))
        .isEqualTo(expected.doseQuantity().value());
  }

  @Test
  public void medicationOrder() {
    assertThat(tx.apply(cdw.medicationOrder())).isEqualTo(expected.medicationOrder());
  }

  @Test
  public void quantity() {
    assertThat(tx.quantity(cdw.dispenseRequest().getQuantity())).isEqualTo(expected.quantity());
  }

  @Test
  public void timingCode() {
    assertThat(tx.timingCode(cdw.timingCode())).isEqualTo(expected.timingCode());
    assertThat(tx.timingCode(null)).isNull();
  }

  private static class CdwSampleData {

    CdwCodeableConcept additionalInstructions() {
      CdwCodeableConcept additionalInstructions = new CdwCodeableConcept();
      additionalInstructions.setText("additional instructions text");
      return additionalInstructions;
    }

    private CdwReference cdwReference(String prefix) {
      CdwReference ref = new CdwReference();
      ref.setReference(prefix + " reference");
      ref.setDisplay(prefix + " display");
      return ref;
    }

    @SneakyThrows
    XMLGregorianCalendar dateEnded() {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-07-21T19:03:16Z");
    }

    @SneakyThrows
    private XMLGregorianCalendar dateWritten() {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-06-21T19:03:16Z");
    }

    CdwDispenseRequest dispenseRequest() {
      CdwDispenseRequest dispenseRequest = new CdwDispenseRequest();
      dispenseRequest.setExpectedSupplyDuration(expectedSupplyDuration());
      dispenseRequest.setNumberOfRepeatsAllowed(10);
      dispenseRequest.setQuantity("10");
      return dispenseRequest;
    }

    CdwDosageInstruction dosageInstruction() {
      CdwDosageInstruction dosageInstruction = new CdwDosageInstruction();
      dosageInstruction.setText("dosage instruction text");
      dosageInstruction.setAdditionalInstructions(additionalInstructions());
      dosageInstruction.setTiming(timing());
      dosageInstruction.setAsNeededBoolean("true");
      dosageInstruction.setRoute(route());
      dosageInstruction.setDoseQuantity(doseQuantity());
      return dosageInstruction;
    }

    CdwDosageInstructions dosageInstructions() {
      CdwDosageInstructions dosageInstructions = new CdwDosageInstructions();
      dosageInstructions.getDosageInstruction().add(dosageInstruction());
      return dosageInstructions;
    }

    CdwSimpleQuantity doseQuantity() {
      CdwSimpleQuantity doseQuantity = new CdwSimpleQuantity();
      doseQuantity.setValue("10");
      return doseQuantity;
    }

    CdwDuration expectedSupplyDuration() {
      CdwDuration expectedSupplyDuration = new CdwDuration();
      expectedSupplyDuration.setCode("expected supply duration code");
      expectedSupplyDuration.setSystem("http://example.com");
      expectedSupplyDuration.setUnit("expected supply duration unit");
      expectedSupplyDuration.setValue(10);
      return expectedSupplyDuration;
    }

    CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder medicationOrder() {
      CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder sampleMedicationOrder =
          new CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder();
      sampleMedicationOrder.setRowNumber(1);
      sampleMedicationOrder.setCdwId("1234");
      sampleMedicationOrder.setPatient(cdwReference("patient"));
      sampleMedicationOrder.setDateWritten(dateWritten());
      sampleMedicationOrder.setStatus("active");
      sampleMedicationOrder.setDateEnded(dateEnded());
      sampleMedicationOrder.setPrescriber(cdwReference("prescriber"));
      sampleMedicationOrder.setMedicationReference(cdwReference("medication"));
      sampleMedicationOrder.setDosageInstructions(dosageInstructions());
      sampleMedicationOrder.setDispenseRequest(dispenseRequest());
      return sampleMedicationOrder;
    }

    CdwRoute route() {
      CdwRoute route = new CdwRoute();
      route.setText("route text");
      return route;
    }

    CdwTiming timing() {
      CdwTiming timing = new CdwTiming();
      timing.setCode(timingCode());
      return timing;
    }

    CdwCodeableConcept timingCode() {
      CdwCodeableConcept timingCode = new CdwCodeableConcept();
      timingCode.setText("timing code text");
      return timingCode;
    }
  }

  @NoArgsConstructor(staticName = "get")
  private static class Expected {

    CodeableConcept additionalInstructions() {
      return CodeableConcept.builder().text("additional instructions text").build();
    }

    DispenseRequest dispenseRequest() {
      return DispenseRequest.builder()
          .expectedSupplyDuration(expectedSupplyDuration())
          .numberOfRepeatsAllowed(10)
          .quantity(quantity())
          .build();
    }

    private DosageInstruction dosageInstruction() {
      return DosageInstruction.builder()
          .text("dosage instruction text")
          .additionalInstructions(additionalInstructions())
          .timing(timing())
          .asNeededBoolean(true)
          .route(route())
          .doseQuantity(doseQuantity())
          .build();
    }

    private List<DosageInstruction> dosageInstructions() {
      return Collections.singletonList(dosageInstruction());
    }

    private SimpleQuantity doseQuantity() {
      return SimpleQuantity.builder().value(Double.valueOf(10)).build();
    }

    Duration expectedSupplyDuration() {
      return Duration.builder()
          .code("expected supply duration code")
          .system("http://example.com")
          .unit("expected supply duration unit")
          .value(Double.valueOf(10))
          .build();
    }

    MedicationOrder medicationOrder() {
      return MedicationOrder.builder()
          .resourceType("Medication Order")
          .id("1234")
          .patient(reference("patient"))
          .dateWritten("2013-06-21T19:03:16Z")
          .status(Status.active)
          .dateEnded("2013-07-21T19:03:16Z")
          .prescriber(reference("prescriber"))
          .medicationReference(reference("medication"))
          .dosageInstruction(dosageInstructions())
          .dispenseRequest(dispenseRequest())
          .build();
    }

    SimpleQuantity quantity() {
      return SimpleQuantity.builder().value(Double.valueOf("10")).build();
    }

    private Reference reference(String prefix) {
      return Reference.builder()
          .reference(prefix + " reference")
          .display(prefix + " display")
          .build();
    }

    private CodeableConcept route() {
      return CodeableConcept.builder().text("route text").build();
    }

    private Timing timing() {
      return Timing.builder().code(timingCode()).build();
    }

    private CodeableConcept timingCode() {
      return CodeableConcept.builder().text("timing code text").build();
    }
  }
}
