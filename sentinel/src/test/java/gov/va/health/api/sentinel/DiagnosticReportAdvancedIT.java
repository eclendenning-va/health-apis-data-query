package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class DiagnosticReportAdvancedIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB",
            ids.patient()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&code={loinc1}",
            ids.patient(),
            ids.diagnosticReports().loinc1()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&code={loinc1},{loinc2}",
            ids.patient(),
            ids.diagnosticReports().loinc1(),
            ids.diagnosticReports().loinc2()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={onDate}",
            ids.patient(),
            ids.diagnosticReports().onDate()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={fromDate}&date={toDate}",
            ids.patient(),
            ids.diagnosticReports().fromDate(),
            ids.diagnosticReports().toDate()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYear}",
            ids.patient(),
            ids.diagnosticReports().dateYear()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonth}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonth()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDay}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDay()),
        assertRequest(
            400,
            OperationOutcome.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHour}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHour()),
        assertRequest(
            400,
            OperationOutcome.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHourMinute}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHourMinute()),
        assertRequest(
            400,
            OperationOutcome.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHourMinuteSecond}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHourMinuteSecond()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHourMinuteSecondTimezone}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHourMinuteSecondTimezone()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateYearMonthDayHourMinuteSecondZulu}",
            ids.patient(),
            ids.diagnosticReports().dateYearMonthDayHourMinuteSecondZulu()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateGreaterThan}",
            ids.patient(),
            ids.diagnosticReports().dateGreaterThan()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateNotEqual}",
            ids.patient(),
            ids.diagnosticReports().dateNotEqual()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateStartsWith}",
            ids.patient(),
            ids.diagnosticReports().dateStartsWith()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateNoPrefix}",
            ids.patient(),
            ids.diagnosticReports().dateNoPrefix()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateEqual}",
            ids.patient(),
            ids.diagnosticReports().dateEqual()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateLessOrEqual}",
            ids.patient(),
            ids.diagnosticReports().dateLessOrEqual()),
        assertRequest(
            200,
            DiagnosticReport.Bundle.class,
            "/api/DiagnosticReport?patient={patient}&category=LAB&date={dateLessThan}",
            ids.patient(),
            ids.diagnosticReports().dateLessThan()));
  }
}
