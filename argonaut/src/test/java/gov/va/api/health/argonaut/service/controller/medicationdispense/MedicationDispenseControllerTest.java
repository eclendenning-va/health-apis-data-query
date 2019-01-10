package gov.va.api.health.argonaut.service.controller.medicationdispense;

import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("WeakerAccess")
public class MedicationDispenseControllerTest {
    @Mock MrAndersonClient client;

    @Mock MedicationDispenseController.Transformer tx;

    MedicationDispenseController controller;
    @Mock Bundler bundler;

    @Before
    public void _init() {
        MockitoAnnotations.initMocks(this);
        controller = new MedicationDispenseController(tx, client, bundler);
    }

    
}
