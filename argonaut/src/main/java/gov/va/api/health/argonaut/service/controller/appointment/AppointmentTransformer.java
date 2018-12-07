package gov.va.api.health.argonaut.service.controller.appointment;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.asInteger;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Appointment;
import gov.va.api.health.argonaut.api.resources.Appointment.Participant;
import gov.va.api.health.argonaut.api.resources.Appointment.Participant.ParticipantStatus;
import gov.va.api.health.argonaut.api.resources.Appointment.Participant.RequiredCode;
import gov.va.api.health.argonaut.api.resources.Appointment.Status;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwAppointment101Root.CdwAppointments.CdwAppointment;
import gov.va.dvp.cdw.xsd.model.CdwAppointment101Root.CdwAppointments.CdwAppointment.CdwParticipants;
import gov.va.dvp.cdw.xsd.model.CdwAppointment101Root.CdwAppointments.CdwAppointment.CdwParticipants.CdwParticipant;
import gov.va.dvp.cdw.xsd.model.CdwAppointment101Root.CdwAppointments.CdwAppointment.CdwParticipants.CdwParticipant.CdwTypes;
import gov.va.dvp.cdw.xsd.model.CdwAppointmentParticipantRequired;
import gov.va.dvp.cdw.xsd.model.CdwAppointmentParticipantStatus;
import gov.va.dvp.cdw.xsd.model.CdwAppointmentParticipantType;
import gov.va.dvp.cdw.xsd.model.CdwAppointmentParticipantTypeCode;
import gov.va.dvp.cdw.xsd.model.CdwAppointmentParticipantTypeCoding;
import gov.va.dvp.cdw.xsd.model.CdwAppointmentParticipantTypeDisplay;
import gov.va.dvp.cdw.xsd.model.CdwAppointmentStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AppointmentTransformer implements AppointmentController.Transformer {

  @Override
  public Appointment apply(CdwAppointment cdw) {

    return Appointment.builder()
        .resourceType("Appointment")
        .id(cdw.getCdwId())
        .status(status(cdw.getStatus()))
        .description(cdw.getDescription())
        .start(asDateTimeString(cdw.getStart()))
        .end(asDateTimeString(cdw.getEnd()))
        .minutesDuration(asInteger(cdw.getMinutesDuration()))
        .comment(cdw.getComment())
        .participant(participants(cdw.getParticipants()))
        .build();
  }

  Participant participant(CdwParticipant cdw) {
    return convert(
        cdw,
        source ->
            Participant.builder()
                .actor(reference(cdw.getActor()))
                .required(required(cdw.getRequired()))
                .status(participantStatus(cdw.getStatus()))
                .type(types(cdw.getTypes()))
                .build());
  }

  ParticipantStatus participantStatus(CdwAppointmentParticipantStatus cdw) {
    return convert(cdw, source -> EnumSearcher.of(ParticipantStatus.class).find(source.value()));
  }

  List<Participant> participants(CdwParticipants cdw) {
    return convertAll(ifPresent(cdw, CdwParticipants::getParticipant), this::participant);
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

  RequiredCode required(CdwAppointmentParticipantRequired cdw) {
    return convert(cdw, source -> EnumSearcher.of(RequiredCode.class).find(source.value()));
  }

  Status status(CdwAppointmentStatus source) {
    return EnumSearcher.of(Appointment.Status.class).find(source.value());
  }

  CodeableConcept type(CdwAppointmentParticipantType cdw) {
    if (cdw == null || (cdw.getText() == null && cdw.getCoding().isEmpty())) {
      return null;
    }
    return convert(
        cdw,
        source ->
            CodeableConcept.builder()
                .text(source.getText())
                .coding(convertAll(source.getCoding(), this::typeCoding))
                .build());
  }

  private Coding typeCoding(CdwAppointmentParticipantTypeCoding cdw) {
    return convert(
        cdw,
        source ->
            Coding.builder()
                .system(source.getSystem())
                .code(convert(source.getCode(), CdwAppointmentParticipantTypeCode::value))
                .display(convert(source.getDisplay(), CdwAppointmentParticipantTypeDisplay::value))
                .build());
  }

  List<CodeableConcept> types(CdwTypes cdw) {
    return convertAll(ifPresent(cdw, CdwTypes::getType), this::type);
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
