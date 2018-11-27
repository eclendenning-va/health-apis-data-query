package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.samples.SampleDiagnosticReports;
import gov.va.dvp.cdw.xsd.model.*;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.SneakyThrows;
import org.junit.Test;

public class DiagnosticReportTransformerTest {

  private CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport cdw =
      new XmlSampleData().diagnosticReport();
  private DiagnosticReport diagnosticReport = SampleDiagnosticReports.get().diagnosticReport();

  @Test
  public void cdwReferenceTransformsToReference() {
    CdwReference cdwRef = new CdwReference();
    cdwRef.setReference("ref-test");
    cdwRef.setDisplay("dis-test");
    assertThat(transformer().reference(cdwRef))
        .isEqualTo(Reference.builder().reference("ref-test").display("dis-test").build());
  }

  @Test
  public void codeTransformsToCodeableConcept() {
    assertThat(transformer().code(cdw.getCode())).isEqualTo(diagnosticReport.code());
  }

  @Test
  public void codeCodingsTransformsToCodingList() {
    assertThat(transformer().codeCodings(cdw.getCode().getCoding()))
        .isEqualTo(diagnosticReport.code().coding());
  }

  @Test
  public void categoryTransformsToCodeableConcept() {
    assertThat(transformer().category(cdw.getCategory())).isEqualTo(diagnosticReport.category());
  }

  @Test
  public void categoryCodingTransformsToCodingList() {
    assertThat(transformer().categoryCodings(cdw.getCategory().getCoding()))
        .isEqualTo(diagnosticReport.category().coding());
  }

  @Test
  public void statusTransformsToCode() {
    assertThat(transformer().status(cdw)).isEqualTo(diagnosticReport.status());
  }

  private DiagnosticReportTransformer transformer() {
    return new DiagnosticReportTransformer();
  }

  private static class XmlSampleData {

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

    @SneakyThrows
    private XMLGregorianCalendar issued() {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-06-21T20:05:12Z");
    }

    @SneakyThrows
    private XMLGregorianCalendar effective() {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-06-21T19:03:16Z");
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
  }
}
