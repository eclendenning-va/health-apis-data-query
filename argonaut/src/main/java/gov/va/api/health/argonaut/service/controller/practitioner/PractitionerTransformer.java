package gov.va.api.health.argonaut.service.controller.practitioner;

import static gov.va.api.health.argonaut.service.controller.Transformers.allNull;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.Address;
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
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwName;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwPractitionerRoles;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwPractitionerRoles.CdwPractitionerRole;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwPractitionerRoles.CdwPractitionerRole.CdwHealthcareServices;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwPractitionerRoles.CdwPractitionerRole.CdwLocations;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root.CdwPractitioners.CdwPractitioner.CdwTelecoms;
import gov.va.dvp.cdw.xsd.model.CdwPractitionerRoleCoding;
import gov.va.dvp.cdw.xsd.model.CdwPractitionerRoleCoding.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.util.Collections;
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
    if (optionalSource == null || optionalSource.getAddress().isEmpty()) {
      return null;
    }
    return convertAll(
        ifPresent(optionalSource, CdwAddresses::getAddress),
        cdw ->
            Address.builder()
                .line(cdw.getLines().getLine())
                .city(cdw.getCity())
                .state(cdw.getState())
                .postalCode(cdw.getPostalCode())
                .build());
  }

  List<Reference> healthcareService(CdwHealthcareServices source) {
    if (source == null
        || source.getHealthcareService() == null
        || source.getHealthcareService().isEmpty()) {
      return null;
    }
    return convertAll(
        ifPresent(source, CdwHealthcareServices::getHealthcareService),
        cdw -> Reference.builder().display(cdw.getDisplay()).reference(cdw.getReference()).build());
  }

  List<Reference> locations(CdwLocations source) {
    if (source == null || source.getLocation().isEmpty() || source.getLocation() == null) {
      return null;
    }
    return convertAll(
        ifPresent(source, CdwLocations::getLocation),
        cdw -> Reference.builder().display(cdw.getDisplay()).reference(cdw.getReference()).build());
  }

  Reference managingOrganization(CdwReference source) {
    if (source == null || allNull(source.getReference(), source.getDisplay())) {
      return null;
    }
    return Reference.builder()
        .reference(source.getReference())
        .display(source.getDisplay())
        .build();
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
    return HumanName.builder()
        .use(ifPresent(source.getUse(), use -> HumanName.NameUse.valueOf(use.value())))
        .text(source.getText())
        .family(Collections.singletonList(source.getFamily()))
        .given(Collections.singletonList(source.getGiven()))
        .prefix(Collections.singletonList(source.getPrefix()))
        .suffix(Collections.singletonList(source.getSuffix()))
        .build();
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
    return CodeableConcept.builder().coding(coding(source.getCoding())).build();
  }

  List<Coding> coding(CdwCoding source) {
    if (source == null) {
      return null;
    }
    return Collections.singletonList(
        Coding.builder()
            .code(ifPresent(source.getCode(), code -> String.valueOf(code.value())))
            .build());
  }

  List<ContactPoint> telecoms(CdwTelecoms optionalSource) {
    if (optionalSource == null || optionalSource.getTelecom().isEmpty()) {
      return null;
    }
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
