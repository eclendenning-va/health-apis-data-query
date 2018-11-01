package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.*;
import gov.va.dvp.cdw.xsd.pojos.Extensions;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class PatientTransformer implements PatientController.PatientTransformer {

  @Override
  public Patient apply(Patient103Root.Patients.Patient patient) {

    return Patient.builder()
            .id(patient.getCdwId())
            .identifier(identifiers(patient.getIdentifier()))
            .name(Collections.singletonList(name(patient.getName())))
            .telecom(telecoms(patient.getTelecoms()))
            .address(addresses(patient.getAddresses()))
            .gender(Patient.Gender.valueOf(patient.getGender().toString()))
            .birthDate(patient.getBirthDate().toString())
            .build();
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
                      .system(ContactPoint.ContactPointSystem.valueOf(telecom.getSystem().toString()))
                      .value(telecom.getValue())
                      .use(ContactPoint.ContactPointUse.valueOf(telecom.getUse().toString()))
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

  private ArgoRaceExtension race(List<Extensions> argoRace) {

    return ArgoRaceExtension.builder().build();
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
    return Identifier.IdentifierUse.valueOf(identifier.getUse().name());
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
