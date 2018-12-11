package gov.va.api.health.argonaut.service.controller.location;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;

import gov.va.api.health.argonaut.api.datatypes.Address;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint.ContactPointSystem;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Location;
import gov.va.api.health.argonaut.api.resources.Location.Mode;
import gov.va.api.health.argonaut.api.resources.Location.Status;
import gov.va.api.health.argonaut.service.controller.EnumSearcher;
import gov.va.dvp.cdw.xsd.model.CdwLocation100Root.CdwLocations.CdwLocation;
import gov.va.dvp.cdw.xsd.model.CdwLocation100Root.CdwLocations.CdwLocation.CdwTelecoms;
import gov.va.dvp.cdw.xsd.model.CdwLocationAddress;
import gov.va.dvp.cdw.xsd.model.CdwLocationMode;
import gov.va.dvp.cdw.xsd.model.CdwLocationPhysicalType;
import gov.va.dvp.cdw.xsd.model.CdwLocationStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class LocationTransformer implements LocationController.Transformer {

  @Override
  public Location apply(CdwLocation location) {
    return location(location);
  }

  private Location location(CdwLocation source) {
    return Location.builder()
        .resourceType("Location")
        .address(address(source.getAddress()))
        .id(source.getCdwId())
        .description(source.getDescription())
        .managingOrganization(reference(source.getManagingOrganization()))
        .mode(mode(source.getMode()))
        .name(source.getName())
        .physicalType(locationPhysicalType(source.getPhysicalType()))
        .status(status(source.getStatus()))
        .telecom(telecoms(source.getTelecoms()))
        .type(locationType(source.getType()))
        .build();
  }

  Address address(CdwLocationAddress maybeCdw) {
    if (isUnusableAddress(maybeCdw)) {
      return null;
    }
    return convert(
        maybeCdw,
        source ->
            Address.builder()
                .city(source.getCity())
                .line(source.getLine())
                .postalCode(source.getPostalCode())
                .state(source.getState())
                .build());
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

  Mode mode(CdwLocationMode maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    return EnumSearcher.of(Mode.class).find(maybeCdw.value());
  }

  Status status(CdwLocationStatus maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    return EnumSearcher.of(Status.class).find(maybeCdw.value());
  }

  ContactPointSystem contactPointCode(String maybeCdw) {
    if (StringUtils.isBlank(maybeCdw)) {
      return null;
    }
    return EnumSearcher.of(ContactPointSystem.class).find(maybeCdw);
  }

  CodeableConcept locationPhysicalType(CdwLocationPhysicalType maybeCdw) {
    if (maybeCdw == null || allNull(maybeCdw.getCoding(), maybeCdw.getText())) {
      return null;
    }
    return convert(
        maybeCdw,
        source ->
            CodeableConcept.builder()
                .coding(locationPhysicalTypeCoding(source.getCoding()))
                .text(source.getText())
                .build());
  }

  List<Coding> locationTypeCoding(List<CdwLocation.CdwType.CdwCoding> maybeCdw) {
    return convertAll(
        maybeCdw,
        source ->
            Coding.builder()
                .system(source.getSystem())
                .code(source.getCode().value())
                .display(source.getDisplay().value())
                .build());
  }

  List<Coding> locationPhysicalTypeCoding(CdwLocationPhysicalType.CdwCoding maybeCdw) {
    if (maybeCdw == null
        || allNull(maybeCdw.getCode(), maybeCdw.getDisplay(), maybeCdw.getSystem())) {
      return null;
    }
    return convert(
        maybeCdw,
        cdw ->
            Collections.singletonList(
                Coding.builder()
                    .code(cdw.getCode().value())
                    .system(cdw.getSystem())
                    .display(cdw.getDisplay().value())
                    .build()));
  }

  List<ContactPoint> telecoms(CdwTelecoms maybeCdw) {
    if (maybeCdw == null) {
      return null;
    }
    return convertAll(
        maybeCdw.getTelecom(),
        source ->
            ContactPoint.builder()
                .system(contactPointCode(source.getSystem()))
                .value(source.getValue())
                .build());
  }

  CodeableConcept locationType(CdwLocation.CdwType maybeCdw) {
    if (maybeCdw == null || maybeCdw.getCoding().isEmpty()) {
      return null;
    }
    return convert(
        maybeCdw,
        source -> CodeableConcept.builder().coding(locationTypeCoding(source.getCoding())).build());
  }

  Boolean isUnusableAddress(CdwLocationAddress maybeCdw) {
    return (maybeCdw == null
        || allNull(
            maybeCdw.getLine(), maybeCdw.getCity(), maybeCdw.getPostalCode(), maybeCdw.getState())
        || maybeCdw.getLine().isEmpty());
  }
}
