package gov.va.api.health.argonaut.service.controller.patient;

import gov.va.api.health.argonaut.api.*;
import gov.va.api.health.argonaut.api.Reference;
import gov.va.dvp.cdw.xsd.pojos.*;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root.Patients.Patient;
import lombok.SneakyThrows;
import org.junit.Test;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;


public class PatientTransformerTest {

  private XmlSampleData data = new XmlSampleData();
  private PatientSampleData patient = new PatientSampleData();

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
    PatientTransformer patientTransformer = new PatientTransformer();
    List<Address> testAddresses = patientTransformer.addresses(data.alivePatient().getAddresses());
    List<Address> expectedAddresses = patient.address();
    assertThat(testAddresses.equals(expectedAddresses));
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

    Patient alivePatient() {
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

    Extensions raceExtensions() {
      Extensions extensions = new Extensions();
      extensions.setUrl("http://test-race");

      Extensions.Extension extension = new Extensions.Extension();
      extension.setUrl("ombTest");

      Extensions.Extension.ValueCoding valueCoding = new Extensions.Extension.ValueCoding();
      valueCoding.setSystem("http://test-race");
      valueCoding.setCode("R4C3");
      valueCoding.setDisplay("tester");
      extension.setValueCoding(valueCoding);

      extensions.getExtension().add(extension);

      extension = new Extensions.Extension();
      extension.setUrl("text");
      extension.setValueString("tester");

      extensions.getExtension().add(extension);
      return extensions;
    }

    Extensions ethnicityExtensions() {
      Extensions extensions = new Extensions();
      extensions.setUrl("http://test-ethnicity");

      Extensions.Extension extension = new Extensions.Extension();
      extension.setUrl("ombTest");

      Extensions.Extension.ValueCoding valueCoding = new Extensions.Extension.ValueCoding();
      valueCoding.setSystem("http://test-ethnicity");
      valueCoding.setCode("3THN1C1TY");
      valueCoding.setDisplay("testa");
      extension.setValueCoding(valueCoding);

      extensions.getExtension().add(extension);

      extension = new Extensions.Extension();
      extension.setUrl("text");
      extension.setValueString("testa");

      extensions.getExtension().add(extension);
      return extensions;    }

    BirthsexExtension birthSexExtension() {
      BirthsexExtension testBirthSexExtension = new BirthsexExtension();
      testBirthSexExtension.setValueCode(BirthSexCodes.M);
      testBirthSexExtension.setUrl("http://argo-birthsex");
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
      relationship.setText("Emergency Contact");

      contact.setRelationship(relationship);
      contact.setName("DAFFY, DUCK JOHN");
      contact.setPhone("9998886666");
      contact.setStreetAddress1("123 Happy Avenue");
      contact.setStreetAddress2("456 Smile Drive");
      contact.setStreetAddress3("789 Laughter Lane");
      contact.setCity("Happyland");
      contact.setState("Happylina");
      contact.setPostalCode("12345");
      contact.setCountry("USA");

      contacts.getContact().add(contact);

      contact = new Patient103Root.Patients.Patient.Contacts.Contact();

      relationship = new Patient103Root.Patients.Patient.Contacts.Contact.Relationship();

      relationshipCoding = new Patient103Root.Patients.Patient.Contacts.Contact.Relationship.Coding();
      relationshipCoding.setSystem(
              PatientContactRelationshipSystem.HTTP_HL_7_ORG_FHIR_PATIENT_CONTACT_RELATIONSHIP);
      relationshipCoding.setCode(PatientContactRelationshipCodes.EMERGENCY);
      relationshipCoding.setDisplay("Emergency");

      relationship.setCoding(relationshipCoding);
      relationship.setText("Emergency Contact");

      contact.setRelationship(relationship);
      contact.setName("DAFFY, DUCK JOHN");
      contact.setPhone("1112224444");
      contact.setStreetAddress1("123 Sad Avenue");
      contact.setStreetAddress2("456 Frown Drive");
      contact.setStreetAddress3("789 Weeping Lane");
      contact.setCity("Sadland");
      contact.setState("Sadlina");
      contact.setPostalCode("98765");
      contact.setCountry("USA");


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

    Patient patientWithNoExtensions() {
      Patient103Root.Patients.Patient patient = deadPatient();
      patient.getArgoRace().clear();
      patient.getArgoEthnicity().clear();
      patient.setArgoBirthsex(null);
      return patient;
    }

    Patient103Root.Patients.Patient.Telecoms telecoms() {
      Patient103Root.Patients.Patient.Telecoms testTelecoms =
          new Patient103Root.Patients.Patient.Telecoms();

      Patient103Root.Patients.Patient.Telecoms.Telecom telecom =
              new Patient103Root.Patients.Patient.Telecoms.Telecom();
      telecom.setSystem(ContactPointSystemCodes.PHONE);
      telecom.setValue("9998886666");
      telecom.setUse(ContactPointUseCodes.HOME);
      testTelecoms.getTelecom().add(telecom);

      telecom = new Patient.Telecoms.Telecom();
      telecom.setSystem(ContactPointSystemCodes.PHONE);
      telecom.setValue("1112224444");
      telecom.setUse(ContactPointUseCodes.WORK);
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

  private static class PatientSampleData {

    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private PatientSampleData() {
      datatypeFactory = DatatypeFactory.newInstance();
    }

    gov.va.api.health.argonaut.api.Patient alivePatient() {
      return gov.va.api.health.argonaut.api.Patient.builder()
              .id("123456789")
              .extension(argoExtensions())
              .identifier(identifier())
              .name(name())
              .telecom(telecom())
              .gender(gov.va.api.health.argonaut.api.Patient.Gender.male)
              .birthDate("2018-11-06")
              .deceasedBoolean(false)
              .address(address())
              .maritalStatus(maritalStatus())
              .contact(contact())
              .build();
    }

    List<Contact> contact() {
      List<Contact> contacts = new LinkedList<>();

      List<String> line = new LinkedList<>();
      line.add("123 Happy Avenue");
      line.add("456 Smile Drive");
      line.add("789 Laugher Lane");
      contacts.add(Contact.builder()
              .relationship(relationship())
              .name(HumanName.builder().text("DUCK, DAFFY JOHN").build())
              .telecom(Collections.singletonList(ContactPoint.builder()
                      .system(ContactPoint.ContactPointSystem.phone)
                      .value("9998886666")
                      .build()))
              .address(Address.builder()
                      .line(line)
                      .city("Happyland")
                      .state("Happylina")
                      .postalCode("12345")
                      .country("USA")
                      .build())
              .build());


      line = new LinkedList<>();
      line.add("123 Sad Avenue");
      line.add("456 Frown Drive");
      line.add("789 Weeping Lane");
      contacts.add(Contact.builder()
              .relationship(relationship())
              .name(HumanName.builder().text("ALICE, TEST JANE").build())
              .telecom(Collections.singletonList(ContactPoint.builder()
                      .system(ContactPoint.ContactPointSystem.phone)
                      .value("1112224444")
                      .build()))
              .address(Address.builder()
                      .line(line)
                      .city("Sadland")
                      .state("Sadlina")
                      .postalCode("98765")
                      .country("USA")
                      .build())
              .build());

      return contacts;
    }

    List<CodeableConcept> relationship() {
      return Collections.singletonList(CodeableConcept.builder()
              .coding(Collections.singletonList(Coding.builder()
                      .system("http://patient-contact-relationship")
                      .code("emergency")
                      .display("Emergency")
                      .build()))
              .text("Emergency Contact")
              .build());
    }

    CodeableConcept maritalStatus() {
      return CodeableConcept.builder()
              .coding(Collections.singletonList(Coding.builder()
                      .system("http://test-maritalstatus")
                      .code("M")
                      .display("Married")
                      .build()))
              .text("testMarriage")
              .build();
    }

    List<Address> address() {
      List<Address> addresses = new LinkedList<>();
      addresses.add(Address.builder()
              .state("Missing*")
              .build());
      List<String> line = new LinkedList<>();
      line.add("1234 Test Road");
      line.add("Testland");
      line.add("Test POSTGRES");
      addresses.add(Address.builder()
              .line(line)
              .city("Testville")
              .state("Testlina")
              .postalCode("12345")
              .build());
      line.clear();
      line.add("9876 Fake Lane");
      addresses.add(Address.builder()
              .line(line)
              .city("Fooville")
              .state("Foolina")
              .postalCode("98765")
              .build());
      return addresses;
    }

    private List<ContactPoint> telecom() {
      List<ContactPoint> telecoms = new LinkedList<>();
      telecoms.add(ContactPoint.builder()
              .system(ContactPoint.ContactPointSystem.phone)
              .value("9998886666")
              .use(ContactPoint.ContactPointUse.home)
              .build());
      telecoms.add(ContactPoint.builder()
              .system(ContactPoint.ContactPointSystem.phone)
              .value("1112224444")
              .use(ContactPoint.ContactPointUse.work)
              .build());
      return telecoms;
    }

    List<HumanName> name() {
      return Collections.singletonList(HumanName.builder()
              .use(HumanName.NameUse.usual)
              .text("FOOMAN FOO")
              .family(Collections.singletonList("FOO"))
              .given(Collections.singletonList("FOOMAN"))
              .build());
    }

    List<Identifier> identifier() {
      List<Identifier> identifiers = new LinkedList<>();
      identifiers.add(Identifier.builder()
              .use(Identifier.IdentifierUse.usual)
              .type(CodeableConcept.builder()
                      .coding(Collections.singletonList(Coding.builder()
                              .system("http://test-code")
                              .code("C0D3")
                              .build()))
                      .build())
              .system("http://test-system")
              .value("123456789")
              .assigner(Reference.builder()
                      .display("tester-test-index")
                      .build())
              .build());

      return identifiers;
    }

    List<Extension> argoExtensions() {
      List<Extension> extensions = new ArrayList<>(3);

      List<Extension> raceExtensions = new LinkedList<>();
      raceExtensions.add(Extension.builder()
              .url("ombTest")
              .valueCoding(Coding.builder()
                      .system("http://test-race")
                      .code("R4C3")
                      .display("tester")
                      .build())
              .build());
      raceExtensions.add(Extension.builder()
              .url("text")
              .valueString("tester")
              .build());

      List<Extension> ethnicityExtensions = new LinkedList<>();
      ethnicityExtensions.add(Extension.builder()
              .url("ombTest")
              .valueCoding(Coding.builder()
                      .system("http://test-ethnicity")
                      .code("3THN1C1TY")
                      .display("testa")
                      .build())
              .build());
      ethnicityExtensions.add(Extension.builder()
              .url("text")
              .valueString("testa")
              .build());

      extensions.add(Extension.builder()
              .url("http://argo-race.com")
              .extension(raceExtensions)
              .build());
      extensions.add(Extension.builder()
              .url("http://argo-ethnicity")
              .extension(ethnicityExtensions)
              .build());
      extensions.add(Extension.builder()
              .url("http://argo-birthsex")
              .valueCode("M")
              .build());
      return extensions;
    }

  }
}
