package gov.va.api.health.argonaut.service.controller.practitioner;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;
import static java.util.Collections.singletonList;

import gov.va.api.health.argonaut.api.datatypes.Address;
import gov.va.api.health.argonaut.api.datatypes.Address.AddressUse;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint;
import gov.va.api.health.argonaut.api.datatypes.HumanName;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Practitioner;
import gov.va.api.health.argonaut.api.resources.Practitioner.Gender;
import gov.va.api.health.argonaut.api.resources.Practitioner.PractitionerRole;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwAddresses;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwAddresses.CdwAddress.CdwLines;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwName;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwPractitionerRoles;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwPractitionerRoles.CdwPractitionerRole;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwPractitionerRoles.CdwPractitionerRole.CdwHealthcareServices;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwPractitionerRoles.CdwPractitionerRole.CdwLocations;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwTelecoms;
import gov.va.dvp.cdw.xsd.model.CdwPractitionerRoleCoding;
import gov.va.dvp.cdw.xsd.model.CdwPractitionerRoleCoding.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PractitionerTransformer implements PractitionerController.Transformer {

  @Override
  public Practitioner apply(CdwPractitioner source) {
    return practitioner(source);
  }

  List<Address> addresses(CdwAddresses optionalSource) {
    return convertAll(
        ifPresent(optionalSource, CdwAddresses::getAddress),
        cdw ->
            Address.builder()
                .line(addressLines(cdw.getLines()))
                .city(cdw.getCity())
                .state(cdw.getState())
                .postalCode(cdw.getPostalCode())
                .use(ifPresent(cdw.getUse(), use -> AddressUse.valueOf(use.value())))
                .build());
  }

  List<String> addressLines(CdwLines source) {
    if (source == null || source.getLine().isEmpty()) {
      return null;
    }
    return source.getLine();
  }

  List<Reference> healthcareService(CdwHealthcareServices source) {
    return convertAll(
        ifPresent(source, CdwHealthcareServices::getHealthcareService),
        cdw -> Reference.builder().display(cdw.getDisplay()).reference(cdw.getReference()).build());
  }

  List<Reference> locations(CdwLocations source) {
    return convertAll(
        ifPresent(source, CdwLocations::getLocation),
        cdw -> Reference.builder().display(cdw.getDisplay()).reference(cdw.getReference()).build());
  }

  Reference managingOrganization(CdwReference source) {
    if (source == null || allNull(source.getDisplay(), source.getReference())) {
      return null;
    }
    return convert(
        source,
        cdw -> Reference.builder().reference(cdw.getReference()).display(cdw.getDisplay()).build());
  }

  HumanName name(CdwName source) {
    if (source == null
        || allNull(
            source.getFamily(),
            source.getGiven(),
            source.getPrefix(),
            source.getSuffix(),
            source.getText(),
            source.getUse())) {
      return null;
    }
    return convert(
        source,
        cdw ->
            HumanName.builder()
                .use(ifPresent(cdw.getUse(), use -> HumanName.NameUse.valueOf(use.value())))
                .text(cdw.getText())
                .family(nameList(cdw.getFamily()))
                .given(nameList(cdw.getGiven()))
                .suffix(nameList(cdw.getSuffix()))
                .prefix(nameList(cdw.getPrefix()))
                .build());
  }

  List<String> nameList(String source) {
    if (source == null) {
      return null;
    }
    return singletonList(source);
  }

  private Practitioner practitioner(CdwPractitioner source) {
    return Practitioner.builder()
        .id(source.getCdwId())
        .resourceType("Practitioner")
        .active(source.isActive())
        .name(name(source.getName()))
        .telecom(telecoms(source.getTelecoms()))
        .address(addresses(source.getAddresses()))
        .gender(ifPresent(source.getGender(), gender -> Gender.valueOf(gender.value())))
        .birthDate(asDateTimeString(source.getBirthDate()))
        .practitionerRole(practitionerRoles(source.getPractitionerRoles()))
        .build();
  }

  List<PractitionerRole> practitionerRoles(CdwPractitionerRoles source) {
    if (source == null || source.getPractitionerRole() == null) {
      return null;
    }
    List<PractitionerRole> practitionerRoles =
        source
            .getPractitionerRole()
            .stream()
            .map(this::practitionerRole)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    if (practitionerRoles.isEmpty()) {
      return null;
    }
    return practitionerRoles;
  }

  PractitionerRole practitionerRole(CdwPractitionerRole source) {
    if (source == null
        || allNull(
            source.getHealthcareServices(),
            source.getLocations(),
            source.getManagingOrganization(),
            source.getRole())) {
      return null;
    }
    return PractitionerRole.builder()
        .healthcareService(healthcareService(source.getHealthcareServices()))
        .location(locations(source.getLocations()))
        .managingOrganization(managingOrganization(source.getManagingOrganization()))
        .role(role(source.getRole()))
        .build();
  }

  CodeableConcept role(CdwPractitionerRoleCoding source) {
    if (source == null || source.getCoding() == null) {
      return null;
    }
    return CodeableConcept.builder().coding(roleCoding(source.getCoding())).build();
  }

  List<Coding> roleCoding(CdwCoding source) {
    if (source == null || allNull(source.getSystem(), source.getDisplay(), source.getCode())) {
      return null;
    }
    return convert(
        source,
        cdw ->
            singletonList(
                Coding.builder()
                    .code(ifPresent(cdw.getCode(), code -> String.valueOf(code.value())))
                    .display(
                        ifPresent(cdw.getDisplay(), display -> String.valueOf(display.value())))
                    .system(cdw.getSystem())
                    .build()));
  }

  List<ContactPoint> telecoms(CdwTelecoms optionalSource) {
    return convertAll(
        ifPresent(optionalSource, CdwTelecoms::getTelecom),
        cdw ->
            ContactPoint.builder()
                .system(
                    ifPresent(
                        cdw.getSystem(),
                        system -> ContactPoint.ContactPointSystem.valueOf(system.value())))
                .value(cdw.getValue())
                .use(
                    ifPresent(
                        cdw.getUse(), use -> ContactPoint.ContactPointUse.valueOf(use.value())))
                .build());
  }
}
