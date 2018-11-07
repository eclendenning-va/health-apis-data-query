package gov.va.api.health.argonaut.service.controller.patient;

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
import gov.va.dvp.cdw.xsd.pojos.BirthsexExtension;
import gov.va.dvp.cdw.xsd.pojos.Extensions;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PatientTransformer implements PatientController.Transformer {

  Address address(Patient103Root.Patients.Patient.Contacts.Contact contact) {
    return Address.builder()
        .line(getLine(contact))
        .city(contact.getCity())
        .state(contact.getState())
        .country(contact.getCountry())
        .postalCode(contact.getPostalCode())
        .build();
  }

  List<Address> addresses(Patient103Root.Patients.Patient.Addresses addresses) {
    List<Address> argoAddresses = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Addresses.Address address : addresses.getAddress()) {
      argoAddresses.add(
          Address.builder()
              .line(getLine(address))
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
            .valueCode(argoBirthsex.getValueCode().name())
            .build());
  }

  Optional<Extension> argoEthnicity(List<Extensions> argoEthnicity) {
    if (argoEthnicity.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        Extension.builder()
            .url(argoEthnicity.get(0).getUrl())
            .extension(ethnicityExtensions(argoEthnicity.get(0).getExtension()))
            .build());
  }

  Optional<Extension> argoRace(List<Extensions> argoRace) {
    if (argoRace.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        Extension.builder()
            .url(argoRace.get(0).getUrl())
            .extension(raceExtensions(argoRace.get(0).getExtension()))
            .build());
  }

  CodeableConcept codeableConcept(Patient103Root.Patients.Patient.Identifier.Type type) {
    return CodeableConcept.builder().coding(codings(type.getCoding())).build();
  }

  List<Coding> coding(
      Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding relationship) {
    return Collections.singletonList(
        Coding.builder()
            .system(relationship.getSystem().value())
            .code(relationship.getCode().value())
            .display(relationship.getDisplay())
            .build());
  }

  List<Coding> codings(List<Patient103Root.Patients.Patient.Identifier.Type.Coding> codings) {
    List<Coding> argoCodings = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Identifier.Type.Coding coding : codings) {
      argoCodings.add(Coding.builder().system(coding.getSystem()).code(coding.getCode()).build());
    }
    return argoCodings;
  }

  List<Contact> contacts(Patient103Root.Patients.Patient.Contacts contacts) {
    List<Contact> argoContacts = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Contacts.Contact contact : contacts.getContact()) {
      argoContacts.add(
          Contact.builder()
              .relationship(relationship(contact.getRelationship()))
              .name(humanName(contact.getName()))
              .telecom(telecom(contact))
              .address(address(contact))
              .build());
    }

    return argoContacts;
  }

  String deceasedDateTime(XMLGregorianCalendar deceasedDateTime) {
    if (deceasedDateTime == null) {
      return null;
    }
    return deceasedDateTime.toString();
  }

  List<Extension> ethnicityExtensions(List<Extensions.Extension> argoEthnicity) {
    List<Extension> extensions = new LinkedList<>();
    for (Extensions.Extension extension : argoEthnicity) {
      if (extension.getUrl().equals("text")) {
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

  List<Extension> extensions(
      Optional<Extension> race, Optional<Extension> ethnicity, Optional<Extension> birthSex) {
    List<Extension> extensions = new ArrayList<>(3);
    race.ifPresent(extensions::add);
    ethnicity.ifPresent(extensions::add);
    birthSex.ifPresent(extensions::add);
    return extensions;
  }

  List<Coding> getCodings(List<Patient103Root.Patients.Patient.MaritalStatus.Coding> codings) {
    List<Coding> argoCodings = new LinkedList<>();
    for (Patient103Root.Patients.Patient.MaritalStatus.Coding coding : codings) {
      argoCodings.add(
          Coding.builder()
              .system(coding.getSystem().value())
              .code(coding.getCode().toString())
              .display(coding.getDisplay())
              .build());
    }
    return argoCodings;
  }

  List<String> getLine(Patient103Root.Patients.Patient.Contacts.Contact contact) {
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

  List<String> getLine(Patient103Root.Patients.Patient.Addresses.Address address) {
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

  String getSimpleBirthDate(XMLGregorianCalendar birthdate) {
    Date date = birthdate.toGregorianCalendar().getTime();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    return formatter.format(date);
  }

  HumanName humanName(String name) {
    return HumanName.builder().text(name).build();
  }

  Identifier.IdentifierUse identifierUse(Patient103Root.Patients.Patient.Identifier identifier) {
    return Identifier.IdentifierUse.valueOf(identifier.getUse().name().toLowerCase());
  }

  List<Identifier> identifiers(List<Patient103Root.Patients.Patient.Identifier> identifiers) {
    List<Identifier> argoIdentifiers = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Identifier identifier : identifiers) {
      argoIdentifiers.add(
          Identifier.builder()
              .use(identifierUse(identifier))
              .type(codeableConcept(identifier.getType()))
              .system(identifier.getSystem())
              .value(identifier.getValue())
              .assigner(reference(identifier.getAssigner()))
              .build());
    }
    return argoIdentifiers;
  }

  CodeableConcept maritalStatus(Patient103Root.Patients.Patient.MaritalStatus maritalStatus) {
    return CodeableConcept.builder()
        .text(maritalStatus.getText())
        .coding(getCodings(maritalStatus.getCoding()))
        .build();
  }

  List<HumanName> name(Patient103Root.Patients.Patient.Name name) {
    return Collections.singletonList(
        HumanName.builder()
            .use(HumanName.NameUse.valueOf(name.getUse()))
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
        .name(name(patient.getName()))
        .telecom(telecoms(patient.getTelecoms()))
        .address(addresses(patient.getAddresses()))
        .gender(Patient.Gender.valueOf(patient.getGender().toString().toLowerCase()))
        .birthDate(getSimpleBirthDate(patient.getBirthDate()))
        .deceasedBoolean(patient.isDeceasedBoolean())
        .deceasedDateTime(deceasedDateTime(patient.getDeceasedDateTime()))
        .maritalStatus(maritalStatus(patient.getMaritalStatus()))
        .contact(contacts(patient.getContacts()))
        .build();
  }

  List<Extension> raceExtensions(List<Extensions.Extension> argoRace) {
    List<Extension> extensions = new LinkedList<>();
    for (Extensions.Extension extension : argoRace) {
      if (extension.getUrl().equals("text")) {
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

  Reference reference(Patient103Root.Patients.Patient.Identifier.Assigner assigner) {
    return Reference.builder().display(assigner.getDisplay()).build();
  }

  List<CodeableConcept> relationship(
      Patient103Root.Patients.Patient.Contacts.Contact.Relationship relationship) {
    return Collections.singletonList(
        CodeableConcept.builder()
            .coding(coding(relationship.getCoding()))
            .text(relationship.getText())
            .build());
  }

  List<ContactPoint> telecom(Patient103Root.Patients.Patient.Contacts.Contact contact) {
    ContactPoint.ContactPointBuilder contactPointBuilder = ContactPoint.builder();
    if (StringUtils.isNotBlank(contact.getPhone())) {
      contactPointBuilder.system(ContactPoint.ContactPointSystem.phone);
      contactPointBuilder.value(contact.getPhone());
    }
    return Collections.singletonList(contactPointBuilder.build());
  }

  List<ContactPoint> telecoms(Patient103Root.Patients.Patient.Telecoms telecoms) {
    List<ContactPoint> contactPoints = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Telecoms.Telecom telecom : telecoms.getTelecom()) {
      contactPoints.add(
          ContactPoint.builder()
              .system(
                  ContactPoint.ContactPointSystem.valueOf(
                      telecom.getSystem().toString().toLowerCase()))
              .value(telecom.getValue())
              .use(ContactPoint.ContactPointUse.valueOf(telecom.getUse().toString().toLowerCase()))
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
