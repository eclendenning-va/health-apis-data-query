package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport.Code;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCategory;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCategoryCode;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCategoryCoding;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCategoryDisplay;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCode;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportCodeCoding;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportFhirVersionValue;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportResourceNameValue;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReportStatus;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import gov.va.dvp.cdw.xsd.model.CdwReturnFormatCodes;
import gov.va.dvp.cdw.xsd.model.CdwReturnTypeCodes;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

public class DiagnosticReportTransformerTest {

  private final DiagnosticReportTransformer tx = new DiagnosticReportTransformer();
  private final CdwSampleData cdw = new CdwSampleData();
  private final Expected expected = new Expected();

  @Test
  public void categoryCoding() {
    assertThat(tx.categoryCodings(null)).isNull();
    assertThat(tx.categoryCodings(new CdwDiagnosticReportCategoryCoding())).isNull();
    assertThat(tx.categoryCodings(cdw.categoryCoding()))
        .isEqualTo(expected.categoryCoding());
  }

  @Test
  public void category() {
    assertThat(tx.category(cdw.category())).isEqualTo(expected.category());
  }

  @Test
  public void cdwReference() {
    assertThat(tx.reference(null)).isNull();
    assertThat(tx.reference(new CdwReference())).isNull();
    assertThat(tx.reference(cdw.cdwReference())).isEqualTo(expected.reference());
  }

  @Test
  public void codeCodings() {
    assertThat(tx.codeCodings(null)).isNull();
    assertThat(tx.codeCodings(Collections.singletonList(new CdwDiagnosticReportCodeCoding()))).isNull();
    assertThat(tx.codeCodings(null)).isNull();
  }

  @Test
  public void code() {
    assertThat(tx.code(cdw.code())).isEqualTo(expected.code());
  }

  @Test
  public void diagnosticReport() {
    assertThat(tx.apply(cdw.diagnosticReport())).isEqualTo(expected.diagnosticReport());
  }

  @Test
  public void status() {
    assertThat(tx.status(null)).isNull();
    fail();
  }

  private static class CdwSampleData {

    private CdwDiagnosticReportCategory category() {
      CdwDiagnosticReportCategory category = new CdwDiagnosticReportCategory();
      category.setCoding(categoryCoding());
      return category;
    }

    private CdwDiagnosticReportCategoryCoding categoryCoding() {
      CdwDiagnosticReportCategoryCoding coding = new CdwDiagnosticReportCategoryCoding();
      coding.setSystem("http://hl7.org/fhir/ValueSet/diagnostic-service-sections");
      coding.setCode(CdwDiagnosticReportCategoryCode.LAB);
      coding.setDisplay(CdwDiagnosticReportCategoryDisplay.LABORATORY);
      return coding;
    }

    private CdwReference cdwReference() {
      CdwReference ref = new CdwReference();
      ref.setReference("HelloReference");
      ref.setDisplay("HelloDisplay");
      return ref;
    }

    private CdwDiagnosticReportCode code() {
      CdwDiagnosticReportCode code = new CdwDiagnosticReportCode();
      code.setText("panel");
      return code;
    }

    CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport diagnosticReport() {
      CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport sampleDR =
          new CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport();
      sampleDR.setCdwId("5d582505-650e-58b3-8673-49138f7b2b04");
      sampleDR.setStatus(CdwDiagnosticReportStatus.FINAL);
      sampleDR.setCategory(category());
      sampleDR.setCode(code());
      sampleDR.setSubject(cdwReference());
      sampleDR.setEncounter(cdwReference());
      sampleDR.setEffective(effective());
      sampleDR.setIssued(issued());
      sampleDR.setPerformer(cdwReference());
      return sampleDR;
    }

    @SneakyThrows
    private XMLGregorianCalendar effective() {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-06-21T19:03:16Z");
    }

    @SneakyThrows
    private XMLGregorianCalendar issued() {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-06-21T20:05:12Z");
    }
  }

  private static class Expected {

    DiagnosticReport diagnosticReport() {
      return DiagnosticReport.builder()
          .resourceType("DiagnosticReport")
          .id("5d582505-650e-58b3-8673-49138f7b2b04")
          .status(Code._final)
          .category(category())
          .code(code())
          .subject(reference())
          .encounter(reference())
          .effectiveDateTime("2013-06-21T19:03:16Z")
          .issued("2013-06-21T20:05:12Z")
          .performer(reference())
          .build();
    }

    private Reference reference() {
      return Reference.builder().reference("HelloReference").display("HelloDisplay").build();
    }

    private CodeableConcept code() {
      return CodeableConcept.builder().text("panel").build();
    }

    private CodeableConcept category() {
      return CodeableConcept.builder().coding(categoryCoding()).build();
    }

    private List<Coding> categoryCoding() {
      return Collections.singletonList(
          Coding.builder()
              .system("http://hl7.org/fhir/ValueSet/diagnostic-service-sections")
              .code("LAB")
              .display("Laboratory")
              .build());
    }
  }
}
