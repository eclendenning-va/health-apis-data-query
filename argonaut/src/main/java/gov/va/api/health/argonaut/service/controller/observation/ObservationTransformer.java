package gov.va.api.health.argonaut.service.controller.observation;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.Observation.Status;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwCategory;
import gov.va.dvp.cdw.xsd.model.CdwObservation104Root.CdwObservations.CdwObservation.CdwCode;
import gov.va.dvp.cdw.xsd.model.CdwObservationStatus;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class ObservationTransformer implements ObservationController.Transformer {

  @Override
  public Observation apply(CdwObservation cdw) {
    // TODO DO NOT MAP SPECIMEN
    return Observation.builder()
        .resourceType("Observation")
        .id(cdw.getCdwId())
        .status(status(cdw.getStatus()))
        .category(category(cdw.getCategory()))
        .build();
  }

  CodeableConcept category(@Nullable CdwCategory cdw) {
    return null;
  }

  Status code(CdwCode cdw) {
    return null;
  }

  Status status(@NotNull CdwObservationStatus cdw) {
    // TODO use searcher
    return Status.amended;
  }
}
