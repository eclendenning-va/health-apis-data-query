package gov.va.api.health.argonaut.service.controller.patient;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateString;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;

import gov.va.api.health.argonaut.api.Address;
import gov.va.api.health.argonaut.api.CodeableConcept;
import gov.va.api.health.argonaut.api.Coding;
import gov.va.api.health.argonaut.api.Contact;
import gov.va.api.health.argonaut.api.ContactPoint;
import gov.va.api.health.argonaut.api.Extension;
import gov.va.api.health.argonaut.api.HumanName;
import gov.va.api.health.argonaut.api.Identifier;
import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.api.Reference;
import gov.va.dvp.cdw.xsd.pojos.BirthSexCodes;
import gov.va.dvp.cdw.xsd.pojos.BirthsexExtension;
import gov.va.dvp.cdw.xsd.pojos.Extensions;
import gov.va.dvp.cdw.xsd.pojos.MaritalStatusCodes;
import gov.va.dvp.cdw.xsd.pojos.MaritalStatusSystems;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import gov.va.dvp.cdw.xsd.pojos.PatientContactRelationshipCodes;
import gov.va.dvp.cdw.xsd.pojos.PatientContactRelationshipSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PatientTransformer implements PatientController.Transformer {

  Address address(Patient103Root.Patients.Patient.Contacts.Contact contact) {
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

  List<String> addressLine(Patient103Root.Patients.Patient.Contacts.Contact contact) {
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

  List<String> addressLine(Patient103Root.Patients.Patient.Addresses.Address address) {
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

  List<Address> addresses(Patient103Root.Patients.Patient.Addresses addresses) {
    if (addresses == null) {
      return Collections.emptyList();
    }
    List<Address> argoAddresses = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Addresses.Address address : addresses.getAddress()) {
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
  public Patient apply(Patient103Root.Patients.Patient patient) {

    return patient(patient);
  }

  Optional<Extension> argoBirthSex(BirthsexExtension argoBirthsex) {
    if (argoBirthsex == null) {
      return Optional.empty();
    }
    return Optional.of(
        Extension.builder()
            .url(argoBirthsex.getUrl())
            .valueCode(ifPresent(argoBirthsex.getValueCode(), BirthSexCodes::value))
            .build());
  }

  Optional<Extension> argoEthnicity(List<Extensions> argoEthnicity) {
    if (argoEthnicity.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        Extension.builder()
            .url(argoEthnicity.get(0).getUrl())
            .extension(argonautExtensions(argoEthnicity.get(0).getExtension()))
            .build());
  }

  Optional<Extension> argoRace(List<Extensions> argoRace) {
    if (argoRace.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        Extension.builder()
            .url(argoRace.get(0).getUrl())
            .extension(argonautExtensions(argoRace.get(0).getExtension()))
            .build());
  }

  List<Extension> argonautExtensions(List<Extensions.Extension> argonautExtensions) {
    List<Extension> extensions = new LinkedList<>();
    for (Extensions.Extension extension : argonautExtensions) {
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

  List<ContactPoint> contact(Patient103Root.Patients.Patient.Contacts.Contact contact) {
    if (contact == null || StringUtils.isBlank(contact.getPhone())) {
      return Collections.emptyList();
    }
    return Collections.singletonList(
        ContactPoint.builder()
            .system(ContactPoint.ContactPointSystem.phone)
            .value(contact.getPhone())
            .build());
  }

  List<CodeableConcept> contactRelationship(
      Patient103Root.Patients.Patient.Contacts.Contact.Relationship relationship) {
    return Collections.singletonList(
        CodeableConcept.builder()
            .coding(contactRelationshipCoding(relationship.getCoding()))
            .text(relationship.getText())
            .build());
  }

  List<Coding> contactRelationshipCoding(
      Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding relationship) {
    return Collections.singletonList(
        Coding.builder()
            .system(ifPresent(relationship.getSystem(), PatientContactRelationshipSystem::value))
            .code(ifPresent(relationship.getCode(), PatientContactRelationshipCodes::value))
            .display(relationship.getDisplay())
            .build());
  }

  List<Contact> contacts(Patient103Root.Patients.Patient.Contacts contacts) {
    if (contacts == null) {
      return null;
    }
    List<Contact> argoContacts = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Contacts.Contact contact : contacts.getContact()) {
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

  Reference identifierAssigner(Patient103Root.Patients.Patient.Identifier.Assigner assigner) {
    return Reference.builder().display(assigner.getDisplay()).build();
  }

  CodeableConcept identifierType(Patient103Root.Patients.Patient.Identifier.Type type) {
    return CodeableConcept.builder().coding(identifierTypeCodings(type.getCoding())).build();
  }

  List<Coding> identifierTypeCodings(
      List<Patient103Root.Patients.Patient.Identifier.Type.Coding> codings) {
    List<Coding> argoCodings = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Identifier.Type.Coding coding : codings) {
      argoCodings.add(Coding.builder().system(coding.getSystem()).code(coding.getCode()).build());
    }
    return argoCodings;
  }

  Identifier.IdentifierUse identifierUse(Patient103Root.Patients.Patient.Identifier identifier) {
    return ifPresent(identifier.getUse(), use -> Identifier.IdentifierUse.valueOf(use.value()));
  }

  List<Identifier> identifiers(List<Patient103Root.Patients.Patient.Identifier> identifiers) {
    List<Identifier> argoIdentifiers = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Identifier identifier : identifiers) {
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

  CodeableConcept maritalStatus(Patient103Root.Patients.Patient.MaritalStatus maritalStatus) {
    if (maritalStatus == null) {
      return null;
    }
    return CodeableConcept.builder()
        .text(maritalStatus.getText())
        .coding(maritalStatusCoding(maritalStatus.getCoding()))
        .build();
  }

  List<Coding> maritalStatusCoding(
      List<Patient103Root.Patients.Patient.MaritalStatus.Coding> codings) {
    List<Coding> argoCodings = new LinkedList<>();
    for (Patient103Root.Patients.Patient.MaritalStatus.Coding coding : codings) {
      argoCodings.add(
          Coding.builder()
              .system(ifPresent(coding.getSystem(), MaritalStatusSystems::value))
              .code(ifPresent(coding.getCode(), MaritalStatusCodes::value))
              .display(coding.getDisplay())
              .build());
    }
    return argoCodings;
  }

  List<HumanName> names(Patient103Root.Patients.Patient.Name name) {
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

  private Patient patient(Patient103Root.Patients.Patient patient) {
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

  List<ContactPoint> telecoms(Patient103Root.Patients.Patient.Telecoms telecoms) {
    if (telecoms == null) {
      return Collections.emptyList();
    }
    List<ContactPoint> contactPoints = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Telecoms.Telecom telecom : telecoms.getTelecom()) {
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

  Coding valueCoding(Extensions.Extension.ValueCoding valueCoding) {
    return Coding.builder()
        .display(valueCoding.getDisplay())
        .code(valueCoding.getCode())
        .system(valueCoding.getSystem())
        .build();
  }
}
