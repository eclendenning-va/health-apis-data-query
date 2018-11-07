package gov.va.api.health.argonaut.service.controller.patient;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.Address;
import gov.va.api.health.argonaut.api.CodeableConcept;
import gov.va.api.health.argonaut.api.Coding;
import gov.va.api.health.argonaut.api.Contact;
import gov.va.api.health.argonaut.api.ContactPoint;
import gov.va.api.health.argonaut.api.Extension;
import gov.va.api.health.argonaut.api.HumanName;
import gov.va.api.health.argonaut.api.Identifier;
import gov.va.api.health.argonaut.api.Reference;
import gov.va.api.health.argonaut.service.controller.Transformers;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.SneakyThrows;
import org.junit.Test;

public class PatientTransformerTest {

  private XmlSampleData data = new XmlSampleData();
  private PatientSampleData patient = new PatientSampleData();

  @Test
  public void addressReturnsNullForNull() {
    assertThat(transformer().address(null)).isNull();
  }

  @Test
  public void addressTransformsToLine() {
    List<String> testLine =
        transformer().addressLine(data.alivePatient().getAddresses().getAddress().get(0));
    List<String> expectedLine = patient.alivePatient().address().get(0).line();
    assertThat(testLine).isEqualTo(expectedLine);
  }

  @Test
  public void addressesTransformsToAddressList() {
    List<Address> testAddresses = transformer().addresses(data.alivePatient().getAddresses());
    List<Address> expectedAddresses = patient.alivePatient().address();
    assertThat(testAddresses).isEqualTo(expectedAddresses);
  }

  @Test
  public void addressessReturnsEmptyListForNull() {
    assertThat(transformer().addresses(null)).isEmpty();
  }

  @Test
  public void argoBirthsex() {
    Optional<Extension> testArgoBirthsex =
        transformer().argoBirthSex(data.alivePatient().getArgoBirthsex());
    Extension expexctedArgoBirthsex = patient.alivePatient().extension().get(2);
    assertThat(testArgoBirthsex.get()).isEqualTo(expexctedArgoBirthsex);
  }

  @Test
  public void argoBirthsexReturnsEmptyForNull() {
    assertThat(transformer().argoBirthSex(null)).isEmpty();
  }

  @Test
  public void argoEthnicityReturnsEmptyForNull() {
    assertThat(transformer().argoEthnicity(Collections.emptyList())).isEmpty();
  }

  @Test
  public void argoEthnicityTransformsToExtensionList() {
    List<Extension> testArgoEthnicity =
        transformer()
            .argonautExtensions(data.alivePatient().getArgoEthnicity().get(0).getExtension());
    List<Extension> expectedArgoEthnicity = patient.alivePatient().extension().get(1).extension();
    assertThat(testArgoEthnicity).isEqualTo(expectedArgoEthnicity);
  }

  @Test
  public void argoEthnicityTransformsToOptionalExtensionList() {
    Optional<Extension> testArgoEthnicity =
        transformer().argoEthnicity(data.alivePatient().getArgoEthnicity());
    Extension expectedArgoEthnicity = patient.alivePatient().extension().get(1);
    assertThat(testArgoEthnicity.get()).isEqualTo(expectedArgoEthnicity);
  }

  @Test
  public void argoPatientExtensionsMissingTransformsToEmptyExtensionList() {
    List<Extension> testPatientExtensions =
        transformer().extensions(Optional.empty(), Optional.empty(), Optional.empty());
    List<Extension> expectedPatientExtensions = new LinkedList<>();
    assertThat(testPatientExtensions).isEqualTo(expectedPatientExtensions);
  }

  @Test
  public void argoPatientExtensionsTransformToExtensionList() {
    List<Extension> testPatientExtensions =
        transformer()
            .extensions(
                transformer().argoRace(data.alivePatient().getArgoRace()),
                transformer().argoEthnicity(data.alivePatient().getArgoEthnicity()),
                transformer().argoBirthSex(data.alivePatient().getArgoBirthsex()));
    List<Extension> expectedPatientExtensions = patient.alivePatient().extension();
    assertThat(testPatientExtensions).isEqualTo(expectedPatientExtensions);
  }

  @Test
  public void argoRaceExtensionTransformsToExtensionList() {
    List<Extension> testRaceExtension =
        transformer().argonautExtensions(data.alivePatient().getArgoRace().get(0).getExtension());
    List<Extension> expectedRaceExtension = patient.alivePatient().extension().get(0).extension();
    assertThat(testRaceExtension).isEqualTo(expectedRaceExtension);
  }

  @Test
  public void argoRaceReturnsEmptyForNull() {
    assertThat(transformer().argoRace(Collections.emptyList())).isEmpty();
  }

  @Test
  public void argoRaceTransformsToOptionalExtensionList() {
    Optional<Extension> testArgoRace = transformer().argoRace(data.alivePatient().getArgoRace());
    Extension expectedArgoRace = patient.alivePatient().extension().get(0);
    assertThat(testArgoRace.get()).isEqualTo(expectedArgoRace);
  }

  @Test
  public void birthDateTransformsToSimpleDateString() {
    String testSimpleDate = Transformers.asDateString(data.alivePatient().getBirthDate());
    String expectedSimpleDate = patient.alivePatient().birthDate();
    assertThat(testSimpleDate).isEqualTo(expectedSimpleDate);
  }

  @Test
  public void contactRelationshipTransformsToCodeableConceptList() {
    List<CodeableConcept> testRelationships =
        transformer()
            .contactRelationship(
                data.alivePatient().getContacts().getContact().get(0).getRelationship());
    List<CodeableConcept> expectedRelationships =
        patient.alivePatient().contact().get(0).relationship();
    assertThat(testRelationships).isEqualTo(expectedRelationships);
  }

  @Test
  public void contactReturnsEmptyListForNull() {
    assertThat(transformer().contact(null)).isEmpty();
  }

  @Test
  public void contactStringNameTransformsToHumanName() {
    PatientTransformer patientTransformer = transformer();
    HumanName testName =
        patientTransformer.humanName(
            data.alivePatient().getContacts().getContact().get(0).getName());
    HumanName expectedName = patient.alivePatient().contact().get(0).name();
    assertThat(testName).isEqualTo(expectedName);
  }

  @Test
  public void contactTelecomTranformsToContactPointList() {
    List<ContactPoint> testTelecoms =
        transformer().contact(data.alivePatient().getContacts().getContact().get(0));
    List<ContactPoint> expectedTelecoms = patient.contact().get(0).telecom();
    assertThat(testTelecoms).isEqualTo(expectedTelecoms);
  }

  @Test
  public void contactTransformsToAddress() {
    Address testAddress =
        transformer().address(data.alivePatient().getContacts().getContact().get(0));
    Address expectedAddress = patient.alivePatient().contact().get(0).address();
    assertThat(testAddress).isEqualTo(expectedAddress);
  }

  @Test
  public void contactTransformsToStringLine() {
    List<String> testLine =
        transformer().addressLine(data.alivePatient().getContacts().getContact().get(0));
    List<String> expectedLine = patient.alivePatient().contact().get(0).address().line();
    assertThat(testLine).isEqualTo(expectedLine);
  }

  @Test
  public void contactsReturnsNullForNull() {
    assertThat(transformer().contacts(null)).isNull();
  }

  @Test
  public void contactsTransformsToContactList() {
    List<Contact> testContacts = transformer().contacts(data.alivePatient().getContacts());
    List<Contact> expectedContacts = patient.contact();
    assertThat(testContacts).isEqualTo(expectedContacts);
  }

  @Test
  public void deceasedDateTimeMissingReturnsNull() {
    String testDateTime = Transformers.asDateTimeString(data.alivePatient().getDeceasedDateTime());
    assertThat(testDateTime).isNull();
  }

  @Test
  public void deceasedDateTimeTransformsToString() {
    String testDateTime = Transformers.asDateTimeString(data.deadPatient().getDeceasedDateTime());
    String expectedDateTime = patient.deceasedPatient().deceasedDateTime();
    assertThat(testDateTime).isEqualTo(expectedDateTime);
  }

  @Test
  public void extensionValueCodingTransformsToCoding() {
    Coding testCoding =
        transformer()
            .valueCoding(
                data.alivePatient().getArgoRace().get(0).getExtension().get(0).getValueCoding());
    Coding expectedCoding =
        patient.alivePatient().extension().get(0).extension().get(0).valueCoding();
    assertThat(testCoding).isEqualTo(expectedCoding);
  }

  @Test
  public void humanNameReturnsNullForNull() {
    assertThat(transformer().humanName(null)).isNull();
  }

  @Test
  public void identifierAssignerTransformsToReference() {
    Reference testReference =
        transformer().identifierAssigner(data.alivePatient().getIdentifier().get(0).getAssigner());
    Reference expectedReference = patient.alivePatient().identifier().get(0).assigner();
    assertThat(testReference).isEqualTo(expectedReference);
  }

  @Test
  public void identifierTransformsToIdentifierUse() {
    Identifier.IdentifierUse testIdentifierUse =
        transformer().identifierUse(data.alivePatient().getIdentifier().get(0));
    Identifier.IdentifierUse expectedIdentifierUse =
        patient.alivePatient().identifier().get(0).use();
    assertThat(testIdentifierUse).isEqualTo(expectedIdentifierUse);
  }

  @Test
  public void identifierTypeCodingListTransformsToCodingList() {
    List<Coding> testCodings =
        transformer()
            .identifierTypeCodings(
                data.alivePatient().getIdentifier().get(0).getType().getCoding());
    List<Coding> expectedCodings = patient.identifier().get(0).type().coding();
    assertThat(testCodings).isEqualTo(expectedCodings);
  }

  @Test
  public void identifierTypeTransfromsToCodeableConcept() {
    CodeableConcept testCodeableConcept =
        transformer().identifierType(data.alivePatient().getIdentifier().get(0).getType());
    CodeableConcept expectedCodeableConcept = patient.alivePatient().identifier().get(0).type();
    assertThat(testCodeableConcept).isEqualTo(expectedCodeableConcept);
  }

  @Test
  public void identifiersTransformsToIdentifiersList() {
    List<Identifier> testIdentifiers =
        transformer().identifiers(data.alivePatient().getIdentifier());
    List<Identifier> expectedIdentifiers = patient.alivePatient().identifier();
    assertThat(testIdentifiers).isEqualTo(expectedIdentifiers);
  }

  @Test
  public void maritalStatusCodingListTransformsToCodingList() {
    List<Coding> testCodingList =
        transformer().maritalStatusCoding(data.alivePatient().getMaritalStatus().getCoding());
    List<Coding> expectedCodingList = patient.alivePatient().maritalStatus().coding();
    assertThat(testCodingList).isEqualTo(expectedCodingList);
  }

  @Test
  public void maritalStatusReturnsNullForNull() {
    assertThat(transformer().maritalStatus(null)).isNull();
  }

  @Test
  public void maritalStatusTransformsToCodeableConcept() {
    CodeableConcept testMaritalStatus = transformer().maritalStatus(data.maritalStatus());
    CodeableConcept expectedMaritalStatus = patient.alivePatient().maritalStatus();
    assertThat(testMaritalStatus).isEqualTo(expectedMaritalStatus);
  }

  @Test
  public void namesReturnsEmptyForNull() {
    assertThat(transformer().names(null)).isEmpty();
  }

  @Test
  public void patient103TransformsToModelPatient() {
    gov.va.api.health.argonaut.api.Patient testPatient = transformer().apply(data.alivePatient());
    gov.va.api.health.argonaut.api.Patient expectedPatient = patient.alivePatient();
    assertThat(testPatient).isEqualTo(expectedPatient);
  }

  @Test
  public void patientStringTransformsToHumanName() {
    List<HumanName> testPatientName = transformer().names(data.alivePatient().getName());
    List<HumanName> expectedPatientName = patient.alivePatient().name();
    assertThat(testPatientName).isEqualTo(expectedPatientName);
  }

  @Test
  public void patientTelecomsTransformsToContactPointList() {
    List<ContactPoint> testTelecoms = transformer().telecoms(data.alivePatient().getTelecoms());
    List<ContactPoint> expectedTelecoms = patient.telecom();
    assertThat(testTelecoms).isEqualTo(expectedTelecoms);
  }

  @Test
  public void relationshipCodingTransformsToCodingList() {
    List<Coding> testCodings =
        transformer()
            .contactRelationshipCoding(
                data.alivePatient()
                    .getContacts()
                    .getContact()
                    .get(0)
                    .getRelationship()
                    .getCoding());
    List<Coding> expectedCodings = patient.contact().get(0).relationship().get(0).coding();
    assertThat(testCodings).isEqualTo(expectedCodings);
  }

  @Test
  public void telecomsReturnsEmptyForNull() {
    assertThat(transformer().telecoms(null)).isEmpty();
  }

  private PatientTransformer transformer() {
    return new PatientTransformer();
  }

  private static class PatientSampleData {

    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private PatientSampleData() {
      datatypeFactory = DatatypeFactory.newInstance();
    }

    List<Address> address() {
      List<Address> addresses = new LinkedList<>();
      addresses.add(Address.builder().state("Missing*").build());
      List<String> line1 = new LinkedList<>();
      line1.add("1234 Test Road");
      line1.add("Testland");
      line1.add("Test POSTGRES");
      addresses.add(
          Address.builder()
              .line(line1)
              .city("Testville")
              .state("Testlina")
              .postalCode("12345")
              .build());
      List<String> line2 = new LinkedList<>();
      line2.add("9876 Fake Lane");
      addresses.add(
          Address.builder()
              .line(line2)
              .city("Fooville")
              .state("Foolina")
              .postalCode("98765")
              .build());
      return addresses;
    }

    gov.va.api.health.argonaut.api.Patient alivePatient() {
      return gov.va.api.health.argonaut.api.Patient.builder()
          .resourceType("Patient")
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

    List<Extension> argoExtensions() {
      List<Extension> extensions = new ArrayList<>(3);

      List<Extension> raceExtensions = new LinkedList<>();
      raceExtensions.add(
          Extension.builder()
              .url("ombTest")
              .valueCoding(
                  Coding.builder()
                      .system("http://test-race")
                      .code("R4C3")
                      .display("tester")
                      .build())
              .build());
      raceExtensions.add(Extension.builder().url("text").valueString("tester").build());

      List<Extension> ethnicityExtensions = new LinkedList<>();
      ethnicityExtensions.add(
          Extension.builder()
              .url("ombTest")
              .valueCoding(
                  Coding.builder()
                      .system("http://test-ethnicity")
                      .code("3THN1C1TY")
                      .display("testa")
                      .build())
              .build());
      ethnicityExtensions.add(Extension.builder().url("text").valueString("testa").build());

      extensions.add(Extension.builder().url("http://test-race").extension(raceExtensions).build());
      extensions.add(
          Extension.builder().url("http://test-ethnicity").extension(ethnicityExtensions).build());
      extensions.add(Extension.builder().url("http://test-birthsex").valueCode("M").build());
      return extensions;
    }

    List<Contact> contact() {
      List<Contact> contacts = new LinkedList<>();

      List<String> line1 = new LinkedList<>();
      line1.add("123 Happy Avenue");
      line1.add("456 Smile Drive");
      line1.add("789 Laughter Lane");
      contacts.add(
          Contact.builder()
              .relationship(relationship())
              .name(HumanName.builder().text("DUCK, DAFFY JOHN").build())
              .telecom(
                  Collections.singletonList(
                      ContactPoint.builder()
                          .system(ContactPoint.ContactPointSystem.phone)
                          .value("9998886666")
                          .build()))
              .address(
                  Address.builder()
                      .line(line1)
                      .city("Happyland")
                      .state("Happylina")
                      .postalCode("12345")
                      .country("USA")
                      .build())
              .build());

      List<String> line2 = new LinkedList<>();
      line2.add("123 Sad Avenue");
      line2.add("456 Frown Drive");
      line2.add("789 Weeping Lane");
      contacts.add(
          Contact.builder()
              .relationship(relationship())
              .name(HumanName.builder().text("ALICE, TEST JANE").build())
              .telecom(
                  Collections.singletonList(
                      ContactPoint.builder()
                          .system(ContactPoint.ContactPointSystem.phone)
                          .value("1112224444")
                          .build()))
              .address(
                  Address.builder()
                      .line(line2)
                      .city("Sadland")
                      .state("Sadlina")
                      .postalCode("98765")
                      .country("USA")
                      .build())
              .build());

      return contacts;
    }

    gov.va.api.health.argonaut.api.Patient deceasedPatient() {
      return gov.va.api.health.argonaut.api.Patient.builder()
          .id("123456789")
          .extension(argoExtensions())
          .identifier(identifier())
          .name(name())
          .telecom(telecom())
          .gender(gov.va.api.health.argonaut.api.Patient.Gender.male)
          .birthDate("2018-11-06")
          .deceasedBoolean(true)
          .deceasedDateTime("2018-11-07")
          .address(address())
          .maritalStatus(maritalStatus())
          .contact(contact())
          .build();
    }

    List<Identifier> identifier() {
      List<Identifier> identifiers = new LinkedList<>();
      identifiers.add(
          Identifier.builder()
              .use(Identifier.IdentifierUse.usual)
              .type(
                  CodeableConcept.builder()
                      .coding(
                          Collections.singletonList(
                              Coding.builder().system("http://test-code").code("C0D3").build()))
                      .build())
              .system("http://test-system")
              .value("123456789")
              .assigner(Reference.builder().display("tester-test-index").build())
              .build());

      return identifiers;
    }

    CodeableConcept maritalStatus() {
      return CodeableConcept.builder()
          .coding(
              Collections.singletonList(
                  Coding.builder()
                      .system("http://hl7.org/fhir/marital-status")
                      .code("M")
                      .display("Married")
                      .build()))
          .text("testMarriage")
          .build();
    }

    List<HumanName> name() {
      return Collections.singletonList(
          HumanName.builder()
              .use(HumanName.NameUse.usual)
              .text("FOOMAN FOO")
              .family(Collections.singletonList("FOO"))
              .given(Collections.singletonList("FOOMAN"))
              .build());
    }

    List<CodeableConcept> relationship() {
      return Collections.singletonList(
          CodeableConcept.builder()
              .coding(
                  Collections.singletonList(
                      Coding.builder()
                          .system("http://hl7.org/fhir/patient-contact-relationship")
                          .code("emergency")
                          .display("Emergency")
                          .build()))
              .text("Emergency Contact")
              .build());
    }

    List<ContactPoint> telecom() {
      List<ContactPoint> telecoms = new LinkedList<>();
      telecoms.add(
          ContactPoint.builder()
              .system(ContactPoint.ContactPointSystem.phone)
              .value("9998886666")
              .use(ContactPoint.ContactPointUse.home)
              .build());
      telecoms.add(
          ContactPoint.builder()
              .system(ContactPoint.ContactPointSystem.phone)
              .value("1112224444")
              .use(ContactPoint.ContactPointUse.work)
              .build());
      return telecoms;
    }
  }

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

    Patient deadPatient() {
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
