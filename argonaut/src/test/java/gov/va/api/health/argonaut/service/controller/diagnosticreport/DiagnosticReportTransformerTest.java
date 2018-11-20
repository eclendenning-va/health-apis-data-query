package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import gov.va.api.health.argonaut.api.samples.SampleDiagnosticReports;
import lombok.SneakyThrows;

import javax.xml.datatype.DatatypeFactory;

public class DiagnosticReportTransformerTest {

    private XmlSampleData cdw = new XmlSampleData();
    private SampleDiagnosticReports diagnosticReport = SampleDiagnosticReports.get();

    private static class XmlSampleData {

        private DatatypeFactory datatypeFactory;

        @SneakyThrows
        private XmlSampleData() {
            datatypeFactory = DatatypeFactory.newInstance();
        }
    }
}
