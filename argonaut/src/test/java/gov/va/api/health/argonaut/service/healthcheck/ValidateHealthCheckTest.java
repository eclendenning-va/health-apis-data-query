package gov.va.api.health.argonaut.service.healthcheck;

import gov.va.api.health.argonaut.service.mranderson.client.RestMrAndersonClient;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;


public class ValidateHealthCheckTest {
    @Mock
    RestTemplate rt;

    RestMrAndersonClient client;

    @Test
    public void entireSteelThreadHappyPath() {
      //  SteelThreadSystemCheck test = new SteelThreadSystemCheck(client, "test");
       // test.health();
    }

    @Test
    public void InvalidIdProvidedToJArgonaut() {

    }

    @Test
    public void mrAndersonNotWorkingAsExpected() {

    }

    @Test
    public void idsNotWorkingAsExpected() {

    }
    @Test
    public void dbNotWorkingAsExpected() {

    }
}
