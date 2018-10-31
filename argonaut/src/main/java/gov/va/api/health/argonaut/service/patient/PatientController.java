package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.service.config.WithJaxb;
import gov.va.api.health.argonaut.service.mranderson.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.MrAndersonClientImpl;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import java.util.Arrays;
import java.util.function.Function;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

/**
 * Request Mappings for the Argonaut Patient Profile, see
 * https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html for implementation
 * details
 */
@RestController
@RequestMapping(
    value = {"/api/Patient"},
    produces = {"application/json"})
@Slf4j
public class PatientController {

  private static final String VERSION = "/1.03";
  @Autowired PatientTransformer patientTransformer;
  @Autowired @WithJaxb private RestTemplate restTemplate;

  @Value("${mranderson.url}")
  private String baseUrl;

  private MrAndersonClient client() {
    return MrAndersonClientImpl.<Patient>builder()
        .profile(MrAndersonClient.Profile.ARGONAUT)
        .resource("/Patient")
        .root(Patient.class)
        .url(baseUrl)
        .version(VERSION)
        .build();
  }

  private ParameterizedTypeReference<PatientSearchResultsRoot> patientSearchResultsType() {
    return new ParameterizedTypeReference<PatientSearchResultsRoot>() {};
  }

  /** Read */
  @GetMapping(value = {"/{publicId}"})
  public Patient read(@PathVariable("publicId") String publicId, ServerWebExchange exchange) {

    ResponseEntity<PatientSearchResultsRoot> entity =
        restTemplate.exchange(
            url() + "?id={publicId}",
            HttpMethod.GET,
            requestEntity(),
            patientSearchResultsType(),
            publicId);

    return patientTransformer.apply(entity.getBody().getPatients().getPatient().get(0));
  }

  private HttpEntity<Void> requestEntity() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    return new HttpEntity<>(headers);
  }

  /** Search by Family+Gender */
  @GetMapping(params = {"family", "gender"})
  public String searchByFamilyAndGender(
      @RequestParam("family") String name,
      @RequestParam("gender") String gender,
      ServerWebExchange exchange) {
    return null;
  }

  /** Search by Given+Gender */
  @GetMapping(params = {"given", "gender"})
  public String searchByGivenAndGender(
      @RequestParam("given") String name,
      @RequestParam("gender") String gender,
      ServerWebExchange exchange) {
    return null;
  }

  /** Search by Identifier */
  @GetMapping(params = {"identifier"})
  public Patient searchByIdentifier(
      @RequestParam("identifier") String[] identifier, ServerWebExchange exchange) {

    MrAndersonClient mrAndersonClient = client();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("identifier", Arrays.asList(identifier));
    return null;
  }

  /** Search by Name+Birthdate */
  @GetMapping(params = {"name", "birthdate"})
  public String searchByNameAndBirthdate(
      @RequestParam("name") String name,
      @RequestParam("birthdate") String birthdate,
      ServerWebExchange exchange) {
    return null;
  }

  /** Search by Name+Gender */
  @GetMapping(params = {"name", "gender"})
  public String searchByNameAndGender(
      @RequestParam("name") String name,
      @RequestParam("gender") String gender,
      ServerWebExchange exchange) {
    return null;
  }

  private String url() {
    return baseUrl + "api/v1/resources/argonaut/Patient/1.03";
  }

  public interface PatientTransformer extends Function<Patient103Root.Patients.Patient, Patient> {}

  @XmlRootElement(name = "root")
  public static class PatientSearchResultsRoot extends Patient103Root {}
}
