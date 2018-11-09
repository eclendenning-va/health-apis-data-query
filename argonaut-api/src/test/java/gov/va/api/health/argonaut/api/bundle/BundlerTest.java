package gov.va.api.health.argonaut.api.bundle;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.dvp.cdw.xsd.pojos.*;
import lombok.SneakyThrows;
import org.junit.Test;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;

public class BundlerTest {

    private XmlSampleData data = new XmlSampleData();

  @Test
  public void bundlerBuildsFullyQualifiedBundle() {
    Bundler.from(Patient103Root.class).to(Patient.class).build();
  }

  private static class XmlSampleData {

    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private XmlSampleData() {
      datatypeFactory = DatatypeFactory.newInstance();
    }

    Patient103Root rootAlivePatient() {
        Patient103Root root = new Patient103Root();
        root.setFhirVersion("DSTU2 Argonaut");
        root.setResourceName(PatientResourceNameValue.PATIENT);
        root.setResourceVersion("1.03");
        root.setReturnType(ReturnTypeCodes.FULL);
        root.setReturnFormat(ReturnFormatCodes.XML);
        root.setRecordsPerPage(15);
        root.setPageNumber(1);
        root.setStartRecord(1);
        root.setEndRecord(1);
        root.setRecordCount(1);
        root.setPageCount(1);
        root.setErrorNumber(0);
        root.setErrorLine(0);
        Patient103Root.Patients patients = new Patient103Root.Patients();
        patients.getPatient().add(alivePatient());
        root.setPatients(patients);
        return root;
    }

    Patient103Root.Patients.Patient.Addresses addresses() {

      Patient103Root.Patients.Patient.Addresses addresses =
          new Patient103Root.Patients.Patient.Addresses();

      Patient103Root.Patients.Patient.Addresses.Address address =
          new Patient103Root.Patients.Patient.Addresses.Address();
      address.setState("Missing*");
      addresses.getAddress().add(address);

      address = new Patient103Root.Patients.Patient.Addresses.Address();
      address.setStreetAddress1("1234 Test Road");
      address.setStreetAddress2("Testland");
      address.setStreetAddress3("Test POSTGRES");
      address.setCity("Testville");
      address.setState("Testlina");
      address.setPostalCode("12345");
      addresses.getAddress().add(address);

      address = new Patient103Root.Patients.Patient.Addresses.Address();
      address.setStreetAddress1("9876 Fake Lane");
      address.setCity("Fooville");
      address.setState("Foolina");
      address.setPostalCode("98765");
      addresses.getAddress().add(address);

      return addresses;
    }

    Patient103Root.Patients.Patient alivePatient() {
      Patient103Root.Patients.Patient patient = new Patient103Root.Patients.Patient();
      patient.setRowNumber(new BigInteger("1"));
      patient.setCdwId("123456789");
      patient.getArgoRace().add(raceExtensions());
      patient.getArgoEthnicity().add(ethnicityExtensions());
      patient.setArgoBirthsex(birthSexExtension());
      patient.getIdentifier().add(identifier());
      patient.setName(name());
      patient.setTelecoms(telecoms());
      patient.setAddresses(addresses());
      patient.setGender(AdministrativeGenderCodes.MALE);
      patient.setBirthDate(birthdate());
      patient.setDeceasedBoolean(false);
      patient.setMaritalStatus(maritalStatus());
      patient.setContacts(contacts());
      return patient;
    }

    BirthsexExtension birthSexExtension() {
      BirthsexExtension testBirthSexExtension = new BirthsexExtension();
      testBirthSexExtension.setValueCode(BirthSexCodes.M);
      testBirthSexExtension.setUrl("http://test-birthsex");
      return testBirthSexExtension;
    }

    @SneakyThrows
    XMLGregorianCalendar birthdate() {
      XMLGregorianCalendar birthdate = datatypeFactory.newXMLGregorianCalendar();
      birthdate.setYear(2018);
      birthdate.setMonth(11);
      birthdate.setDay(06);
      return birthdate;
    }

    Patient103Root.Patients.Patient.Contacts contacts() {
      Patient103Root.Patients.Patient.Contacts contacts =
          new Patient103Root.Patients.Patient.Contacts();

      Patient103Root.Patients.Patient.Contacts.Contact contact1 =
          new Patient103Root.Patients.Patient.Contacts.Contact();

      Patient103Root.Patients.Patient.Contacts.Contact.Relationship relationship1 =
          new Patient103Root.Patients.Patient.Contacts.Contact.Relationship();

      Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding relationshipCoding1 =
          new Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding();
      relationshipCoding1.setSystem(
          PatientContactRelationshipSystem.HTTP_HL_7_ORG_FHIR_PATIENT_CONTACT_RELATIONSHIP);
      relationshipCoding1.setCode(PatientContactRelationshipCodes.EMERGENCY);
      relationshipCoding1.setDisplay("Emergency");

      relationship1.setCoding(relationshipCoding1);
      relationship1.setText("Emergency Contact");

      contact1.setRelationship(relationship1);
      contact1.setName("DUCK, DAFFY JOHN");
      contact1.setPhone("9998886666");
      contact1.setStreetAddress1("123 Happy Avenue");
      contact1.setStreetAddress2("456 Smile Drive");
      contact1.setStreetAddress3("789 Laughter Lane");
      contact1.setCity("Happyland");
      contact1.setState("Happylina");
      contact1.setPostalCode("12345");
      contact1.setCountry("USA");
      contacts.getContact().add(contact1);

      Patient103Root.Patients.Patient.Contacts.Contact contact2 =
          new Patient103Root.Patients.Patient.Contacts.Contact();

      Patient103Root.Patients.Patient.Contacts.Contact.Relationship relationship2 =
          new Patient103Root.Patients.Patient.Contacts.Contact.Relationship();

      Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding relationshipCoding2 =
          new Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding();
      relationshipCoding2.setSystem(
          PatientContactRelationshipSystem.HTTP_HL_7_ORG_FHIR_PATIENT_CONTACT_RELATIONSHIP);
      relationshipCoding2.setCode(PatientContactRelationshipCodes.EMERGENCY);
      relationshipCoding2.setDisplay("Emergency");

      relationship2.setCoding(relationshipCoding2);
      relationship2.setText("Emergency Contact");

      contact2.setRelationship(relationship2);
      contact2.setName("ALICE, TEST JANE");
      contact2.setPhone("1112224444");
      contact2.setStreetAddress1("123 Sad Avenue");
      contact2.setStreetAddress2("456 Frown Drive");
      contact2.setStreetAddress3("789 Weeping Lane");
      contact2.setCity("Sadland");
      contact2.setState("Sadlina");
      contact2.setPostalCode("98765");
      contact2.setCountry("USA");
      contacts.getContact().add(contact2);

      return contacts;
    }

    Patient103Root.Patients.Patient deadPatient() {
      Patient103Root.Patients.Patient patient = new Patient103Root.Patients.Patient();
      patient.setDeceasedBoolean(true);
      patient.setDeceasedDateTime(deceasedDateTime());
      return patient;
    }

    XMLGregorianCalendar deceasedDateTime() {
      XMLGregorianCalendar deceasedDate = datatypeFactory.newXMLGregorianCalendar();
      deceasedDate.setYear(2018);
      deceasedDate.setMonth(11);
      deceasedDate.setDay(07);
      return deceasedDate;
    }

    Extensions ethnicityExtensions() {
      Extensions extensions = new Extensions();
      extensions.setUrl("http://test-ethnicity");

      Extensions.Extension extension1 = new Extensions.Extension();
      extension1.setUrl("ombTest");

      Extensions.Extension.ValueCoding valueCoding = new Extensions.Extension.ValueCoding();
      valueCoding.setSystem("http://test-ethnicity");
      valueCoding.setCode("3THN1C1TY");
      valueCoding.setDisplay("testa");
      extension1.setValueCoding(valueCoding);

      extensions.getExtension().add(extension1);

      Extensions.Extension extension2 = new Extensions.Extension();
      extension2.setUrl("text");
      extension2.setValueString("testa");

      extensions.getExtension().add(extension2);
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
      coding.setSystem("http://test-code");
      coding.setCode("C0D3");
      type.getCoding().add(coding);
      identifier.setType(type);
      identifier.setSystem("http://test-system");
      identifier.setValue("123456789");
      Patient103Root.Patients.Patient.Identifier.Assigner assigner =
          new Patient103Root.Patients.Patient.Identifier.Assigner();
      assigner.setDisplay("tester-test-index");
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

    Extensions raceExtensions() {
      Extensions extensions = new Extensions();
      extensions.setUrl("http://test-race");

      Extensions.Extension extension1 = new Extensions.Extension();
      extension1.setUrl("ombTest");

      Extensions.Extension.ValueCoding valueCoding = new Extensions.Extension.ValueCoding();
      valueCoding.setSystem("http://test-race");
      valueCoding.setCode("R4C3");
      valueCoding.setDisplay("tester");
      extension1.setValueCoding(valueCoding);

      extensions.getExtension().add(extension1);

      Extensions.Extension extension2 = new Extensions.Extension();
      extension2.setUrl("text");
      extension2.setValueString("tester");

      extensions.getExtension().add(extension2);
      return extensions;
    }

    Patient103Root.Patients.Patient.Telecoms telecoms() {
      Patient103Root.Patients.Patient.Telecoms testTelecoms =
          new Patient103Root.Patients.Patient.Telecoms();

      Patient103Root.Patients.Patient.Telecoms.Telecom telecom1 =
          new Patient103Root.Patients.Patient.Telecoms.Telecom();
      telecom1.setSystem(ContactPointSystemCodes.PHONE);
      telecom1.setValue("9998886666");
      telecom1.setUse(ContactPointUseCodes.HOME);
      testTelecoms.getTelecom().add(telecom1);

      Patient103Root.Patients.Patient.Telecoms.Telecom telecom2 =
          new Patient103Root.Patients.Patient.Telecoms.Telecom();
      telecom2.setSystem(ContactPointSystemCodes.PHONE);
      telecom2.setValue("1112224444");
      telecom2.setUse(ContactPointUseCodes.WORK);
      testTelecoms.getTelecom().add(telecom2);
      return testTelecoms;
    }
  }
}
