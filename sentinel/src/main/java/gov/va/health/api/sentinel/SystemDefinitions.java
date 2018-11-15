package gov.va.health.api.sentinel;

import gov.va.health.api.sentinel.TestIds.PersonallyIdentifiableInformation;
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
                  .unknown("5555555555555")
                  .patient("185601V825290")
                  .medication("212846")
                  .pii(
                      PersonallyIdentifiableInformation.builder()
                          .gender("male")
                          .birthdate("1970-01-01")
                          .given("JOHN Q")
                          .name("VETERAN,JOHN")
                          .family("VETERAN")
                          .build())
                  .build())
          .build();
}
