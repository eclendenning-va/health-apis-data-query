package gov.va.api.health.argonaut.api.samples;

import static java.util.Collections.singletonList;

import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.DosageInstruction;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.Status;
import lombok.experimental.Delegate;

public class SampleMedicationOrders {
  @Delegate
  SampleDataTypes dataTypes = SampleDataTypes.get();

  public MedicationOrder medicationOrder() {
    return MedicationOrder.builder()
        .resourceType("MedicationOrder")
        .id("2222")
        .meta(meta())
        .implicitRules("http://HelloRules.com")
        .language("Hello Language")
        .text(narrative())
        .contained(singletonList(resource()))
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .identifier(singletonList(identifier()))
        .dateWritten("2000-10-01")
        .status(Status.on_hold)
        .dateEnded("2000-10-01")
        .reasonEnded(codeableConcept())
        .patient(reference())
        .prescriber(reference())
        .reasonCodeableConcept(codeableConcept())
        .note("Hello Note")
        .medicationReference(reference())
        .build();
  }

  public DosageInstruction dosageInstruction() {
    return DosageInstruction.builder()
        .id("2222")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .text("Hello Text")
        .additionalInstructions(codeableConcept())
        .build();
  }
}
