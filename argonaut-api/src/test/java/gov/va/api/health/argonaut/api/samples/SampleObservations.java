package gov.va.api.health.argonaut.api.samples;

import static java.util.Collections.singletonList;

import com.sun.org.apache.bcel.internal.classfile.Code;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.Identifier;
import gov.va.api.health.argonaut.api.datatypes.Identifier.IdentifierUse;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.Observation.ObservationReferenceRange;
import gov.va.api.health.argonaut.api.resources.Observation.ObservationRelated;
import gov.va.api.health.argonaut.api.resources.Observation.Status;
import gov.va.api.health.argonaut.api.resources.Observation.Type;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

/**
 * This class provides data structures that are populated with dummy values, suitable for testing
 * serialization.
 */
@SuppressWarnings("WeakerAccess")
@NoArgsConstructor(staticName = "get")
public class SampleObservations {

  @Delegate SampleDataTypes dataTypes = SampleDataTypes.get();

  public Observation observation() {
    return Observation.builder()
        .id("1234")
        .resourceType("Observation")
        .meta(meta())
        .implicitRules("http://HelloRules.com")
        .language("Hello Language")
        .text(narrative())
        .contained(singletonList(resource()))
        .extension(Arrays.asList(extension(), extension()))
        .modifierExtension(
            Arrays.asList(extension(), extensionWithQuantity(), extensionWithRatio()))
        .identifier(singletonList(identifier()))
        .status(Status.registered)
        .category(codeableConcept())
        .code(codeableConcept())
        .subject(reference())
        .encounter(reference())
        .effectiveDateTime()
        .issued()
        .performer()
        .valueCodeableConcept()
        .dataAbsentReason()
        .interpretation()
        .comments()
        .bodySite()
        .method()
        .specimen()
        .device()
        .referenceRange(referenceRange())
        .related()
        .build();
  }

  public CodeableConcept category() {
    return CodeableConcept.builder()
        .coding(observationCategoryCoding())
        .build();
  }

  public List<Coding> observationCategoryCoding() {
    Coding coding = Coding.builder()
        .system("http://hl7.org/fhir/observation-category")
        .code("laboratory")
        .build();
    return Collections.singletonList(coding());
  }

  public ObservationReferenceRange referenceRange() {
    return ObservationReferenceRange.builder()
        .id("0000")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .low(simpleQuantity())
        .high(simpleQuantity())
        .meaning(codeableConcept())
        .age(range())
        .text("HelloText")
        .build();
  }

  public ObservationRelated related() {
    return ObservationRelated.builder()
        .id("0000")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .type(Type.has_member)
        .target(reference())
        .build();
  }
}
