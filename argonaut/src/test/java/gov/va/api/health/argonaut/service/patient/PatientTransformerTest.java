package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.service.controller.patient.PatientTransformer;
import gov.va.dvp.cdw.xsd.pojos.AdministrativeGenderCodes;
import gov.va.dvp.cdw.xsd.pojos.BirthSexCodes;
import gov.va.dvp.cdw.xsd.pojos.BirthsexExtension;
import gov.va.dvp.cdw.xsd.pojos.ContactPointSystemCodes;
import gov.va.dvp.cdw.xsd.pojos.ContactPointUseCodes;
import gov.va.dvp.cdw.xsd.pojos.Extensions;
import gov.va.dvp.cdw.xsd.pojos.IdentifierUseCodes;
import gov.va.dvp.cdw.xsd.pojos.MaritalStatusCodes;
import gov.va.dvp.cdw.xsd.pojos.MaritalStatusSystems;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root.Patients.Patient;
import gov.va.dvp.cdw.xsd.pojos.PatientContactRelationshipCodes;
import gov.va.dvp.cdw.xsd.pojos.PatientContactRelationshipSystem;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.junit.Assert.fail;

public class PatientTransformerTest {

  private XmlSampleData data = new XmlSampleData();

  @Test
  public void patient103TransformsToJsonPatient() {
    new PatientTransformer().apply(data.alivePatient());
    fail();
  }

  @Test
  public void contactTransformsToAddress() {
    fail();
  }

  @Test
  public void addressesTransformsToAddressList() {
    fail();
  }

  @Test
  public void argoBirthsex() {
    fail();
  }

  @Test
  public void argoEthnicity() {
    fail();
  }

  @Test
  public void argoRace() {
    fail();
  }

  @Test
  public void identifierTypeTransfromsToCodeableConcept() {
    fail();
  }

  @Test
  public void relationshipCodingTransformsToCodingList() {
    fail();
  }

  @Test
  public void identifierTypeCodingListTransformsToCodingList() {
    fail();
  }

  @Test
  public void contactsTransformsToContactList() {
    fail();
  }

  @Test
  public void deceasedDateTimeTransformsToString() {
    fail();
  }

  @Test
  public void deceasedDateTimeMissingReturnsNull() {
    fail();
  }

  @Test
  public void argoEthnicityTransformsToExtensionList() {
    fail();
  }

  @Test
  public void argoEthnicityTextUrlTransformsToExtensionsList() {
    fail();
  }

  @Test
  public void argoPatientExtensionsTransformToExtensionList() {
    fail();
  }

  @Test
  public void argoPatientExtensionsMissingTransformsToEmptyExtensionList() { fail(); }

  @Test
  public void argoPartialPatientExtensionsTransformsToExtensionList() { fail(); }

  @Test
  public void maritalStatusCodingListTransformsToCodingList() {
    fail();
  }

  @Test
  public void contactTransformsToStringLine() {
    fail();
  }

  @Test
  public void addressTransformsToLine() {
    fail();
  }

  @Test
  public void birthDateTransformsToSimpleDateString() {
    fail();
  }

  @Test
  public void contactNameTransformsToHumanName() {
    fail();
  }

  @Test
  public void identifierTransformsToIdentifierUse() {
    fail();
  }

  @Test
  public void identifiersTransformsToIdentifiersList() {
    fail();
  }

  @Test
  public void maritalStatusTransformsToCodeableConcept() {
    fail();
  }

  @Test
  public void stringTransformsToHumanName() {
    fail();
  }

  @Test
  public void argoRaceExtensionTransformsToExtensionList() {
    fail();
  }

  @Test
  public void argoRaceExtensionTextUrlTransformsToExtensionsList() { fail(); }

  @Test
  public void assignerTransformsToReference() { fail(); }

  @Test
  public void relationshipTransformsToCodeableConceptList() { fail(); }

  @Test
  public void contactTranformsToContactPointList() { fail();  }

  @Test
  public void telecomsTransformsToContactPointList() { fail(); }

  @Test
  public void valueCodingTransformsToCoding() { fail(); }

  private static class XmlSampleData {

    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private XmlSampleData() {
      datatypeFactory = DatatypeFactory.newInstance();
    }

    Patient103Root.Patients.Patient.Addresses addresses() {
      Patient103Root.Patients.Patient.Addresses.Address address =
          new Patient103Root.Patients.Patient.Addresses.Address();
      address.setStreetAddress1("hi road");
      address.setStreetAddress2("low park");
      address.setStreetAddress3("mid way");
      address.setCity("fooville");
      address.setState("Foolina");
      address.setPostalCode("ABC123");
      address.setGisFipsCode("GIS_FIPS");
      address.setGisPatientAddressLatitude(12.0f);
      address.setGisPatientAddressLongitude(21.0f);
      Patient103Root.Patients.Patient.Addresses addresses =
          new Patient103Root.Patients.Patient.Addresses();
      addresses.getAddress().add(address);
      return addresses;
    }

    Patient alivePatient() {
      Patient103Root.Patients.Patient patient = new Patient103Root.Patients.Patient();
      patient.setRowNumber(new BigInteger("1"));
      patient.setCdwId("123456789");
      patient.getArgoRace().add(extensions());
      patient.getArgoEthnicity().add(extensions());
      patient.setArgoBirthsex(birthSexExtension());
      patient.getIdentifier().add(identifier());
      patient.setName(name());
      patient.setTelecoms(telecoms());
      patient.setAddresses(addresses());
      patient.setGender(AdministrativeGenderCodes.UNKNOWN);
      patient.setBirthDate(birthdate());
      patient.setMaritalStatus(maritalStatus());
      patient.setContacts(contacts());
      patient.setDeceasedBoolean(false);
      return patient;
    }

    BirthsexExtension birthSexExtension() {
      BirthsexExtension testBirthSexExtension = new BirthsexExtension();
      testBirthSexExtension.setValueCode(BirthSexCodes.UNK);
      testBirthSexExtension.setUrl("http://www.testville.com");
      return testBirthSexExtension;
    }

    @SneakyThrows
    XMLGregorianCalendar birthdate() {
      XMLGregorianCalendar birthdate = datatypeFactory.newXMLGregorianCalendar();
      birthdate.setYear(2088);
      birthdate.setMonth(11);
      birthdate.setDay(3);
      birthdate.setTimezone(0);
      birthdate.setHour(6);
      birthdate.setMinute(0);
      birthdate.setSecond(9);
      return birthdate;
    }

    Patient103Root.Patients.Patient.Contacts contacts() {
      Patient103Root.Patients.Patient.Contacts contacts =
          new Patient103Root.Patients.Patient.Contacts();
      Patient103Root.Patients.Patient.Contacts.Contact contact =
          new Patient103Root.Patients.Patient.Contacts.Contact();
      Patient103Root.Patients.Patient.Contacts.Contact.Relationship relationship =
          new Patient103Root.Patients.Patient.Contacts.Contact.Relationship();
      Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding relationshipCoding =
          new Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding();
      relationshipCoding.setSystem(
          PatientContactRelationshipSystem.HTTP_HL_7_ORG_FHIR_PATIENT_CONTACT_RELATIONSHIP);
      relationshipCoding.setCode(PatientContactRelationshipCodes.EMERGENCY);
      relationshipCoding.setDisplay("Emergency");
      relationship.setCoding(relationshipCoding);
      relationship.setText("TestRelations");
      contact.setRelationship(relationship);
      contact.setName("DAFFY, DUCK");
      contact.setPhone("1112223334");
      contact.setStreetAddress1("123 Happy Avenue");
      contact.setStreetAddress2("456 Smile Drive");
      contact.setStreetAddress3("789 Laughter Lane");
      contact.setCity("Hollywood");
      contact.setState("CALIFORNIA");
      contact.setPostalCode("456789");
      contact.setCountry("USA");
      contacts.getContact().add(contact);
      return contacts;
    }

    Patient deadPatient() {
      Patient103Root.Patients.Patient patient = deadPatient();
      patient.setDeceasedBoolean(null);
      patient.setDeceasedDateTime(deceasedDateTime());
      return patient;
    }

    XMLGregorianCalendar deceasedDateTime() {
      XMLGregorianCalendar deceasedDate = datatypeFactory.newXMLGregorianCalendar();
      deceasedDate.setYear(2088);
      deceasedDate.setMonth(11);
      deceasedDate.setDay(3);
      deceasedDate.setTimezone(0);
      deceasedDate.setHour(6);
      deceasedDate.setMinute(0);
      deceasedDate.setSecond(9);
      return deceasedDate;
    }

    Extensions extensions() {
      Extensions extensions = new Extensions();
      Extensions.Extension extension = new Extensions.Extension();
      Extensions.Extension testExtension = new Extensions.Extension();
      extensions.setUrl("http://testland.com");
      extension.setValueString("testvalue");
      extension.setUrl("http://testville.com");
      testExtension.setUrl("text");
      testExtension.setValueString("textvalue");
      extension.setValueCoding(valueCoding());
      extensions.getExtension().add(extension);
      extensions.getExtension().add(testExtension);
      return extensions;
    }

    Patient103Root.Patients.Patient.Identifier identifier() {
      Patient103Root.Patients.Patient.Identifier identifier =
          new Patient103Root.Patients.Patient.Identifier();
      identifier.setUse(IdentifierUseCodes.USUAL);
      Patient103Root.Patients.Patient.Identifier.Type type =
          new Patient103Root.Patients.Patient.Identifier.Type();
      Patient103Root.Patients.Patient.Identifier.Type.Coding coding =
          new Patient103Root.Patients.Patient.Identifier.Type.Coding();
      coding.setSystem("http://coding.system");
      coding.setCode("testCODE");
      type.getCoding().add(coding);
      identifier.setType(type);
      identifier.setSystem("http://identifier.system");
      identifier.setValue("123456789");
      Patient103Root.Patients.Patient.Identifier.Assigner assigner =
          new Patient103Root.Patients.Patient.Identifier.Assigner();
      assigner.setDisplay("tester's brain");
      identifier.setAssigner(assigner);
      return identifier;
    }

    Patient103Root.Patients.Patient.MaritalStatus maritalStatus() {
      Patient103Root.Patients.Patient.MaritalStatus maritalStatus =
          new Patient103Root.Patients.Patient.MaritalStatus();
      maritalStatus.setText("testMarriage");
      Patient103Root.Patients.Patient.MaritalStatus.Coding coding =
          new Patient103Root.Patients.Patient.MaritalStatus.Coding();
      coding.setSystem(MaritalStatusSystems.HTTP_HL_7_ORG_FHIR_MARITAL_STATUS);
      coding.setCode(MaritalStatusCodes.M);
      coding.setDisplay("Married");
      maritalStatus.getCoding().add(coding);
      return maritalStatus;
    }

    Patient103Root.Patients.Patient.Name name() {
      Patient103Root.Patients.Patient.Name name = new Patient103Root.Patients.Patient.Name();
      name.setUse("usual");
      name.setText("FOOMAN FOO");
      name.setFamily("FOO");
      name.setGiven("FOOMAN");
      return name;
    }

    Patient patientWithNoExtensions() {
      Patient103Root.Patients.Patient patient = deadPatient();
      patient.getArgoRace().clear();
      patient.getArgoEthnicity().clear();
      patient.setArgoBirthsex(null);
      return patient;
    }

    Patient103Root.Patients.Patient.Telecoms.Telecom telecom() {
      Patient103Root.Patients.Patient.Telecoms.Telecom telecom =
          new Patient103Root.Patients.Patient.Telecoms.Telecom();
      telecom.setSystem(ContactPointSystemCodes.PHONE);
      telecom.setValue("987345126");
      telecom.setUse(ContactPointUseCodes.HOME);
      return telecom;
    }

    Patient103Root.Patients.Patient.Telecoms telecoms() {
      Patient103Root.Patients.Patient.Telecoms testTelecoms =
          new Patient103Root.Patients.Patient.Telecoms();
      testTelecoms.getTelecom().add(telecom());
      return testTelecoms;
    }

    Extensions.Extension.ValueCoding valueCoding() {
      Extensions.Extension.ValueCoding valueCoding = new Extensions.Extension.ValueCoding();
      valueCoding.setCode("testcode");
      valueCoding.setDisplay("testdisplay");
      valueCoding.setSystem("testsystem");
      return valueCoding;
    }
  }
}
