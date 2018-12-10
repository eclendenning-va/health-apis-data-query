package gov.va.api.health.argonaut.service.controller.organization;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.Address;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint.ContactPointSystem;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint.ContactPointUse;
import gov.va.api.health.argonaut.api.resources.Organization;
import gov.va.dvp.cdw.xsd.model.CdwOrganization100Root.CdwOrganizations.CdwOrganization;
import gov.va.dvp.cdw.xsd.model.CdwOrganization100Root.CdwOrganizations.CdwOrganization.CdwAddresses;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationAddress;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationTelecom;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationType;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationType.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationTypeCode;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationTypeDisplay;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrganizationTransformer implements OrganizationController.Transformer {

  @Override
  public Organization apply(CdwOrganization organization) {
    return organization(organization);
  }

  Organization organization(CdwOrganization source) {
    return Organization.builder()
        .resourceType("Organization")
        .id(source.getCdwId())
        .active(source.isActive())
        .type(type(source.getType()))
        .name(source.getName())
        .telecom(telecoms(source.getTelecoms().getTelecom()))
        .address(addresses(source.getAddresses()))
        .build();
  }

  List<Address> addresses(CdwAddresses optionalSource) {
    return convertAll(
        ifPresent(optionalSource, CdwAddresses::getAddress),
        cdw ->
            Address.builder()
                .line(addressLine(cdw))
                .city(cdw.getCity())
                .state(cdw.getState())
                .postalCode(cdw.getPostalCode())
                .build());
  }

  List<String> addressLine(CdwOrganizationAddress source) {
    if (source == null) {
      return null;
    }
    if (source.getLines() == null) {
      return null;
    }
    if (source.getLines().getLine() == null) {
      return null;
    }
    return allNull(source.getLines().getLine()) ? null : source.getLines().getLine();
  }

  List<ContactPoint> telecoms(List<CdwOrganizationTelecom> optionalSource) {
    if (optionalSource == null || optionalSource.isEmpty()) {
      return null;
    }
    List<ContactPoint> contactPoints =
        optionalSource
            .stream()
            .map(this::telecom)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    if (contactPoints.isEmpty()) {
      return null;
    }
    return contactPoints;
  }

  ContactPoint telecom(CdwOrganizationTelecom cdw) {
    if (allNull(cdw.getSystem(), cdw.getUse(), cdw.getValue())) {
      return null;
    }
    return ContactPoint.builder()
        .system(telecomSystem(cdw.getSystem()))
        .value(cdw.getValue())
        .use(ifPresent(cdw.getUse(), use -> ContactPointUse.valueOf(use.value())))
        .build();
  }

  ContactPointSystem telecomSystem(String system) {
    return (system == null) ? null : ContactPointSystem.phone;
  }

  CodeableConcept type(CdwOrganizationType optionalSource) {
    if (optionalSource == null || optionalSource.getCoding() == null) {
      return null;
    }
    return convert(
        optionalSource.getCoding(), cdw -> CodeableConcept.builder().coding(codings(cdw)).build());
  }

  List<Coding> codings(CdwCoding optionalSource) {
    Coding coding = convert(optionalSource, this::coding);
    if (coding == null) {
      return null;
    }
    return Collections.singletonList(coding);
  }

  Coding coding(CdwCoding cdw) {
    if (allNull(cdw.getCode(), cdw.getDisplay(), cdw.getSystem())) {
      return null;
    }
    return Coding.builder()
        .system(cdw.getSystem())
        .code(convert(cdw.getCode(), CdwOrganizationTypeCode::value))
        .display(convert(cdw.getDisplay(), CdwOrganizationTypeDisplay::value))
        .build();
  }
}
