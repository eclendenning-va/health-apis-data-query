package gov.va.api.health.argonaut.service.controller.condition;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.api.resources.Condition.ClinicalStatusCode;
import gov.va.api.health.argonaut.api.resources.Condition.VerificationStatusCode;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwCondition103Root.CdwConditions.CdwCondition;
import gov.va.dvp.cdw.xsd.model.CdwCondition103Root.CdwConditions.CdwCondition.CdwCategory;
import gov.va.dvp.cdw.xsd.model.CdwConditionCategoryCoding;
import gov.va.dvp.cdw.xsd.model.CdwConditionClinicalStatus;
import gov.va.dvp.cdw.xsd.model.CdwConditionVerificationStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

public class ConditionTransformerTest {

  private final ConditionTransformer transformer = new ConditionTransformer();
  private final CdwSampleData cdw = CdwSampleData.get();
  private final Expected expected = Expected.get();

  @Test
  public void category() {
    assertThat(transformer.category(null)).isNull();
    assertThat(transformer.category(new CdwCategory())).isNull();
    assertThat(transformer.category(cdw.category())).isEqualTo(expected.category());
  }

  @Test
  public void condition() {
    assertThat(transformer.apply(cdw.condition())).isEqualTo(expected.condition());
    assertThat(transformer.apply(cdw.condition())).isEqualTo(expected.condition());
  }

  @Test
  public void clinicalStatusCode() {
    assertThat(transformer.clinicalStatusCode(CdwConditionClinicalStatus.ACTIVE))
        .isEqualTo(ClinicalStatusCode.active);
    assertThat(transformer.clinicalStatusCode(CdwConditionClinicalStatus.RELAPSE))
        .isEqualTo(ClinicalStatusCode.relapse);
    assertThat(transformer.clinicalStatusCode(CdwConditionClinicalStatus.REMISSION))
        .isEqualTo(ClinicalStatusCode.remission);
    assertThat(transformer.clinicalStatusCode(CdwConditionClinicalStatus.RESOLVED))
        .isEqualTo(ClinicalStatusCode.resolved);
  }

  @Test
  public void verificationStatusCode() {
    assertThat(transformer.verificationStatusCode(CdwConditionVerificationStatus.CONFIRMED))
        .isEqualTo(VerificationStatusCode.confirmed);
    assertThat(transformer.verificationStatusCode(CdwConditionVerificationStatus.DIFFERENTIAL))
        .isEqualTo(VerificationStatusCode.differential);
    assertThat(transformer.verificationStatusCode(CdwConditionVerificationStatus.ENTERED_IN_ERROR))
        .isEqualTo(VerificationStatusCode.entered_in_error);
    assertThat(transformer.verificationStatusCode(CdwConditionVerificationStatus.PROVISIONAL))
        .isEqualTo(VerificationStatusCode.provisional);
    assertThat(transformer.verificationStatusCode(CdwConditionVerificationStatus.REFUTED))
        .isEqualTo(VerificationStatusCode.refuted);
    assertThat(transformer.verificationStatusCode(CdwConditionVerificationStatus.UNKNOWN))
        .isEqualTo(VerificationStatusCode.unknown);
  }

  @Test
  public void code() {
    assertThat(transformer.code(null)).isNull();
    assertThat(transformer.code(singletonList(cdw.code()))).isEqualTo(expected.code());
  }

  @Test
  public void reference() {
    assertThat(transformer.reference(null)).isNull();
    assertThat(transformer.reference(cdw.reference("x", "y")))
        .isEqualTo(expected.reference("x", "y"));
  }

  @NoArgsConstructor(staticName = "get")
  private static class CdwSampleData {

    private CdwCategory category() {
      CdwCategory cdw = new CdwCategory();
      cdw.getCoding().add(categoryCoding());
      return cdw;
    }

    CdwConditionCategoryCoding categoryCoding() {
      CdwConditionCategoryCoding coding = new CdwConditionCategoryCoding();
      coding.setSystem("http://hl7.org/fhir/condition-category");
      coding.setCode("diagnosis");
      coding.setDisplay("Diagnosis");
      return coding;
    }

    private CdwCodeableConcept code() {
      CdwCodeableConcept cdw = new CdwCodeableConcept();
      cdw.setText("Asthma");
      cdw.getCoding().add(coding());
      return cdw;
    }

    private CdwCoding coding() {
      CdwCoding cdw = new CdwCoding();
      cdw.setSystem("http://www.cdc.gov/nchs/icd/icd9.htm");
      cdw.setCode("493.20");
      cdw.setDisplay("CHRONIC OBSTRUCTIVE ASTHMA, UNSPECIFIED");
      return cdw;
    }

    private CdwReference reference(String ref, String display) {
      CdwReference cdw = new CdwReference();
      cdw.setReference(ref);
      cdw.setDisplay(display);
      return cdw;
    }

    @SneakyThrows
    private XMLGregorianCalendar dateTime(String timestamp) {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(timestamp);
    }

    CdwCondition condition() {
      CdwCondition condition = new CdwCondition();
      condition.setCdwId("1000000289360:P");
      condition.setRowNumber(BigInteger.ONE);
      condition.getCode().add(code());
      condition.setAbatementDateTime(dateTime("2013-06-21T20:05:12Z"));
      condition.setAsserter(reference("Practitioner/1234", "Asserter"));
      condition.setCategory(category());
      condition.setClinicalStatus(CdwConditionClinicalStatus.ACTIVE);
      condition.setDateRecorded(dateTime("2013-06-21"));
      condition.setEncounter(reference("Encounter/1234", "The 3rd Kind"));
      condition.setOnsetDateTime(dateTime("2013-06-21T20:05:12Z"));
      condition.setPatient(reference("Patient/185601V825290", "VETERAN,JOHN Q"));
      condition.setVerificationStatus(CdwConditionVerificationStatus.CONFIRMED);
      return condition;
    }
  }

  @NoArgsConstructor(staticName = "get")
  private static class Expected {
    CodeableConcept category() {
      return codeableConcept(
          coding("http://hl7.org/fhir/condition-category", "diagnosis", "Diagnosis"));
    }

    CodeableConcept code() {
      return codeableConcept(
              coding(
                  "http://www.cdc.gov/nchs/icd/icd9.htm",
                  "493.20",
                  "CHRONIC OBSTRUCTIVE ASTHMA, UNSPECIFIED"))
          .text("Asthma");
    }

    CodeableConcept codeableConcept(Coding coding) {
      return CodeableConcept.builder().coding(singletonList(coding)).build();
    }

    Coding coding(String system, String code, String display) {
      return Coding.builder().system(system).code(code).display(display).build();
    }

    Reference reference(String ref, String display) {
      return Reference.builder().reference(ref).display(display).build();
    }

    Condition condition() {
      return Condition.builder()
          .resourceType("Condition")
          .id("1000000289360:P")
          .abatementDateTime("2013-06-21T20:05:12Z")
          .asserter(reference("Practitioner/1234", "Asserter"))
          .category(category())
          .code(code())
          .clinicalStatus(ClinicalStatusCode.active)
          .dateRecorded("2013-06-21")
          .encounter(reference("Encounter/1234", "The 3rd Kind"))
          .onsetDateTime("2013-06-21T20:05:12Z")
          .patient(reference("Patient/185601V825290", "VETERAN,JOHN Q"))
          .verificationStatus(VerificationStatusCode.confirmed)
          .build();
    }
  }
}
