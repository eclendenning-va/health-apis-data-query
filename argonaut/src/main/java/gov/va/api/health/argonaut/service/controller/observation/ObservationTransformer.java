package gov.va.api.health.argonaut.service.controller.observation;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.Quantity;
import gov.va.api.health.argonaut.api.datatypes.SimpleQuantity;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.Observation.ObservationComponent;
import gov.va.api.health.argonaut.api.resources.Observation.ObservationReferenceRange;
import gov.va.api.health.argonaut.api.resources.Observation.Status;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwCategory;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwCode;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwComponents;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwComponents.CdwComponent;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwInterpretation;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwPerformers;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwReferenceRanges;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwReferenceRanges.CdwReferenceRange;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwValueCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwValueQuantity;
import gov.va.dvp.cdw.xsd.model.CdwObservationRefRangeQuantity;
import gov.va.dvp.cdw.xsd.model.CdwObservationStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ObservationTransformer implements ObservationController.Transformer {

  @Override
  public Observation apply(CdwObservation cdw) {
    /*
     * Specimen reference is omitted since we do not support the a specimen resource and
     * do not want dead links
     */
    return Observation.builder()
        .resourceType("Observation")
        .id(cdw.getCdwId())
        .status(status(cdw.getStatus()))
        .category(category(cdw.getCategory()))
        .code(code(cdw.getCode()))
        .subject(reference(cdw.getSubject()))
        .encounter(reference(cdw.getEncounter()))
        .effectiveDateTime(asDateTimeString(cdw.getEffectiveDateTime()))
        .issued(asDateTimeString(cdw.getIssued()))
        .performer(performers(cdw.getPerformers()))
        .valueQuantity(valueQuantity(cdw.getValueQuantity()))
        .valueCodeableConcept(valueCodeableConcept(cdw.getValueCodeableConcept()))
        .interpretation(interpretation(cdw.getInterpretation()))
        .comments(cdw.getComments())
        // INTENTIONALLY OMITTED .specimen()
        .referenceRange(referenceRanges(cdw.getReferenceRanges()))
        .component(components(cdw.getComponents()))
        .build();
  }

  CodeableConcept category(CdwCategory maybeCdw) {
    if (maybeCdw == null || maybeCdw.getCoding().isEmpty()) {
      return null;
    }
    return CodeableConcept.builder()
        .coding(
            convertAll(
                maybeCdw.getCoding(),
                source ->
                    coding(
                        source.getSystem().value(),
                        source.getCode().value(),
                        source.getDisplay().value())))
        .build();
  }

  CodeableConcept code(CdwCode maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    if (maybeCdw.getText() == null && maybeCdw.getCoding().isEmpty()) {
      return null;
    }
    return CodeableConcept.builder()
        .text(maybeCdw.getText())
        .coding(
            convertAll(
                maybeCdw.getCoding(),
                source -> coding(source.getSystem(), source.getCode(), source.getDisplay())))
        .build();
  }

  private Coding coding(String system, String code, String display) {
    return Coding.builder().system(system).code(code).display(display).build();
  }

  ObservationComponent component(CdwComponent maybeCdw) {
    if (maybeCdw == null
        || allNull(
            maybeCdw.getCode(),
            maybeCdw.getId(),
            maybeCdw.getValueCodeableConcept(),
            maybeCdw.getValueQuantity())) {
      return null;
    }
    return convert(
        maybeCdw,
        source ->
            ObservationComponent.builder()
                .id(source.getId())
                .code(componentCode(source.getCode()))
                .valueCodeableConcept(
                    componentValueCodeableConcept(maybeCdw.getValueCodeableConcept()))
                .valueQuantity(componentValueQuantity(maybeCdw.getValueQuantity()))
                .build());
  }

  CodeableConcept componentCode(CdwComponent.CdwCode maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    if (StringUtils.isBlank(maybeCdw.getText()) && maybeCdw.getCoding().isEmpty()) {
      return null;
    }
    return CodeableConcept.builder()
        .text(maybeCdw.getText())
        .coding(
            convertAll(
                maybeCdw.getCoding(),
                source -> coding(source.getSystem(), source.getCode(), source.getDisplay())))
        .build();
  }

  CodeableConcept componentValueCodeableConcept(CdwComponent.CdwValueCodeableConcept maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    if (StringUtils.isBlank(maybeCdw.getText()) && maybeCdw.getCoding().isEmpty()) {
      return null;
    }
    return CodeableConcept.builder()
        .text(maybeCdw.getText())
        .coding(
            convertAll(
                maybeCdw.getCoding(),
                source -> coding(source.getSystem(), source.getCode(), source.getDisplay())))
        .build();
  }

  Quantity componentValueQuantity(CdwComponent.CdwValueQuantity maybeCdw) {
    if (maybeCdw == null
        || allNull(
            maybeCdw.getCode(),
            maybeCdw.getComparator(),
            maybeCdw.getSystem(),
            maybeCdw.getUnit(),
            maybeCdw.getValue())) {
      return null;
    }
    return Quantity.builder()
        .code(maybeCdw.getCode())
        .comparator(maybeCdw.getComparator())
        .system(maybeCdw.getSystem())
        .unit(maybeCdw.getUnit())
        .value(ifPresent(maybeCdw.getValue(), BigDecimal::doubleValue))
        .build();
  }

  List<ObservationComponent> components(CdwComponents maybeCdw) {
    return convertAll(ifPresent(maybeCdw, CdwComponents::getComponent), this::component);
  }

  CodeableConcept interpretation(CdwInterpretation maybeCdw) {
    return ifPresent(
        maybeCdw,
        source ->
            CodeableConcept.builder()
                .text(source.getText())
                .coding(
                    convertAll(
                        source.getCoding(),
                        coding ->
                            coding(
                                coding.getSystem().value(), coding.getCode(), coding.getDisplay())))
                .build());
  }

  List<Reference> performers(CdwPerformers maybeCdw) {
    return convertAll(ifPresent(maybeCdw, CdwPerformers::getPerformer), this::reference);
  }

  Reference reference(CdwReference maybeCdw) {
    return convert(
        maybeCdw,
        source ->
            Reference.builder()
                .reference(source.getReference())
                .display(source.getDisplay())
                .build());
  }

  ObservationReferenceRange referenceRange(CdwReferenceRange maybeCdw) {
    if (maybeCdw == null || allNull(maybeCdw.getLow(), maybeCdw.getHigh())) {
      return null;
    }
    return ObservationReferenceRange.builder()
        .low(referenceRangeQuantity(maybeCdw.getLow()))
        .high(referenceRangeQuantity(maybeCdw.getHigh()))
        .build();
  }

  SimpleQuantity referenceRangeQuantity(CdwObservationRefRangeQuantity maybeCdw) {
    if (maybeCdw == null
        || allNull(
            maybeCdw.getSystem(), maybeCdw.getCode(), maybeCdw.getUnit(), maybeCdw.getValue())) {
      return null;
    }
    return SimpleQuantity.builder()
        .system(maybeCdw.getSystem())
        .value(ifPresent(maybeCdw.getValue(), BigDecimal::doubleValue))
        .unit(maybeCdw.getUnit())
        .code(maybeCdw.getCode())
        .build();
  }

  List<ObservationReferenceRange> referenceRanges(CdwReferenceRanges maybeCdw) {
    return convertAll(
        ifPresent(maybeCdw, CdwReferenceRanges::getReferenceRange), this::referenceRange);
  }

  Status status(@NotNull CdwObservationStatus cdw) {
    return EnumSearcher.of(Status.class).find(cdw.value());
  }

  CodeableConcept valueCodeableConcept(CdwValueCodeableConcept maybeCdw) {
    return ifPresent(
        maybeCdw,
        source ->
            CodeableConcept.builder()
                .text(source.getText())
                .coding(
                    convertAll(
                        source.getCoding(),
                        x -> coding(x.getSystem(), x.getCode(), x.getDisplay())))
                .build());
  }

  Quantity valueQuantity(CdwValueQuantity maybeCdw) {
    return ifPresent(
        maybeCdw,
        cdw ->
            Quantity.builder()
                .system(cdw.getSystem())
                .value(cdw.getValue().doubleValue())
                .comparator(cdw.getComparator())
                .code(cdw.getCode())
                .unit(cdw.getUnit())
                .build());
  }
}
