package gov.va.health.api.sentinel;

import gov.va.health.api.sentinel.TestIds.DiagnosticReports;
import gov.va.health.api.sentinel.TestIds.Observations;
import gov.va.health.api.sentinel.TestIds.PersonallyIdentifiableInformation;
import gov.va.health.api.sentinel.TestIds.Procedures;
import gov.va.health.api.sentinel.TestIds.Range;
import lombok.NoArgsConstructor;
import lombok.Value;

/** The standard system configurations for typical environments like QA or PROD. */
@Value
@NoArgsConstructor(staticName = "get")
public class SystemDefinitions {

  SystemDefinition local =
      SystemDefinition.builder()
          .ids(ServiceDefinition.builder().url("https://localhost").port(8089).build())
          .mrAnderson(ServiceDefinition.builder().url("https://localhost").port(8088).build())
          .argonaut(ServiceDefinition.builder().url("https://localhost").port(8090).build())
          .cdwIds(
              TestIds.builder()
                  .allergyIntolerance("1000001782544")
                  .appointment("1200438317388")
                  .condition("1400007575530:P")
                  .diagnosticReport("1000000031384:L")
                  .encounter("1200753214085")
                  .diagnosticReports(
                      DiagnosticReports.builder()
                          .loinc1("10000-8")
                          .loinc2("99999-9")
                          .onDate("eq1970-01-01")
                          .fromDate("gt1970-01-01")
                          .toDate("lt2038-01-01")
                          .dateYear("ge1970")
                          .dateYearMonth("ge1970-01")
                          .dateYearMonthDay("ge1970-01-01")
                          .dateYearMonthDayHour("ge1970-01-01T07")
                          .dateYearMonthDayHourMinute("ge1970-01-01T07:00")
                          .dateYearMonthDayHourMinuteSecond("ge1970-01-01T07:00:00")
                          .dateYearMonthDayHourMinuteSecondTimezone("ge1970-01-01T07:00:00+05:00")
                          .dateYearMonthDayHourMinuteSecondZulu("ge1970-01-01T07:00:00Z")
                          .dateGreaterThan("ge1970-01-01")
                          .dateNotEqual("ne1970-01-01")
                          .dateStartsWith("sa1970-01-01")
                          .dateNoPrefix("1970-01-01")
                          .dateEqual("1970-01-01")
                          .dateLessOrEqual("le2038-01-19")
                          .dateLessThan("lt2038-01-19")
                          .build())
                  .immunization("1000000043979")
                  .location("166365:L")
                  .medication("212846")
                  .medicationOrder("1200389904206:O")
                  .medicationStatement("1400000182116")
                  .observation("1201051417263:V")
                  .observations(
                      Observations.builder()
                          .loinc1("72166-2")
                          .loinc2("777-3")
                          .onDate("2015-04-15")
                          .dateRange(Range.allTime())
                          .build())
                  .organization("1000025431:C")
                  .pii(
                      PersonallyIdentifiableInformation.builder()
                          .gender("male")
                          .birthdate("1970-01-01")
                          .given("JOHN Q")
                          .name("VETERAN,JOHN")
                          .family("VETERAN")
                          .build())
                  .patient("185601V825290")
                  .practitioner("10092125")
                  .procedure("1400000140034")
                  .procedures(
                      Procedures.builder()
                          .onDate("ge2009")
                          .fromDate("ge2009")
                          .toDate("le2010")
                          .build())
                  .unknown("5555555555555")
                  .build())
          .build();

  SystemDefinition PROD =
      SystemDefinition.builder()
          .ids(ServiceDefinition.builder().url("https://argonaut.lighthouse.va.gov/api").port(8089).build())
          .mrAnderson(ServiceDefinition.builder().url("https://argonaut.lighthouse.va.gov/api").port(8088).build())
          .argonaut(ServiceDefinition.builder().url("https://argonaut.lighthouse.va.gov/api").port(8090).build())
          .cdwIds(prodAndQaIds())
          .build();

  SystemDefinition QA =
      SystemDefinition.builder()
          .ids(ServiceDefinition.builder().url("https://qa-argonaut.lighthouse.va.gov/api").port(8089).build())
          .mrAnderson(ServiceDefinition.builder().url("https://qa-argonaut.lighthouse.va.gov/api").port(8088).build())
          .argonaut(ServiceDefinition.builder().url("https://qa-argonaut.lighthouse.va.gov/api").port(8090).build())
          .cdwIds(prodAndQaIds())
          .build();

  TestIds prodAndQaIds() {
    return TestIds.builder()
        .allergyIntolerance("")
        .appointment("")
        .condition("")
        .diagnosticReport("")
        .diagnosticReports(
            DiagnosticReports.builder()
                .loinc1("10000-8")
                .loinc2("99999-9")
                .onDate("eq1970-01-01")
                .fromDate("gt1970-01-01")
                .toDate("lt2038-01-01")
                .dateYear("ge1970")
                .dateYearMonth("ge1970-01")
                .dateYearMonthDay("ge1970-01-01")
                .dateYearMonthDayHour("ge1970-01-01T07")
                .dateYearMonthDayHourMinute("ge1970-01-01T07:00")
                .dateYearMonthDayHourMinuteSecond("ge1970-01-01T07:00:00")
                .dateYearMonthDayHourMinuteSecondTimezone("ge1970-01-01T07:00:00+05:00")
                .dateYearMonthDayHourMinuteSecondZulu("ge1970-01-01T07:00:00Z")
                .dateGreaterThan("gt1970-01-01")
                .dateNotEqual("ne1970-01-01")
                .dateStartsWith("sa1970-01-01")
                .dateNoPrefix("1970-01-01")
                .dateEqual("eq1970-01-01")
                .dateLessOrEqual("le2038-01-19")
                .dateLessThan("lt2038-01-19")
                .build()
        )
        .encounter("")
        .immunization("")
        .location("")
        .medication("")
        .medicationOrder("")
        .medicationStatement("")
        .observation("")
        .observations(
            Observations.builder()
                .loinc1("72166-2")
                .loinc2("777-3")
                .onDate("2015-04-15")
                .dateRange(Range.allTime())
                .build()
        )
        .organization("")
        .patient("")
        .pii(
            PersonallyIdentifiableInformation.builder()
                .gender("")
                .birthdate("")
                .given("")
                .name(",")
                .family("")
                .build()
        )
        .practitioner("")
        .procedure("")
        .procedures(
            Procedures.builder()
                .fromDate("ge2009")
                .onDate("ge2009")
                .toDate("le2010")
                .build()
        )
        .build();
  }


}
