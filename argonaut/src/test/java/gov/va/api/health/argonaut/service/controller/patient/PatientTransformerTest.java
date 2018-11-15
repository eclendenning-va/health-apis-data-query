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
import gov.va.dvp.cdw.xsd.model.CdwAdministrativeGenderCodes;
import gov.va.dvp.cdw.xsd.model.CdwBirthSexCodes;
import gov.va.dvp.cdw.xsd.model.CdwBirthsexExtension;
import gov.va.dvp.cdw.xsd.model.CdwContactPointSystemCodes;
import gov.va.dvp.cdw.xsd.model.CdwContactPointUseCodes;
import gov.va.dvp.cdw.xsd.model.CdwExtensions;
import gov.va.dvp.cdw.xsd.model.CdwExtensions.CdwExtension;
import gov.va.dvp.cdw.xsd.model.CdwIdentifierUseCodes;
import gov.va.dvp.cdw.xsd.model.CdwMaritalStatusCodes;
import gov.va.dvp.cdw.xsd.model.CdwMaritalStatusSystems;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwAddresses;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwAddresses.CdwAddress;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwContacts;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient.CdwContacts.CdwContact;
import gov.va.dvp.cdw.xsd.model.CdwPatientContactRelationshipCodes;
import gov.va.dvp.cdw.xsd.model.CdwPatientContactRelationshipSystem;
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

  private XmlSampleData cdw = new XmlSampleData();
  private PatientSampleData expectedPatient = new PatientSampleData();

  @Test
  public void addressReturnsNullForNull() {
    assertThat(transformer().address(null)).isNull();
  }

  @Test
  public void addressTransformsToLine() {
    List<String> testLine =
        transformer().addressLine(cdw.alivePatient().getAddresses().getAddress().get(0));
    List<String> expectedLine = expectedPatient.alivePatient().address().get(0).line();
    assertThat(testLine).isEqualTo(expectedLine);
  }

  @Test
  public void addressesTransformsToAddressList() {
    List<Address> testAddresses = transformer().addresses(cdw.alivePatient().getAddresses());
    List<Address> expectedAddresses = expectedPatient.alivePatient().address();
    assertThat(testAddresses).isEqualTo(expectedAddresses);
  }

  @Test
  public void addressessReturnsEmptyListForNull() {
    assertThat(transformer().addresses(null)).isEmpty();
  }

  @Test
  public void argoBirthsex() {
    Optional<Extension> testArgoBirthsex =
        transformer().argoBirthSex(cdw.alivePatient().getArgoBirthsex());
    Extension expexctedArgoBirthsex = expectedPatient.alivePatient().extension().get(2);
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
            .argonautExtensions(cdw.alivePatient().getArgoEthnicity().get(0).getExtension());
    List<Extension> expectedArgoEthnicity =
        expectedPatient.alivePatient().extension().get(1).extension();
    assertThat(testArgoEthnicity).isEqualTo(expectedArgoEthnicity);
  }

  @Test
  public void argoEthnicityTransformsToOptionalExtensionList() {
    Optional<Extension> testArgoEthnicity =
        transformer().argoEthnicity(cdw.alivePatient().getArgoEthnicity());
    Extension expectedArgoEthnicity = expectedPatient.alivePatient().extension().get(1);
    assertThat(testArgoEthnicity.get()).isEqualTo(expectedArgoEthnicity);
  }

  @Test
  public void argoPatientCdwExtensionsMissingTransformsToEmptyExtensionList() {
    List<Extension> testPatientCdwExtensions =
        transformer().extensions(Optional.empty(), Optional.empty(), Optional.empty());
    List<Extension> expectedPatientCdwExtensions = new LinkedList<>();
    assertThat(testPatientCdwExtensions).isEqualTo(expectedPatientCdwExtensions);
  }

  @Test
  public void argoPatientCdwExtensionsTransformToExtensionList() {
    List<Extension> testPatientCdwExtensions =
        transformer()
            .extensions(
                transformer().argoRace(cdw.alivePatient().getArgoRace()),
                transformer().argoEthnicity(cdw.alivePatient().getArgoEthnicity()),
                transformer().argoBirthSex(cdw.alivePatient().getArgoBirthsex()));
    List<Extension> expectedPatientCdwExtensions = expectedPatient.alivePatient().extension();
    assertThat(testPatientCdwExtensions).isEqualTo(expectedPatientCdwExtensions);
  }

  @Test
  public void argoRaceExtensionTransformsToExtensionList() {
    List<Extension> testRaceExtension =
        transformer().argonautExtensions(cdw.alivePatient().getArgoRace().get(0).getExtension());
    List<Extension> expectedRaceExtension =
        expectedPatient.alivePatient().extension().get(0).extension();
    assertThat(testRaceExtension).isEqualTo(expectedRaceExtension);
  }

  @Test
  public void argoRaceReturnsEmptyForNull() {
    assertThat(transformer().argoRace(Collections.emptyList())).isEmpty();
  }

  @Test
  public void argoRaceTransformsToOptionalExtensionList() {
    Optional<Extension> testArgoRace = transformer().argoRace(cdw.alivePatient().getArgoRace());
    Extension expectedArgoRace = expectedPatient.alivePatient().extension().get(0);
    assertThat(testArgoRace.get()).isEqualTo(expectedArgoRace);
  }

  @Test
  public void birthDateTransformsToSimpleDateString() {
    String testSimpleDate = Transformers.asDateString(cdw.alivePatient().getBirthDate());
    String expectedSimpleDate = expectedPatient.alivePatient().birthDate();
    assertThat(testSimpleDate).isEqualTo(expectedSimpleDate);
  }

  @Test
  public void contactRelationshipTransformsToCodeableConceptList() {
    List<CodeableConcept> testRelationships =
        transformer()
            .contactRelationship(
                cdw.alivePatient().getContacts().getContact().get(0).getRelationship());
    List<CodeableConcept> expectedRelationships =
        expectedPatient.alivePatient().contact().get(0).relationship();
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
            cdw.alivePatient().getContacts().getContact().get(0).getName());
    HumanName expectedName = expectedPatient.alivePatient().contact().get(0).name();
    assertThat(testName).isEqualTo(expectedName);
  }

  @Test
  public void contactTelecomTranformsToContactPointList() {
    List<ContactPoint> testTelecoms =
        transformer().contact(cdw.alivePatient().getContacts().getContact().get(0));
    List<ContactPoint> expectedTelecoms = expectedPatient.contact().get(0).telecom();
    assertThat(testTelecoms).isEqualTo(expectedTelecoms);
  }

  @Test
  public void contactTransformsToAddress() {
    Address testAddress =
        transformer().address(cdw.alivePatient().getContacts().getContact().get(0));
    Address expectedAddress = expectedPatient.alivePatient().contact().get(0).address();
    assertThat(testAddress).isEqualTo(expectedAddress);
  }

  @Test
  public void contactTransformsToStringLine() {
    List<String> testLine =
        transformer().addressLine(cdw.alivePatient().getContacts().getContact().get(0));
    List<String> expectedLine = expectedPatient.alivePatient().contact().get(0).address().line();
    assertThat(testLine).isEqualTo(expectedLine);
  }

  @Test
  public void contactsReturnsNullForNull() {
    assertThat(transformer().contacts(null)).isNull();
  }

  @Test
  public void contactsTransformsToContactList() {
    List<Contact> testContacts = transformer().contacts(cdw.alivePatient().getContacts());
    List<Contact> expectedContacts = expectedPatient.contact();
    assertThat(testContacts).isEqualTo(expectedContacts);
  }

  @Test
  public void deceasedDateTimeMissingReturnsNull() {
    String testDateTime = Transformers.asDateTimeString(cdw.alivePatient().getDeceasedDateTime());
    assertThat(testDateTime).isNull();
  }

  @Test
  public void deceasedDateTimeTransformsToString() {
    String testDateTime = Transformers.asDateTimeString(cdw.deadPatient().getDeceasedDateTime());
    String expectedDateTime = expectedPatient.deceasedPatient().deceasedDateTime();
    assertThat(testDateTime).isEqualTo(expectedDateTime);
  }

  @Test
  public void extensionValueCodingTransformsToCoding() {
    Coding testCoding =
        transformer()
            .valueCoding(
                cdw.alivePatient().getArgoRace().get(0).getExtension().get(0).getValueCoding());
    Coding expectedCoding =
        expectedPatient.alivePatient().extension().get(0).extension().get(0).valueCoding();
    assertThat(testCoding).isEqualTo(expectedCoding);
  }

  @Test
  public void humanNameReturnsNullForNull() {
    assertThat(transformer().humanName(null)).isNull();
  }

  @Test
  public void identifierAssignerTransformsToReference() {
    Reference testReference =
        transformer().identifierAssigner(cdw.alivePatient().getIdentifier().get(0).getAssigner());
    Reference expectedReference = expectedPatient.alivePatient().identifier().get(0).assigner();
    assertThat(testReference).isEqualTo(expectedReference);
  }

  @Test
  public void identifierTransformsToIdentifierUse() {
    Identifier.IdentifierUse testIdentifierUse =
        transformer().identifierUse(cdw.alivePatient().getIdentifier().get(0));
    Identifier.IdentifierUse expectedIdentifierUse =
        expectedPatient.alivePatient().identifier().get(0).use();
    assertThat(testIdentifierUse).isEqualTo(expectedIdentifierUse);
  }

  @Test
  public void identifierTypeCodingListTransformsToCodingList() {
    List<Coding> testCodings =
        transformer()
            .identifierTypeCodings(cdw.alivePatient().getIdentifier().get(0).getType().getCoding());
    List<Coding> expectedCodings = expectedPatient.identifier().get(0).type().coding();
    assertThat(testCodings).isEqualTo(expectedCodings);
  }

  @Test
  public void identifierTypeTransfromsToCodeableConcept() {
    CodeableConcept testCodeableConcept =
        transformer().identifierType(cdw.alivePatient().getIdentifier().get(0).getType());
    CodeableConcept expectedCodeableConcept =
        expectedPatient.alivePatient().identifier().get(0).type();
    assertThat(testCodeableConcept).isEqualTo(expectedCodeableConcept);
  }

  @Test
  public void identifiersTransformsToIdentifiersList() {
    List<Identifier> testIdentifiers =
        transformer().identifiers(cdw.alivePatient().getIdentifier());
    List<Identifier> expectedIdentifiers = expectedPatient.alivePatient().identifier();
    assertThat(testIdentifiers).isEqualTo(expectedIdentifiers);
  }

  @Test
  public void maritalStatusCodingListTransformsToCodingList() {
    List<Coding> testCodingList =
        transformer().maritalStatusCoding(cdw.alivePatient().getMaritalStatus().getCoding());
    List<Coding> expectedCodingList = expectedPatient.alivePatient().maritalStatus().coding();
    assertThat(testCodingList).isEqualTo(expectedCodingList);
  }

  @Test
  public void maritalStatusReturnsNullForNull() {
    assertThat(transformer().maritalStatus(null)).isNull();
  }

  @Test
  public void maritalStatusTransformsToCodeableConcept() {
    CodeableConcept testCdwMaritalStatus = transformer().maritalStatus(cdw.maritalStatus());
    CodeableConcept expectedCdwMaritalStatus = expectedPatient.alivePatient().maritalStatus();
    assertThat(testCdwMaritalStatus).isEqualTo(expectedCdwMaritalStatus);
  }

  @Test
  public void namesReturnsEmptyForNull() {
    assertThat(transformer().names(null)).isEmpty();
  }

  @Test
  public void patient103TransformsToModelPatient() {
    gov.va.api.health.argonaut.api.Patient test = transformer().apply(cdw.alivePatient());
    gov.va.api.health.argonaut.api.Patient expected = expectedPatient.alivePatient();
    assertThat(test).isEqualTo(expected);
  }

  @Test
  public void patientStringTransformsToHumanName() {
    List<HumanName> testPatientName = transformer().names(cdw.alivePatient().getName());
    List<HumanName> expectedPatientName = expectedPatient.alivePatient().name();
    assertThat(testPatientName).isEqualTo(expectedPatientName);
  }

  @Test
  public void patientTelecomsTransformsToContactPointList() {
    List<ContactPoint> testTelecoms = transformer().telecoms(cdw.alivePatient().getTelecoms());
    List<ContactPoint> expectedTelecoms = expectedPatient.telecom();
    assertThat(testTelecoms).isEqualTo(expectedTelecoms);
  }

  @Test
  public void relationshipCodingTransformsToCodingList() {
    List<Coding> testCodings =
        transformer()
            .contactRelationshipCoding(
                cdw.alivePatient().getContacts().getContact().get(0).getRelationship().getCoding());
    List<Coding> expectedCodings = expectedPatient.contact().get(0).relationship().get(0).coding();
    assertThat(testCodings).isEqualTo(expectedCodings);
  }

  @Test
  public void telecomsReturnsEmptyForNull() {
    assertThat(transformer().telecoms(null)).isEmpty();
  }

  private PatientTransformer transformer() {
    return new PatientTransformer();
  }

  static class PatientSampleData {

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
          .extension(argoCdwExtensions())
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

    List<Extension> argoCdwExtensions() {
      List<Extension> CdwExtensions = new ArrayList<>(3);

      List<Extension> raceCdwExtensions = new LinkedList<>();
      raceCdwExtensions.add(
          Extension.builder()
              .url("ombTest")
              .valueCoding(
                  Coding.builder()
                      .system("http://test-race")
                      .code("R4C3")
                      .display("tester")
                      .build())
              .build());
      raceCdwExtensions.add(Extension.builder().url("text").valueString("tester").build());

      List<Extension> ethnicityCdwExtensions = new LinkedList<>();
      ethnicityCdwExtensions.add(
          Extension.builder()
              .url("ombTest")
              .valueCoding(
                  Coding.builder()
                      .system("http://test-ethnicity")
                      .code("3THN1C1TY")
                      .display("testa")
                      .build())
              .build());
      ethnicityCdwExtensions.add(Extension.builder().url("text").valueString("testa").build());

      CdwExtensions.add(
          Extension.builder().url("http://test-race").extension(raceCdwExtensions).build());
      CdwExtensions.add(
          Extension.builder()
              .url("http://test-ethnicity")
              .extension(ethnicityCdwExtensions)
              .build());
      CdwExtensions.add(Extension.builder().url("http://test-birthsex").valueCode("M").build());
      return CdwExtensions;
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
          .extension(argoCdwExtensions())
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

    CdwBirthsexExtension CdwBirthsexExtension() {
      CdwBirthsexExtension testCdwBirthsexExtension = new CdwBirthsexExtension();
      testCdwBirthsexExtension.setValueCode(CdwBirthSexCodes.M);
      testCdwBirthsexExtension.setUrl("http://test-birthsex");
      return testCdwBirthsexExtension;
    }

    CdwAddresses addresses() {

      CdwAddresses addresses = new CdwAddresses();

      CdwAddress address = new CdwAddress();
      address.setState("Missing*");
      addresses.getAddress().add(address);

      address = new CdwAddress();
      address.setStreetAddress1("1234 Test Road");
      address.setStreetAddress2("Testland");
      address.setStreetAddress3("Test POSTGRES");
      address.setCity("Testville");
      address.setState("Testlina");
      address.setPostalCode("12345");
      addresses.getAddress().add(address);

      address = new CdwAddress();
      address.setStreetAddress1("9876 Fake Lane");
      address.setCity("Fooville");
      address.setState("Foolina");
      address.setPostalCode("98765");
      addresses.getAddress().add(address);

      return addresses;
    }

    CdwPatient alivePatient() {
      CdwPatient patient = new CdwPatient();
      patient.setRowNumber(new BigInteger("1"));
      patient.setCdwId("123456789");
      patient.getArgoRace().add(raceCdwExtensions());
      patient.getArgoEthnicity().add(ethnicityCdwExtensions());
      patient.setArgoBirthsex(CdwBirthsexExtension());
      patient.getIdentifier().add(identifier());
      patient.setName(name());
      patient.setTelecoms(telecoms());
      patient.setAddresses(addresses());
      patient.setGender(CdwAdministrativeGenderCodes.MALE);
      patient.setBirthDate(birthdate());
      patient.setDeceasedBoolean(false);
      patient.setMaritalStatus(maritalStatus());
      patient.setContacts(contacts());
      return patient;
    }

    @SneakyThrows
    XMLGregorianCalendar birthdate() {
      XMLGregorianCalendar birthdate = datatypeFactory.newXMLGregorianCalendar();
      birthdate.setYear(2018);
      birthdate.setMonth(11);
      birthdate.setDay(06);
      return birthdate;
    }

    CdwContacts contacts() {
      CdwContacts contacts = new CdwContacts();

      CdwContact contact1 = new CdwContact();

      CdwContact.CdwRelationship relationship1 = new CdwContact.CdwRelationship();

      CdwContact.CdwRelationship.CdwCoding relationshipCoding1 =
          new CdwContact.CdwRelationship.CdwCoding();
      relationshipCoding1.setSystem(
          CdwPatientContactRelationshipSystem.HTTP_HL_7_ORG_FHIR_PATIENT_CONTACT_RELATIONSHIP);
      relationshipCoding1.setCode(CdwPatientContactRelationshipCodes.EMERGENCY);
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

      CdwContact contact2 = new CdwContact();

      CdwContact.CdwRelationship relationship2 = new CdwContact.CdwRelationship();

      CdwContact.CdwRelationship.CdwCoding relationshipCoding2 =
          new CdwContact.CdwRelationship.CdwCoding();
      relationshipCoding2.setSystem(
          CdwPatientContactRelationshipSystem.HTTP_HL_7_ORG_FHIR_PATIENT_CONTACT_RELATIONSHIP);
      relationshipCoding2.setCode(CdwPatientContactRelationshipCodes.EMERGENCY);
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

    CdwPatient deadPatient() {
      CdwPatient patient = new CdwPatient();
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

    CdwExtensions ethnicityCdwExtensions() {
      CdwExtensions extensions = new CdwExtensions();
      extensions.setUrl("http://test-ethnicity");

      CdwExtension extension1 = new CdwExtension();
      extension1.setUrl("ombTest");

      CdwExtension.CdwValueCoding valueCoding = new CdwExtension.CdwValueCoding();
      valueCoding.setSystem("http://test-ethnicity");
      valueCoding.setCode("3THN1C1TY");
      valueCoding.setDisplay("testa");
      extension1.setValueCoding(valueCoding);

      extensions.getExtension().add(extension1);

      CdwExtension extension2 = new CdwExtension();
      extension2.setUrl("text");
      extension2.setValueString("testa");

      extensions.getExtension().add(extension2);
      return extensions;
    }

    CdwPatient.CdwIdentifier identifier() {
      CdwPatient.CdwIdentifier identifier = new CdwPatient.CdwIdentifier();
      identifier.setUse(CdwIdentifierUseCodes.USUAL);
      CdwPatient.CdwIdentifier.CdwType type = new CdwPatient.CdwIdentifier.CdwType();
      CdwPatient.CdwIdentifier.CdwType.CdwCoding coding =
          new CdwPatient.CdwIdentifier.CdwType.CdwCoding();
      coding.setSystem("http://test-code");
      coding.setCode("C0D3");
      type.getCoding().add(coding);
      identifier.setType(type);
      identifier.setSystem("http://test-system");
      identifier.setValue("123456789");
      CdwPatient.CdwIdentifier.CdwAssigner assigner = new CdwPatient.CdwIdentifier.CdwAssigner();
      assigner.setDisplay("tester-test-index");
      identifier.setAssigner(assigner);
      return identifier;
    }

    CdwPatient.CdwMaritalStatus maritalStatus() {
      CdwPatient.CdwMaritalStatus maritalStatus = new CdwPatient.CdwMaritalStatus();
      maritalStatus.setText("testMarriage");
      CdwPatient.CdwMaritalStatus.CdwCoding coding = new CdwPatient.CdwMaritalStatus.CdwCoding();
      coding.setSystem(CdwMaritalStatusSystems.HTTP_HL_7_ORG_FHIR_MARITAL_STATUS);
      coding.setCode(CdwMaritalStatusCodes.M);
      coding.setDisplay("Married");
      maritalStatus.getCoding().add(coding);
      return maritalStatus;
    }

    CdwPatient.CdwName name() {
      CdwPatient.CdwName name = new CdwPatient.CdwName();
      name.setUse("usual");
      name.setText("FOOMAN FOO");
      name.setFamily("FOO");
      name.setGiven("FOOMAN");
      return name;
    }

    CdwExtensions raceCdwExtensions() {
      CdwExtensions CdwExtensions = new CdwExtensions();
      CdwExtensions.setUrl("http://test-race");

      CdwExtension extension1 = new CdwExtension();
      extension1.setUrl("ombTest");

      CdwExtension.CdwValueCoding valueCoding = new CdwExtension.CdwValueCoding();
      valueCoding.setSystem("http://test-race");
      valueCoding.setCode("R4C3");
      valueCoding.setDisplay("tester");
      extension1.setValueCoding(valueCoding);

      CdwExtensions.getExtension().add(extension1);

      CdwExtension extension2 = new CdwExtension();
      extension2.setUrl("text");
      extension2.setValueString("tester");

      CdwExtensions.getExtension().add(extension2);
      return CdwExtensions;
    }

    CdwPatient.CdwTelecoms telecoms() {
      CdwPatient.CdwTelecoms testTelecoms = new CdwPatient.CdwTelecoms();

      CdwPatient.CdwTelecoms.CdwTelecom telecom1 = new CdwPatient.CdwTelecoms.CdwTelecom();
      telecom1.setSystem(CdwContactPointSystemCodes.PHONE);
      telecom1.setValue("9998886666");
      telecom1.setUse(CdwContactPointUseCodes.HOME);
      testTelecoms.getTelecom().add(telecom1);

      CdwPatient.CdwTelecoms.CdwTelecom telecom2 = new CdwPatient.CdwTelecoms.CdwTelecom();
      telecom2.setSystem(CdwContactPointSystemCodes.PHONE);
      telecom2.setValue("1112224444");
      telecom2.setUse(CdwContactPointUseCodes.WORK);
      testTelecoms.getTelecom().add(telecom2);
      return testTelecoms;
    }
  }
}
