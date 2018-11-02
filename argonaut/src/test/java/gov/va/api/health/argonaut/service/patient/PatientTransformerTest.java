package gov.va.api.health.argonaut.service.patient;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import gov.va.api.health.argonaut.service.controller.patient.PatientTransformer;
import gov.va.dvp.cdw.xsd.pojos.*;
import java.math.BigInteger;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Before;
import org.junit.Test;

public class PatientTransformerTest {

  private Patient103Root.Patients.Patient testPatient;

  @Before
  public void setTestPatient() {
    testPatient = new Patient103Root.Patients.Patient();
    testPatient.setRowNumber(new BigInteger("1"));
    testPatient.setCdwId("123456789");
    testPatient.getArgoRace().add(getExtensions());
    testPatient.getArgoEthnicity().add(getExtensions());
    testPatient.setArgoBirthsex(getBirthSexExtension());
    testPatient.getIdentifier().add(getIdentifier());
    testPatient.setName(getName());
    testPatient.setTelecoms(getTelecoms());
    testPatient.setAddresses(getAddresses());
    testPatient.setGender(AdministrativeGenderCodes.UNKNOWN);
    testPatient.setBirthDate(getBirthdate());
    testPatient.setMaritalStatus(getMaritalStatus());
    testPatient.setContacts(getContacts());
  }

  @Test
  public void alivePatient103RootToPatient() {
    PatientTransformer transformer = new PatientTransformer();
    testPatient.setDeceasedBoolean(false);
    transformer.apply(testPatient);
  }

  @Test
  public void deceasedPatient103RootToPatient() {
    PatientTransformer transformer = new PatientTransformer();
    testPatient.setDeceasedDateTime(getDeceasedDateTime());
    transformer.apply(testPatient);
  }

  @Test
  public void testArgoPatientExtension() {
    PatientTransformer transformer = new PatientTransformer();
    testPatient.getArgoRace().clear();
    testPatient.getArgoEthnicity().clear();
    testPatient.setArgoBirthsex(null);
    transformer.apply(testPatient);
  }

  private Patient103Root.Patients.Patient.Contacts getContacts() {
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

  private Patient103Root.Patients.Patient.MaritalStatus getMaritalStatus() {
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

  private XMLGregorianCalendarImpl getBirthdate() {
    XMLGregorianCalendarImpl birthdate = new XMLGregorianCalendarImpl();
    birthdate.setYear(2088);
    birthdate.setMonth(11);
    birthdate.setDay(3);
    birthdate.setTimezone(0);
    birthdate.setHour(6);
    birthdate.setMinute(0);
    birthdate.setSecond(9);
    return birthdate;
  }

  private Patient103Root.Patients.Patient.Addresses getAddresses() {
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

  private Patient103Root.Patients.Patient.Telecoms.Telecom getTelecom() {
    Patient103Root.Patients.Patient.Telecoms.Telecom telecom =
        new Patient103Root.Patients.Patient.Telecoms.Telecom();
    telecom.setSystem(ContactPointSystemCodes.PHONE);
    telecom.setValue("987345126");
    telecom.setUse(ContactPointUseCodes.HOME);
    return telecom;
  }

  private Patient103Root.Patients.Patient.Name getName() {
    Patient103Root.Patients.Patient.Name name = new Patient103Root.Patients.Patient.Name();
    name.setUse("usual");
    name.setText("FOOMAN FOO");
    name.setFamily("FOO");
    name.setGiven("FOOMAN");
    return name;
  }

  private Patient103Root.Patients.Patient.Identifier getIdentifier() {
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

  private Extensions getExtensions() {
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

  private Extensions.Extension.ValueCoding valueCoding() {
    Extensions.Extension.ValueCoding valueCoding = new Extensions.Extension.ValueCoding();
    valueCoding.setCode("testcode");
    valueCoding.setDisplay("testdisplay");
    valueCoding.setSystem("testsystem");
    return valueCoding;
  }

  private BirthsexExtension getBirthSexExtension() {
    BirthsexExtension testBirthSexExtension = new BirthsexExtension();
    testBirthSexExtension.setValueCode(BirthSexCodes.UNK);
    testBirthSexExtension.setUrl("http://www.testville.com");
    return testBirthSexExtension;
  }

  private XMLGregorianCalendar getDeceasedDateTime() {
    XMLGregorianCalendarImpl deceasedDate = new XMLGregorianCalendarImpl();
    deceasedDate.setYear(2088);
    deceasedDate.setMonth(11);
    deceasedDate.setDay(3);
    deceasedDate.setTimezone(0);
    deceasedDate.setHour(6);
    deceasedDate.setMinute(0);
    deceasedDate.setSecond(9);
    return deceasedDate;
  }

  private Patient103Root.Patients.Patient.Telecoms getTelecoms() {
    Patient103Root.Patients.Patient.Telecoms testTelecoms =
        new Patient103Root.Patients.Patient.Telecoms();
    testTelecoms.getTelecom().add(getTelecom());
    return testTelecoms;
  }
}
