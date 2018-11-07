package gov.va.api.health.argonaut.service.controller.patient;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import javax.xml.bind.JAXBContext;
import lombok.SneakyThrows;
import org.junit.Test;

public class OldNewComparisonTest {
  @SneakyThrows
  @Test
  public void compareOldWithNew() {
    JAXBContext context = JAXBContext.newInstance(Patient103Root.class);
    Patient103Root xml =
        (Patient103Root)
            context
                .createUnmarshaller()
                .unmarshal(getClass().getResourceAsStream("/cdw/patient-1.03.xml"));

    Patient newPatient = new PatientTransformer().apply(xml.getPatients().getPatient().get(0));
    Patient oldPatient =
        JacksonConfig.createMapper()
            .readValue(getClass().getResourceAsStream("/cdw/old-patient-1.03.json"), Patient.class);
    assertThat(newPatient).isEqualTo(newPatient);
  }
}
