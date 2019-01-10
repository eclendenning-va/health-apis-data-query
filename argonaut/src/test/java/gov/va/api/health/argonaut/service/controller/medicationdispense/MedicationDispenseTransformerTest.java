package gov.va.api.health.argonaut.service.controller.medicationdispense;

import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.MedicationDispense;
import gov.va.api.health.argonaut.api.resources.MedicationDispense.Status;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispenseStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MedicationDispenseTransformerTest {

  private final MedicationDispenseTransformer tx = new MedicationDispenseTransformer();
  private final CdwSampleData cdw = CdwSampleData.get();
  private final Expected expected = Expected.get();

  @Test
  public void medicationDispense() {
    assertThat(tx.apply(cdw.medicationDispense())).isEqualTo(expected.medicationDispense());
  }

  @Test
  public void status() {
    assertThat(tx.status(CdwMedicationDispenseStatus.COMPLETED)).isEqualTo(Status.completed);
    assertThat(tx.status(CdwMedicationDispenseStatus.ENTERED_IN_ERROR))
        .isEqualTo(Status.entered_in_error);
    assertThat(tx.status(CdwMedicationDispenseStatus.IN_PROGRESS)).isEqualTo(Status.in_progress);
    assertThat(tx.status(CdwMedicationDispenseStatus.ON_HOLD)).isEqualTo(Status.on_hold);
    assertThat(tx.status(CdwMedicationDispenseStatus.STOPPED)).isEqualTo(Status.stopped);
  }

  @Test
  public void reference() {
    assertThat(tx.reference(null)).isNull();
    assertThat(tx.reference(new CdwReference())).isNull();
    assertThat(
            tx.reference(
                cdw.reference(
                    "https://www.freedomstream.io/CDCArgonaut/api/Patient/185601V825290",
                    "VETERAN,JOHN Q")))
        .isEqualTo(
            expected.reference(
                "https://www.freedomstream.io/CDCArgonaut/api/Patient/185601V825290",
                "VETERAN,JOHN Q"));
  }

  @NoArgsConstructor(staticName = "get", access = AccessLevel.PUBLIC)
  static class CdwSampleData {
    CdwMedicationDispense medicationDispense() {
      CdwMedicationDispense cdw = new CdwMedicationDispense();
      cdw.setCdwId("1200738474343:R");
      cdw.setStatus(CdwMedicationDispenseStatus.COMPLETED);
      cdw.setPatient(patient());
      cdw.setDispenser(dispenser());
      return cdw;
    }

    private CdwReference reference(String ref, String display) {
      CdwReference cdw = new CdwReference();
      cdw.setReference(ref);
      cdw.setDisplay(display);
      return cdw;
    }

    private CdwReference patient() {
      return reference(
          "https://www.freedomstream.io/CDCArgonaut/api/Patient/185601V825290", "VETERAN,JOHN Q");
    }

    private CdwReference dispenser() {
      return reference(
          "https://www.freedomstream.io/CDCArgonaut/api/Practitioner/5e27c469-82e4-5725-babb-49cf7eee948f",
          "BONES,ATTENDING C");
    }
  }

  @NoArgsConstructor(staticName = "get")
  private static class Expected {
    MedicationDispense medicationDispense() {
      return MedicationDispense.builder()
          .resourceType("MedicationDispense")
          .id("1200738474343:R")
          .status(MedicationDispense.Status.completed)
          .patient(patient())
          .dispenser(dispenser())
          .build();
    }

    private Reference reference(String ref, String display) {
      return Reference.builder().reference(ref).display(display).build();
    }

    private Reference patient() {
      return reference(
          "https://www.freedomstream.io/CDCArgonaut/api/Patient/185601V825290", "VETERAN,JOHN Q");
    }

    private Reference dispenser() {
      return reference(
          "https://www.freedomstream.io/CDCArgonaut/api/Practitioner/5e27c469-82e4-5725-babb-49cf7eee948f",
          "BONES,ATTENDING C");
    }
  }
}
