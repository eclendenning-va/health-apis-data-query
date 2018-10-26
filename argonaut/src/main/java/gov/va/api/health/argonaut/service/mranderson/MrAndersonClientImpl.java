package gov.va.api.health.argonaut.service.mranderson;

import gov.va.api.health.argonaut.api.Patient;
import org.springframework.stereotype.Service;

@Service
public class MrAndersonClientImpl implements MrAndersonClient {
    @Override
    public Patient query(MrAndersonQuery queryString) {
        return Patient.builder().id("123").build();

    }
}
