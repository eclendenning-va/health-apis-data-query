package gov.va.api.health.argonaut.api.samples;

import static java.util.Collections.singletonList;

import gov.va.api.health.argonaut.api.datatypes.Timing;
import gov.va.api.health.argonaut.api.datatypes.Timing.EventTiming;
import gov.va.api.health.argonaut.api.datatypes.Timing.Repeat;
import gov.va.api.health.argonaut.api.datatypes.Timing.UnitsOfTime;
import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.DispenseRequest;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.DosageInstruction;
import gov.va.api.health.argonaut.api.resources.MedicationOrder.Substitution;
import java.util.LinkedList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@SuppressWarnings("WeakerAccess")
@NoArgsConstructor(staticName = "get")
public class SampleMedicationOrders {
  @Delegate SampleDataTypes dataTypes = SampleDataTypes.get();

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
        .status("active")
        .dateEnded("2000-10-01")
        .reasonEnded(codeableConcept())
        .patient(reference())
        .prescriber(reference())
        .reasonCodeableConcept(codeableConcept())
        .note("Hello Note")
        .medicationReference(reference())
        .dosageInstruction(singletonList(dosageInstruction()))
        .dispenseRequest(dispenseRequest())
        .substitution(substitution())
        .priorPrescription(reference())
        .build();
  }

  public DosageInstruction dosageInstruction() {
    return DosageInstruction.builder()
        .id("2222")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .text("Hello Text")
        .additionalInstructions(codeableConcept())
        .timing(timing())
        .asNeededBoolean(true)
        .siteCodeableConcept(codeableConcept())
        .route(codeableConcept())
        .method(codeableConcept())
        .doseQuantity(simpleQuantity())
        .rateRange(range())
        .maxDosePerDay(ratio())
        .build();
  }

  public DispenseRequest dispenseRequest() {
    return DispenseRequest.builder()
        .id("2222")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .medicationCodeableConcept(codeableConcept())
        .validityPeriod(period())
        .numberOfRepeatsAllowed(10)
        .quantity(simpleQuantity())
        .expectedSupplyDuration(duration())
        .build();
  }

  public Repeat repeat() {
    return Repeat.builder()
        .id("2222")
        .extension(singletonList(extension()))
        .boundsQuantity(duration())
        .count(1)
        .duration(11.11)
        .durationMax(11.11)
        .durationUnits(UnitsOfTime.min)
        .frequency(1)
        .frequencyMax(1)
        .period(11.11)
        .periodMax(11.11)
        .periodUnits(UnitsOfTime.h)
        .when(EventTiming.AC)
        .build();
  }

  public Substitution substitution() {
    return Substitution.builder()
        .id("2222")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .type(codeableConcept())
        .reason(codeableConcept())
        .build();
  }

  public Timing timing() {
    List<String> events = new LinkedList<>();
    String event = "2015-04-15T04:00:00Z";
    events.add(event);
    return Timing.builder()
        .id("2222")
        .extension(singletonList(extension()))
        .event(events)
        .repeat(repeat())
        .code(codeableConcept())
        .build();
  }
}
