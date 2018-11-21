package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.samples.SampleDiagnosticReports;
import gov.va.dvp.cdw.xsd.model.*;
import lombok.SneakyThrows;
import org.junit.Test;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.math.BigInteger;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;

public class DiagnosticReportTransformerTest {

  private XmlSampleData cdw = new XmlSampleData();
  private SampleDiagnosticReports diagnosticReport = SampleDiagnosticReports.get();

  @Test
  public void resultTransformsToReferenceList() {
    List<Reference> actual = transformer().result(cdw.results());
    List<Reference> expected = diagnosticReport.diagnosticReport().result();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void specimenTransformsToReferenceList() {
    List<Reference> actual = transformer().specimen(cdw.specimens());
    List<Reference> expected = diagnosticReport.diagnosticReport().specimen();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void requestTransformsToReferenceList() {
    List<Reference> actual = transformer().request(cdw.requests());
    List<Reference> expected = diagnosticReport.diagnosticReport().request();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void cdwReferenceTransformsToReference() {
    fail();
  }

  @Test
  public void codeTransformsToCodeableConcept() {
    fail();
  }

  @Test
  public void codeCodingsTransformsToCodingList() {
    fail();
  }

  @Test
  public void categoryTransformsToCodeableConcept() {
    fail();
  }

  @Test
  public void categoryCodingTransformsToCodingList() {
    fail();
  }

  @Test
  public void statusTransformsToCode() {
    fail();
  }

  private DiagnosticReportTransformer transformer() {
    return new DiagnosticReportTransformer();
  }

  private static class XmlSampleData {

    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private XmlSampleData() {
      datatypeFactory = DatatypeFactory.newInstance();
    }

    public CdwDiagnosticReport102Root diagnosticReport() {
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
      sampleRoot.getDiagnosticReports().getDiagnosticReport().add(newDiagnosticReport());
      return sampleRoot;
    }

    private CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport
        newDiagnosticReport() {
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
      sampleDR.setRequests(requests());
      sampleDR.setSpecimens(specimens());
      sampleDR.setResults(results());
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

    private CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport.CdwResults
        results() {
      CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport.CdwResults results =
          new CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport.CdwResults();
      results.getResult().add(cdwReference());
      return results;
    }

    private CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport.CdwSpecimens
        specimens() {
      CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport.CdwSpecimens specimens =
          new CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport.CdwSpecimens();
      specimens.getSpecimen().add(cdwReference());
      return specimens;
    }

    private CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport.CdwRequests
        requests() {
      CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport.CdwRequests request =
          new CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport.CdwRequests();
      request.getRequest().add(cdwReference());
      return request;
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

    private CdwDiagnosticReportCategory category() {
      CdwDiagnosticReportCategory category = new CdwDiagnosticReportCategory();
      CdwDiagnosticReportCategoryCoding coding = new CdwDiagnosticReportCategoryCoding();
      coding.setSystem("http://hl7.org/fhir/v2/0074");
      coding.setCode(CdwDiagnosticReportCategoryCode.CH);
      coding.setDisplay(CdwDiagnosticReportCategoryDisplay.CHEMISTRY);
      category.getCoding().add(coding);
      category.setText("Test Category Text");
      return category;
    }
  }
}
