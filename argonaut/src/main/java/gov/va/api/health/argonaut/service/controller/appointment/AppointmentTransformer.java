package gov.va.api.health.argonaut.service.controller.appointment;

import gov.va.api.health.argonaut.api.resources.Appointment;
import gov.va.api.health.argonaut.api.resources.Appointment.Status;
import gov.va.dvp.cdw.xsd.model.CdwAppointment101Root.CdwAppointments.CdwAppointment;
import gov.va.dvp.cdw.xsd.model.CdwAppointmentStatus;
import org.springframework.stereotype.Service;

@Service
public class AppointmentTransformer implements AppointmentController.Transformer {

  @Override
  public Appointment apply(CdwAppointment cdw) {

    return Appointment.builder()
        .resourceType("Appointment")
        .id(cdw.getCdwId())
        //        .status(status(cdw.getStatus()))
        //        .category(category(cdw.getCategory()))
        //        .code(code(cdw.getCode()))
        //        .subject(reference(cdw.getSubject()))
        //        .encounter(reference(cdw.getEncounter()))
        //        .effectiveDateTime(asDateTimeString(cdw.getEffectiveDateTime()))
        //        .issued(asDateTimeString(cdw.getIssued()))
        //        .performer(performers(cdw.getPerformers()))
        //        .valueQuantity(valueQuantity(cdw.getValueQuantity()))
        //        .valueCodeableConcept(valueCodeableConcept(cdw.getValueCodeableConcept()))
        //        .interpretation(interpretation(cdw.getInterpretation()))
        //        .comments(cdw.getComments())
        //        // INTENTIONALLY OMITTED .specimen()
        //        .referenceRange(referenceRanges(cdw.getReferenceRanges()))
        //        .component(components(cdw.getComponents()))
        .build();
  }

  Status status(CdwAppointmentStatus source) {
    return null;
  }

  //  CodeableConcept category(CdwCategory maybeCdw) {
  //    if (maybeCdw == null || maybeCdw.getCoding().isEmpty()) {
  //      return null;
  //    }
  //    return CodeableConcept.builder()
  //        .coding(
  //            convertAll(
  //                maybeCdw.getCoding(),
  //                source ->
  //                    coding(
  //                        source.getSystem().value(),
  //                        source.getCode().value(),
  //                        source.getDisplay().value())))
  //        .build();
  //  }
  //
  //  CodeableConcept code(CdwCode maybeCdw) {
  //    if (maybeCdw == null) {
  //      return null;
  //    }
  //    if (maybeCdw.getText() == null && maybeCdw.getCoding().isEmpty()) {
  //      return null;
  //    }
  //    return CodeableConcept.builder()
  //        .text(maybeCdw.getText())
  //        .coding(
  //            convertAll(
  //                maybeCdw.getCoding(),
  //                source -> coding(source.getSystem(), source.getCode(), source.getDisplay())))
  //        .build();
  //  }
  //
  //  private Coding coding(String system, String code, String display) {
  //    return Coding.builder().system(system).code(code).display(display).build();
  //  }
  //
  //  AppointmentComponent component(CdwComponent maybeCdw) {
  //    if (maybeCdw == null
  //        || allNull(
  //            maybeCdw.getCode(),
  //            maybeCdw.getId(),
  //            maybeCdw.getValueCodeableConcept(),
  //            maybeCdw.getValueQuantity())) {
  //      return null;
  //    }
  //    return convert(
  //        maybeCdw,
  //        source ->
  //            AppointmentComponent.builder()
  //                .id(source.getId())
  //                .code(componentCode(source.getCode()))
  //                .valueCodeableConcept(
  //                    componentValueCodeableConcept(maybeCdw.getValueCodeableConcept()))
  //                .valueQuantity(componentValueQuantity(maybeCdw.getValueQuantity()))
  //                .build());
  //  }
  //
  //  CodeableConcept componentCode(CdwComponent.CdwCode maybeCdw) {
  //    if (maybeCdw == null) {
  //      return null;
  //    }
  //    if (StringUtils.isBlank(maybeCdw.getText()) && maybeCdw.getCoding().isEmpty()) {
  //      return null;
  //    }
  //    return CodeableConcept.builder()
  //        .text(maybeCdw.getText())
  //        .coding(
  //            convertAll(
  //                maybeCdw.getCoding(),
  //                source -> coding(source.getSystem(), source.getCode(), source.getDisplay())))
  //        .build();
  //  }
  //
  //  CodeableConcept componentValueCodeableConcept(CdwComponent.CdwValueCodeableConcept maybeCdw) {
  //    if (maybeCdw == null) {
  //      return null;
  //    }
  //    if (StringUtils.isBlank(maybeCdw.getText()) && maybeCdw.getCoding().isEmpty()) {
  //      return null;
  //    }
  //    return CodeableConcept.builder()
  //        .text(maybeCdw.getText())
  //        .coding(
  //            convertAll(
  //                maybeCdw.getCoding(),
  //                source -> coding(source.getSystem(), source.getCode(), source.getDisplay())))
  //        .build();
  //  }
  //
  //  Quantity componentValueQuantity(CdwComponent.CdwValueQuantity maybeCdw) {
  //    if (maybeCdw == null
  //        || allNull(
  //            maybeCdw.getCode(),
  //            maybeCdw.getComparator(),
  //            maybeCdw.getSystem(),
  //            maybeCdw.getUnit(),
  //            maybeCdw.getValue())) {
  //      return null;
  //    }
  //    return Quantity.builder()
  //        .code(maybeCdw.getCode())
  //        .comparator(maybeCdw.getComparator())
  //        .system(maybeCdw.getSystem())
  //        .unit(maybeCdw.getUnit())
  //        .value(ifPresent(maybeCdw.getValue(), BigDecimal::doubleValue))
  //        .build();
  //  }
  //
  //  List<AppointmentComponent> components(CdwComponents maybeCdw) {
  //    return convertAll(ifPresent(maybeCdw, CdwComponents::getComponent), this::component);
  //  }
  //
  //  CodeableConcept interpretation(CdwInterpretation maybeCdw) {
  //    return ifPresent(
  //        maybeCdw,
  //        source ->
  //            CodeableConcept.builder()
  //                .text(source.getText())
  //                .coding(
  //                    convertAll(
  //                        source.getCoding(),
  //                        coding ->
  //                            coding(
  //                                coding.getSystem().value(), coding.getCode(),
  // coding.getDisplay())))
  //                .build());
  //  }
  //
  //  List<Reference> performers(CdwPerformers maybeCdw) {
  //    return convertAll(ifPresent(maybeCdw, CdwPerformers::getPerformer), this::reference);
  //  }
  //
  //  Reference reference(CdwReference maybeCdw) {
  //    return convert(
  //        maybeCdw,
  //        source ->
  //            Reference.builder()
  //                .reference(source.getReference())
  //                .display(source.getDisplay())
  //                .build());
  //  }
  //
  //  AppointmentReferenceRange referenceRange(CdwReferenceRange maybeCdw) {
  //    if (maybeCdw == null || allNull(maybeCdw.getLow(), maybeCdw.getHigh())) {
  //      return null;
  //    }
  //    return AppointmentReferenceRange.builder()
  //        .low(referenceRangeQuantity(maybeCdw.getLow()))
  //        .high(referenceRangeQuantity(maybeCdw.getHigh()))
  //        .build();
  //  }
  //
  //  SimpleQuantity referenceRangeQuantity(CdwAppointmentRefRangeQuantity maybeCdw) {
  //    if (maybeCdw == null
  //        || allNull(
  //            maybeCdw.getSystem(), maybeCdw.getCode(), maybeCdw.getUnit(), maybeCdw.getValue()))
  // {
  //      return null;
  //    }
  //    return SimpleQuantity.builder()
  //        .system(maybeCdw.getSystem())
  //        .value(ifPresent(maybeCdw.getValue(), BigDecimal::doubleValue))
  //        .unit(maybeCdw.getUnit())
  //        .code(maybeCdw.getCode())
  //        .build();
  //  }
  //
  //  List<AppointmentReferenceRange> referenceRanges(CdwReferenceRanges maybeCdw) {
  //    return convertAll(
  //        ifPresent(maybeCdw, CdwReferenceRanges::getReferenceRange), this::referenceRange);
  //  }
  //
  //  Status status(@NotNull CdwAppointmentStatus cdw) {
  //    return EnumSearcher.of(Status.class).find(cdw.value());
  //  }
  //
  //  CodeableConcept valueCodeableConcept(CdwValueCodeableConcept maybeCdw) {
  //    return ifPresent(
  //        maybeCdw,
  //        source ->
  //            CodeableConcept.builder()
  //                .text(source.getText())
  //                .coding(
  //                    convertAll(
  //                        source.getCoding(),
  //                        x -> coding(x.getSystem(), x.getCode(), x.getDisplay())))
  //                .build());
  //  }
  //
  //  Quantity valueQuantity(CdwValueQuantity maybeCdw) {
  //    return ifPresent(
  //        maybeCdw,
  //        cdw ->
  //            Quantity.builder()
  //                .system(cdw.getSystem())
  //                .value(cdw.getValue().doubleValue())
  //                .comparator(cdw.getComparator())
  //                .code(cdw.getCode())
  //                .unit(cdw.getUnit())
  //                .build());
  //  }
}
