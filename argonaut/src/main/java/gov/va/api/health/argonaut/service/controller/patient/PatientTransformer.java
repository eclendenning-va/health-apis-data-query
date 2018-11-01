package gov.va.api.health.argonaut.service.controller.patient;

import gov.va.api.health.argonaut.api.*;
import gov.va.dvp.cdw.xsd.pojos.BirthsexExtension;
import gov.va.dvp.cdw.xsd.pojos.Extensions;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class PatientTransformer implements PatientController.Transformer {

  @Override
  public Patient apply(Patient103Root.Patients.Patient patient) {

    return Patient.builder()
            .id(patient.getCdwId())
            .argoRace(argoRace(patient.getArgoRace()))
            .argoEthnicity(argoEthnicity(patient.getArgoEthnicity()))
            .argoBirthSex(argoBirthSex(patient.getArgoBirthsex()))
            .identifier(identifiers(patient.getIdentifier()))
            .name(Collections.singletonList(name(patient.getName())))
            .telecom(telecoms(patient.getTelecoms()))
            .address(addresses(patient.getAddresses()))
            .gender(Patient.Gender.valueOf(patient.getGender().toString().toLowerCase()))
            .birthDate(patient.getBirthDate().toString())
            .deceasedBoolean(patient.isDeceasedBoolean())
            .deceasedDateTime(deceasedDateTime(patient.getDeceasedDateTime()))
            .maritalStatus(maritalStatus(patient.getMaritalStatus()))
            .contact(contacts(patient.getContacts()))
            .build();
  }

  private String deceasedDateTime(XMLGregorianCalendar deceasedDateTime) {
    if(deceasedDateTime == null) {
      return null;
    }
    return deceasedDateTime.toString();
  }

  private ArgoBirthSexExtension argoBirthSex(BirthsexExtension argoBirthsex) {
    return ArgoBirthSexExtension.builder()
            .url(argoBirthsex.getUrl())
            .valueCode(argoBirthsex.getValueCode().name())
            .build();
  }

  private ArgoEthnicityExtension argoEthnicity(List<Extensions> argoEthnicity) {
    return ArgoEthnicityExtension.builder()
            .url(argoEthnicity.get(0).getUrl())
            .extension(ethnicityExtensions(argoEthnicity.get(0).getExtension()))
            .build();
  }

  private List<Extension> ethnicityExtensions(List<Extensions.Extension> argoEthnicity) {
    List<Extension> extensions = new LinkedList<>();
    for (Extensions.Extension extension: argoEthnicity) {
      if (extension.getUrl().equals("text")) {
        extensions.add(Extension.builder()
                .url(extension.getUrl())
                .valueString(extension.getValueString())
                .build());
      } else {
        extensions.add(Extension.builder()
                .url(extension.getUrl())
                .valueCoding(valueCoding(extension.getValueCoding()))
                .build());
      }
    }
    return extensions;
  }

  private ArgoRaceExtension argoRace(List<Extensions> argoRace) {
    return ArgoRaceExtension.builder()
            .url(argoRace.get(0).getUrl())
            .extension(raceExtensions(argoRace.get(0).getExtension()))
            .build();
  }

  private List<Extension> raceExtensions(List<Extensions.Extension> argoRace) {
    List<Extension> extensions = new LinkedList<>();
    for (Extensions.Extension extension: argoRace) {
      if (extension.getUrl().equals("text")) {
        extensions.add(Extension.builder()
                .url(extension.getUrl())
                .valueString(extension.getValueString())
                .build());
      } else {
        extensions.add(Extension.builder()
                .url(extension.getUrl())
                .valueCoding(valueCoding(extension.getValueCoding()))
                .build());
      }
    }
    return extensions;
  }

  private Coding valueCoding(Extensions.Extension.ValueCoding valueCoding) {
    return Coding.builder()
            .display(valueCoding.getDisplay())
            .code(valueCoding.getCode())
            .system(valueCoding.getSystem())
            .build();
  }

  private List<Contact> contacts(Patient103Root.Patients.Patient.Contacts contacts) {
    List<Contact> argoContacts = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Contacts.Contact contact: contacts.getContact()) {
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

  private Address address(Patient103Root.Patients.Patient.Contacts.Contact contact) {
    return Address.builder()
            .line(getLine(contact))
            .city(contact.getCity())
            .state(contact.getState())
            .postalCode(contact.getPostalCode())
            .build();
  }

  private List<String> getLine(Patient103Root.Patients.Patient.Contacts.Contact contact) {
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
    return line;
  }

  private List<ContactPoint> telecom(Patient103Root.Patients.Patient.Contacts.Contact contact) {
    ContactPoint.ContactPointBuilder contactPointBuilder = ContactPoint.builder();
    if(StringUtils.isNotBlank(contact.getPhone())) {
      contactPointBuilder.system(ContactPoint.ContactPointSystem.phone);
      contactPointBuilder.value(contact.getPhone());
    }
    return Collections.singletonList(contactPointBuilder.build());
  }

  private HumanName humanName(String name) {
    return HumanName.builder()
            .text(name)
            .build();
  }

  private List<CodeableConcept> relationship(Patient103Root.Patients.Patient.Contacts.Contact.Relationship relationship) {
    return Collections.singletonList(CodeableConcept.builder()
            .coding(coding(relationship.getCoding()))
            .text(relationship.getText())
            .build());
  }

  private List<Coding> coding(Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding relationship) {
    return Collections.singletonList(Coding.builder()
            .system(relationship.getSystem().toString())
            .code(relationship.getCode().toString())
            .display(relationship.getDisplay())
            .build());
  }

  private CodeableConcept maritalStatus(Patient103Root.Patients.Patient.MaritalStatus maritalStatus) {
    return CodeableConcept.builder()
            .text(maritalStatus.getText())
            .coding(getCodings(maritalStatus.getCoding()))
            .build();
  }

  private List<Coding> getCodings(List<Patient103Root.Patients.Patient.MaritalStatus.Coding> codings) {
    List<Coding> argoCodings = new LinkedList<>();
    for (Patient103Root.Patients.Patient.MaritalStatus.Coding coding: codings) {
      argoCodings.add(
              Coding.builder()
                      .system(coding.getSystem().toString())
                      .code(coding.getCode().toString())
                      .build());
    }
    return argoCodings;
  }


  private List<Address> addresses(Patient103Root.Patients.Patient.Addresses addresses) {
    List<Address> argoAddresses = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Addresses.Address address: addresses.getAddress()) {
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

  private List<String> getLine(Patient103Root.Patients.Patient.Addresses.Address address) {
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
    return line;
  }

  private List<ContactPoint> telecoms(Patient103Root.Patients.Patient.Telecoms telecoms) {
    List<ContactPoint> contactPoints = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Telecoms.Telecom telecom: telecoms.getTelecom()) {
      contactPoints.add(
              ContactPoint.builder()
                      .system(ContactPoint.ContactPointSystem.valueOf(telecom.getSystem().toString().toLowerCase()))
                      .value(telecom.getValue())
                      .use(ContactPoint.ContactPointUse.valueOf(telecom.getUse().toString().toLowerCase()))
                      .build());
    }
    return contactPoints;
  }

  private HumanName name(Patient103Root.Patients.Patient.Name name) {
    return HumanName.builder()
            .use(HumanName.NameUse.valueOf(name.getUse()))
            .text(name.getText())
            .family(Collections.singletonList(name.getFamily()))
            .given(Collections.singletonList(name.getGiven()))
            .build();

  }

  private List<Identifier> identifiers(List<Patient103Root.Patients.Patient.Identifier> identifiers) {
    List<Identifier> argoIdentifiers = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Identifier identifier: identifiers) {
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

  private Reference reference(Patient103Root.Patients.Patient.Identifier.Assigner assigner) {
    return Reference.builder()
            .display(assigner.getDisplay())
            .build();
  }

  private Identifier.IdentifierUse identifierUse(Patient103Root.Patients.Patient.Identifier identifier) {
    return Identifier.IdentifierUse.valueOf(identifier.getUse().name().toLowerCase());
  }

  private CodeableConcept codeableConcept(Patient103Root.Patients.Patient.Identifier.Type type) {
    return CodeableConcept.builder()
            .coding(codings(type.getCoding()))
            .build();
  }

  private List<Coding> codings(List<Patient103Root.Patients.Patient.Identifier.Type.Coding> codings) {
    List<Coding> argoCodings = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Identifier.Type.Coding coding: codings) {
      argoCodings.add(
              Coding.builder()
                      .system(coding.getSystem())
                      .code(coding.getCode())
                      .build());
    }
    return argoCodings;
  }
}
