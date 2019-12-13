package gov.va.api.health.dataquery.service.controller.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import gov.va.api.health.argonaut.api.resources.Patient;
import gov.va.api.health.argonaut.api.resources.Patient.Bundle;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.dataquery.service.controller.ConfigurableBaseUrlPageLinks;
import gov.va.api.health.dataquery.service.controller.Dstu2Bundler;
import gov.va.api.health.dataquery.service.controller.Dstu2Bundler.BundleContext;
import gov.va.api.health.dataquery.service.controller.Dstu2Validator;
import gov.va.api.health.dataquery.service.controller.PageLinks.LinkConfig;
import gov.va.api.health.dataquery.service.controller.Parameters;
import gov.va.api.health.dataquery.service.controller.WitnessProtection;
import gov.va.api.health.dataquery.service.controller.patient.PatientSamples.Datamart;
import gov.va.api.health.dataquery.service.controller.patient.PatientSamples.Dstu2;
import gov.va.api.health.dataquery.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.dataquery.service.mranderson.client.Query;
import gov.va.api.health.dstu2.api.bundle.AbstractBundle.BundleType;
import gov.va.api.health.ids.api.IdentityService;
import gov.va.api.health.ids.api.Registration;
import gov.va.api.health.ids.api.ResourceIdentity;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root.CdwPatients.CdwPatient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

@SuppressWarnings("WeakerAccess")
@DataJpaTest
@RunWith(SpringRunner.class)
public class Dstu2PatientControllerTest {
  HttpServletResponse response = mock(HttpServletResponse.class);

  private IdentityService ids = mock(IdentityService.class);

  @Autowired private PatientSearchRepository repository;


  @SneakyThrows
  private PatientSearchEntity asEntity(DatamartPatient dm) {
    var entity =  PatientEntity.builder()
        .icn(dm.fullIcn())
        .payload(JacksonConfig.createMapper().writeValueAsString(dm))
        .build();
    return PatientSearchEntity.builder()
        .icn(dm.fullIcn())
        .patient(entity)
        .build();
  }


  Dstu2PatientController controller() {
    return new Dstu2PatientController(
        new Dstu2Bundler(new ConfigurableBaseUrlPageLinks("http://fonzy.com", "cool", "cool")),
        repository,
        WitnessProtection.builder().identityService(ids).build());
  }

  @SneakyThrows
  String json(Object o) {
    return JacksonConfig.createMapper().writerWithDefaultPrettyPrinter().writeValueAsString(o);
  }

  public void mockPatientIdentity(String publicId, String cdwId) {
    ResourceIdentity resourceIdentity =
        ResourceIdentity.builder().system("CDW").resource("PATIENT").identifier(cdwId).build();
    when(ids.lookup(publicId)).thenReturn(List.of(resourceIdentity));
    when(ids.register(Mockito.any()))
        .thenReturn(
            List.of(
                Registration.builder().uuid(publicId).resourceIdentity(resourceIdentity).build()));
  }

  private Multimap<String, Patient> populateData() {
    var dstu2 = Dstu2.create();
    var datamart = Datamart.create();
    var patients = LinkedHashMultimap.<String, Patient>create();
    var registrations = new ArrayList<Registration>(10);
    for (int i = 0; i < 10; i++) {
      String patientId = "p" + i % 2;
      DatamartPatient dm = datamart.patient(patientId);
      Patient patient = dstu2.patient(patientId);
      patients.put(patientId, patient);
      ResourceIdentity resourceIdentity =
          ResourceIdentity.builder().system("CDW").resource("CONDITION").identifier(patientId).build();
      Registration registration =
          Registration.builder().uuid(patientId).resourceIdentity(resourceIdentity).build();
      registrations.add(registration);
      when(ids.lookup(patientId)).thenReturn(List.of(resourceIdentity));
    }
    when(ids.register(Mockito.any())).thenReturn(registrations);
    return patients;
  }

  @Test
  public void read() {
    DatamartPatient dm = Datamart.create().patient();
    repository.save(asEntity(dm));
    mockPatientIdentity("x", dm.fullIcn());
    Patient actual = controller().read("x");
    assertThat(actual).isEqualTo(PatientSamples.Dstu2.create().patient("x"));
  }


}
