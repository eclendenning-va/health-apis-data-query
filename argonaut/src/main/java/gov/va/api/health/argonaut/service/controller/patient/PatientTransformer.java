package gov.va.api.health.argonaut.service.controller.patient;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateString;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.datatypes.Address;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint;
import gov.va.api.health.argonaut.api.datatypes.HumanName;
import gov.va.api.health.argonaut.api.datatypes.Identifier;
import gov.va.api.health.argonaut.api.elements.Extension;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Patient;
import gov.va.api.health.argonaut.api.resources.Patient.Contact;
import gov.va.dvp.cdw.xsd.model.CdwBirthSexCodes;
import gov.va.dvp.cdw.xsd.model.CdwBirthsexExtension;
import gov.va.dvp.cdw.xsd.model.CdwExtensions;
import gov.va.dvp.cdw.xsd.model.CdwExtensions.CdwExtension;
import gov.va.dvp.cdw.xsd.model.CdwExtensions.CdwExtension.CdwValueCoding;
import gov.va.dvp.cdw.xsd.model.CdwMaritalStatusCodes;
import gov.va.dvp.cdw.xsd.model.CdwMaritalStatusSystems;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwAddresses;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwAddresses.CdwAddress;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwContacts;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwContacts.CdwContact;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwContacts.CdwContact.CdwRelationship;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwIdentifier;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwIdentifier.CdwAssigner;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwMaritalStatus;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwName;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwTelecoms;
import gov.va.dvp.cdw.xsd.model.CdwPatientContactRelationshipCodes;
import gov.va.dvp.cdw.xsd.model.CdwPatientContactRelationshipSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PatientTransformer implements PatientController.Transformer {

  Address address(CdwContact contact) {
    if (contact == null) {
      return null;
    }
    return Address.builder()
        .line(addressLine(contact))
        .city(contact.getCity())
        .state(contact.getState())
        .country(contact.getCountry())
        .postalCode(contact.getPostalCode())
        .build();
  }

  List<String> addressLine(CdwContact contact) {
    List<String> line = new LinkedList<>();
    if (StringUtils.isNotBlank(contact.getStreetAddress1())) {
      line.add(contact.getStreetAddress1());
    }
    if (StringUtils.isNotBlank(contact.getStreetAddress2())) {
      line.add(contact.getStreetAddress2());
    }
    if (StringUtils.isNotBlank(contact.getStreetAddress3())) {
      line.add(contact.getStreetAddress3());
    }
    return line.isEmpty() ? null : line;
  }

  List<String> addressLine(CdwAddress address) {
    List<String> line = new LinkedList<>();
    if (StringUtils.isNotBlank(address.getStreetAddress1())) {
      line.add(address.getStreetAddress1());
    }
    if (StringUtils.isNotBlank(address.getStreetAddress2())) {
      line.add(address.getStreetAddress2());
    }
    if (StringUtils.isNotBlank(address.getStreetAddress3())) {
      line.add(address.getStreetAddress3());
    }
    return line.isEmpty() ? null : line;
  }

  List<Address> addresses(CdwAddresses addresses) {
    if (addresses == null) {
      return Collections.emptyList();
    }
    List<Address> argoAddresses = new LinkedList<>();
    for (CdwAddress address : addresses.getAddress()) {
      argoAddresses.add(
          Address.builder()
              .line(addressLine(address))
              .city(address.getCity())
              .state(address.getState())
              .postalCode(address.getPostalCode())
              .build());
    }
    return argoAddresses;
  }

  @Override
  public Patient apply(CdwPatient patient) {

    return patient(patient);
  }

  Optional<Extension> argoBirthSex(CdwBirthsexExtension argoBirthsex) {
    if (argoBirthsex == null) {
      return Optional.empty();
    }
    return Optional.of(
        Extension.builder()
            .url(argoBirthsex.getUrl())
            .valueCode(ifPresent(argoBirthsex.getValueCode(), CdwBirthSexCodes::value))
            .build());
  }

  Optional<Extension> argoEthnicity(List<CdwExtensions> argoEthnicity) {
    if (argoEthnicity.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        Extension.builder()
            .url(argoEthnicity.get(0).getUrl())
            .extension(argonautExtensions(argoEthnicity.get(0).getExtension()))
            .build());
  }

  Optional<Extension> argoRace(List<CdwExtensions> argoRace) {
    if (argoRace.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        Extension.builder()
            .url(argoRace.get(0).getUrl())
            .extension(argonautExtensions(argoRace.get(0).getExtension()))
            .build());
  }

  List<Extension> argonautExtensions(List<CdwExtension> argonautExtensions) {
    List<Extension> extensions = new LinkedList<>();
    for (CdwExtension extension : argonautExtensions) {
      if ("text".equals(extension.getUrl())) {
        extensions.add(
            Extension.builder()
                .url(extension.getUrl())
                .valueString(extension.getValueString())
                .build());
      } else {
        extensions.add(
            Extension.builder()
                .url(extension.getUrl())
                .valueCoding(valueCoding(extension.getValueCoding()))
                .build());
      }
    }
    return extensions;
  }

  List<ContactPoint> contact(CdwContact contact) {
    if (contact == null || StringUtils.isBlank(contact.getPhone())) {
      return Collections.emptyList();
    }
    return Collections.singletonList(
        ContactPoint.builder()
            .system(ContactPoint.ContactPointSystem.phone)
            .value(contact.getPhone())
            .build());
  }

  List<CodeableConcept> contactRelationship(CdwRelationship relationship) {
    return Collections.singletonList(
        CodeableConcept.builder()
            .coding(contactRelationshipCoding(relationship.getCoding()))
            .text(relationship.getText())
            .build());
  }

  List<Coding> contactRelationshipCoding(CdwRelationship.CdwCoding relationship) {
    return Collections.singletonList(
        Coding.builder()
            .system(ifPresent(relationship.getSystem(), CdwPatientContactRelationshipSystem::value))
            .code(ifPresent(relationship.getCode(), CdwPatientContactRelationshipCodes::value))
            .display(relationship.getDisplay())
            .build());
  }

  List<Contact> contacts(CdwContacts contacts) {
    if (contacts == null) {
      return null;
    }
    List<Contact> argoContacts = new LinkedList<>();
    for (CdwContact contact : contacts.getContact()) {
      argoContacts.add(
          Contact.builder()
              .relationship(contactRelationship(contact.getRelationship()))
              .name(humanName(contact.getName()))
              .telecom(contact(contact))
              .address(address(contact))
              .build());
    }

    return argoContacts;
  }

  List<Extension> extensions(
      Optional<Extension> race, Optional<Extension> ethnicity, Optional<Extension> birthSex) {
    List<Extension> extensions = new ArrayList<>(3);
    race.ifPresent(extensions::add);
    ethnicity.ifPresent(extensions::add);
    birthSex.ifPresent(extensions::add);
    return extensions;
  }

  HumanName humanName(String name) {
    if (name == null) {
      return null;
    }
    return HumanName.builder().text(name).build();
  }

  Reference identifierAssigner(CdwAssigner assigner) {
    return Reference.builder().display(assigner.getDisplay()).build();
  }

  CodeableConcept identifierType(CdwIdentifier.CdwType type) {
    return CodeableConcept.builder().coding(identifierTypeCodings(type.getCoding())).build();
  }

  List<Coding> identifierTypeCodings(List<CdwIdentifier.CdwType.CdwCoding> codings) {
    List<Coding> argoCodings = new LinkedList<>();
    for (CdwIdentifier.CdwType.CdwCoding coding : codings) {
      argoCodings.add(Coding.builder().system(coding.getSystem()).code(coding.getCode()).build());
    }
    return argoCodings;
  }

  Identifier.IdentifierUse identifierUse(CdwIdentifier identifier) {
    return ifPresent(identifier.getUse(), use -> Identifier.IdentifierUse.valueOf(use.value()));
  }

  List<Identifier> identifiers(List<CdwIdentifier> identifiers) {
    List<Identifier> argoIdentifiers = new LinkedList<>();
    for (CdwIdentifier identifier : identifiers) {
      argoIdentifiers.add(
          Identifier.builder()
              .use(identifierUse(identifier))
              .type(identifierType(identifier.getType()))
              .system(identifier.getSystem())
              .value(identifier.getValue())
              .assigner(identifierAssigner(identifier.getAssigner()))
              .build());
    }
    return argoIdentifiers;
  }

  CodeableConcept maritalStatus(CdwMaritalStatus maritalStatus) {
    if (maritalStatus == null) {
      return null;
    }
    return CodeableConcept.builder()
        .text(maritalStatus.getText())
        .coding(maritalStatusCoding(maritalStatus.getCoding()))
        .build();
  }

  List<Coding> maritalStatusCoding(List<CdwMaritalStatus.CdwCoding> codings) {
    List<Coding> argoCodings = new LinkedList<>();
    for (CdwMaritalStatus.CdwCoding coding : codings) {
      argoCodings.add(
          Coding.builder()
              .system(ifPresent(coding.getSystem(), CdwMaritalStatusSystems::value))
              .code(ifPresent(coding.getCode(), CdwMaritalStatusCodes::value))
              .display(coding.getDisplay())
              .build());
    }
    return argoCodings;
  }

  List<HumanName> names(CdwName name) {
    if (name == null) {
      return Collections.emptyList();
    }
    return Collections.singletonList(
        HumanName.builder()
            .use(ifPresent(name.getUse(), HumanName.NameUse::valueOf))
            .text(name.getText())
            .family(Collections.singletonList(name.getFamily()))
            .given(Collections.singletonList(name.getGiven()))
            .build());
  }

  private Patient patient(CdwPatient patient) {
    return Patient.builder()
        .id(patient.getCdwId())
        .resourceType("Patient")
        .extension(
            extensions(
                argoRace(patient.getArgoRace()),
                argoEthnicity(patient.getArgoEthnicity()),
                argoBirthSex(patient.getArgoBirthsex())))
        .identifier(identifiers(patient.getIdentifier()))
        .name(names(patient.getName()))
        .telecom(telecoms(patient.getTelecoms()))
        .address(addresses(patient.getAddresses()))
        .gender(ifPresent(patient.getGender(), gender -> Patient.Gender.valueOf(gender.value())))
        .birthDate(asDateString(patient.getBirthDate()))
        .deceasedBoolean(patient.isDeceasedBoolean())
        .deceasedDateTime(asDateTimeString(patient.getDeceasedDateTime()))
        .maritalStatus(maritalStatus(patient.getMaritalStatus()))
        .contact(contacts(patient.getContacts()))
        .build();
  }

  List<ContactPoint> telecoms(CdwTelecoms telecoms) {
    if (telecoms == null) {
      return Collections.emptyList();
    }
    List<ContactPoint> contactPoints = new LinkedList<>();
    for (CdwTelecoms.CdwTelecom telecom : telecoms.getTelecom()) {
      contactPoints.add(
          ContactPoint.builder()
              .system(
                  ifPresent(
                      telecom.getSystem(),
                      system -> ContactPoint.ContactPointSystem.valueOf(system.value())))
              .value(telecom.getValue())
              .use(
                  ifPresent(
                      telecom.getUse(), use -> ContactPoint.ContactPointUse.valueOf(use.value())))
              .build());
    }
    return contactPoints;
  }

  Coding valueCoding(CdwValueCoding valueCoding) {
    return Coding.builder()
        .display(valueCoding.getDisplay())
        .code(valueCoding.getCode())
        .system(valueCoding.getSystem())
        .build();
  }
}
