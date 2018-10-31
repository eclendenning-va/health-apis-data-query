package gov.va.api.health.argonaut.service.controller.patient;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient.Profile;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient.Query;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import java.util.Arrays;
import java.util.function.Function;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * Request Mappings for the Argonaut Patient Profile, see
 * https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html for implementation
 * details.
 */
@RestController
@RequestMapping(
    value = {"/api/Patient"},
    produces = {"application/json"})
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class PatientController {

  private PatientTransformer patientTransformer;
  private MrAndersonClient client;

  private ParameterizedTypeReference<PatientSearchResultsRoot> patientSearchResultsType() {
    return ParameterizedTypeReference.forType(PatientSearchResultsRoot.class);
  }

  /** Read by id. */
  @GetMapping(value = {"/{publicId}"})
  public Patient read(@PathVariable("publicId") String publicId, ServerWebExchange exchange) {

    Query<PatientSearchResultsRoot> query =
        Query.forType(PatientSearchResultsRoot.class)
            .profile(Profile.ARGONAUT)
            .resource("Patient")
            .version("1.03")
            .parameters(readParameters(publicId))
            .build();

    PatientSearchResultsRoot root = client.search(query);

    return patientTransformer.apply(root.getPatients().getPatient().get(0));
  }

  private MultiValueMap<String, String> readParameters(@PathVariable("publicId") String publicId) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("identifier", publicId);
    return params;
  }

  private HttpEntity<Void> requestEntity() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    return new HttpEntity<>(headers);
  }

  interface PatientTransformer extends Function<Patient103Root.Patients.Patient, Patient> {}

  @XmlRootElement(name = "root")
  public static class PatientSearchResultsRoot extends Patient103Root {}
}
