package gov.va.health.api.sentinel;

import gov.va.health.api.sentinel.TestIds.DiagnosticReports;
import gov.va.health.api.sentinel.TestIds.Observations;
import gov.va.health.api.sentinel.TestIds.PersonallyIdentifiableInformation;
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
                  .unknown("5555555555555")
                  .diagnosticReport("1000000031384:L")
                  .patient("185601V825290")
                  .medication("212846")
                  .observation("1201051417263:V")
                  .condition("1400007575530:P")
                  .immunization("1000000043979")
                  .pii(
                      PersonallyIdentifiableInformation.builder()
                          .gender("male")
                          .birthdate("1970-01-01")
                          .given("JOHN Q")
                          .name("VETERAN,JOHN")
                          .family("VETERAN")
                          .build())
                  .observations(
                      Observations.builder()
                          .loinc1("72166-2")
                          .loinc2("777-3")
                          .onDate("2015-04-15")
                          .dateRange(Range.allTime())
                          .build())
                  .diagnosticReports(
                      DiagnosticReports.builder()
                          .loinc1("10000-8")
                          .loinc2("99999-9")
                          .onDate("eq1970-01-01")
                          .fromDate("gt1970-01-01")
                          .toDate("lt2038-01-01")
                          .build())
                  .build())
          .build();
}
