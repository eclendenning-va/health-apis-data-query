package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import static org.assertj.core.api.Assertions.assertThat;

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
import java.util.Collections;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

public class DiagnosticReportTransformerTest {

  DiagnosticReportTransformer tx = new DiagnosticReportTransformer();
  private CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport cdw =
      new CdwSampleData().diagnosticReport();

  private DiagnosticReport expected = Expected.get().diagnosticReport();

  @Test
  public void categoryCodingTransformsToCodingList() {
    assertThat(tx.categoryCodings(cdw.getCategory().getCoding()))
        .isEqualTo(expected.category().coding());
  }

  @Test
  public void categoryTransformsToCodeableConcept() {
    assertThat(tx.category(cdw.getCategory())).isEqualTo(expected.category());
  }

  @Test
  public void cdwReferenceTransformsToReference() {
    CdwReference cdwRef = new CdwReference();
    cdwRef.setReference("ref-test");
    cdwRef.setDisplay("dis-test");
    assertThat(tx.reference(cdwRef))
        .isEqualTo(Reference.builder().reference("ref-test").display("dis-test").build());
  }

  @Test
  public void codeCodingsTransformsToCodingList() {
    CodeableConcept codeableConcept = expected.code();
    codeableConcept.coding().get(0).display("Hello Display");
    assertThat(tx.codeCodings(cdw.getCode().getCoding())).isEqualTo(codeableConcept.coding());
  }

  @Test
  public void codeTransformsToCodeableConcept() {
    CodeableConcept codeableConcept = expected.code();
    codeableConcept.coding().get(0).display("Hello Display");
    assertThat(tx.code(cdw.getCode())).isEqualTo(codeableConcept);
  }

  @Test
  public void diagnosticReport() {
    assertThat(tx.apply(cdw)).isEqualTo(expected);
  }

  @Test
  public void statusTransformsToCode() {
    assertThat(tx.status(cdw)).isEqualTo(expected.status());
  }

  private static class CdwSampleData {

    private CdwDiagnosticReportCategory category() {
      CdwDiagnosticReportCategory category = new CdwDiagnosticReportCategory();
      CdwDiagnosticReportCategoryCoding coding = new CdwDiagnosticReportCategoryCoding();
      coding.setSystem("http://hl7.org/fhir/ValueSet/diagnostic-service-sections");
      coding.setCode(CdwDiagnosticReportCategoryCode.LAB);
      coding.setDisplay(CdwDiagnosticReportCategoryDisplay.LABORATORY);
      category.setCoding(coding);
      category.setText("dat category");
      return category;
    }

    private CdwReference cdwReference() {
      CdwReference ref = new CdwReference();
      ref.setReference("HelloReference");
      ref.setDisplay("HelloDisplay");
      return ref;
    }

    private CdwDiagnosticReportCode code() {
      CdwDiagnosticReportCode code = new CdwDiagnosticReportCode();
      CdwDiagnosticReportCodeCoding coding = new CdwDiagnosticReportCodeCoding();
      coding.setSystem("http://HelloSystem.com");
      coding.setDisplay("Hello Display");
      coding.setCode("Hello Code");
      code.getCoding().add(coding);
      code.setText("panel");
      return code;
    }

    CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport diagnosticReport() {
      CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport sampleDR =
          new CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport();
      sampleDR.setRowNumber(BigInteger.valueOf(1));
      sampleDR.setCdwId("1234");
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

    CdwDiagnosticReport102Root diagnosticReportRoot() {
      CdwDiagnosticReport102Root sampleRoot = new CdwDiagnosticReport102Root();
      sampleRoot.setFhirVersion(CdwDiagnosticReportFhirVersionValue.DSTU_2_ARGONAUT);
      sampleRoot.setResourceName(CdwDiagnosticReportResourceNameValue.DIAGNOSTICREPORT);
      sampleRoot.setResourceVersion("1.02");
      sampleRoot.setReturnType(CdwReturnTypeCodes.FULL);
      sampleRoot.setReturnFormat(CdwReturnFormatCodes.XML);
      sampleRoot.setRecordsPerPage(BigInteger.valueOf(15));
      sampleRoot.setStartRecord(BigInteger.valueOf(1));
      sampleRoot.setEndRecord(BigInteger.valueOf(1));
      sampleRoot.setRecordCount(BigInteger.valueOf(1));
      sampleRoot.setPageCount(BigInteger.valueOf(1));
      sampleRoot.setErrorNumber(0);
      sampleRoot.setErrorLine(0);
      sampleRoot.getDiagnosticReports().getDiagnosticReport().add(diagnosticReport());
      return sampleRoot;
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

  @NoArgsConstructor(staticName = "get")
  private static class Expected {

    DiagnosticReport diagnosticReport() {
      return DiagnosticReport.builder()
          .resourceType("Diagnostic Report")
          .id("1234")
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
      return CodeableConcept.builder().coding(codeCoding()).text("panel").build();
    }

    private List<Coding> codeCoding() {
      return Collections.singletonList(
          Coding.builder()
              .system("http://HelloSystem.com")
              .code("Hello Code")
              .display("Hello Display")
              .build());
    }

    private CodeableConcept category() {
      return CodeableConcept.builder().coding(categoryCoding()).text("dat category").build();
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
