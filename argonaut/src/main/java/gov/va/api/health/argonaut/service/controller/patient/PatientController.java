package gov.va.api.health.argonaut.service.controller.patient;

import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Request Mappings for the Argonaut Patient Profile, see
 * https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html for implementation
 * details.
 */
@RestController
@RequestMapping(
  value = {"/api/Patient"},
  produces = {"application/json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class PatientController {

  private Transformer patientTransformer;
  private MrAndersonClient client;

  /** Read by id. */
  @GetMapping(value = {"/{publicId}"})
  public Patient read(@PathVariable("publicId") String publicId) {

    Query<Patient103Root> query =
        Query.forType(Patient103Root.class)
            .profile(Query.Profile.ARGONAUT)
            .resource("Patient")
            .version("1.03")
            .parameters(Parameters.forIdentity(publicId))
            .build();

    Patient103Root root = client.search(query);

    return patientTransformer.apply(firstPayloadItem(hasPayload(root.getPatients()).getPatient()));
  }

  public interface Transformer extends Function<Patient103Root.Patients.Patient, Patient> {}
}
