package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.service.mranderson.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.MrAndersonClientImpl;
import java.util.Arrays;
import java.util.Collections;

import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
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
  produces = {"application/json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class PatientController {

  @Value("${mranderson.url}")
  private String baseUrl;
  private final PatientTransformer patientTransformer;
  private final String VERSION = "/1.03";

  @Autowired
  private final RestTemplate restTemplate;

  private HttpHeaders headers() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    return headers;
  }

  private MrAndersonClient client() {
    return MrAndersonClientImpl.<Patient>builder()
        .profile(MrAndersonClient.Profile.ARGONAUT)
        .resource("/Patient")
        .root(Patient.class)
        .url(baseUrl)
        .version(VERSION)
        .build();
  }

  /** Read */
  @GetMapping(value = {"/{publicId}"})
  @SneakyThrows
  public Patient read(@PathVariable("publicId") String publicId, ServerWebExchange exchange) {

    MrAndersonClient mrAndersonClient = client();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("id", Collections.singletonList(publicId));

    ResponseEntity entity = restTemplate.exchange("https://localhost:8088/api/v1/resources/argonaut/Patient/1.03?id={publicId}",
            HttpMethod.GET,
            new HttpEntity<Patient103Root>(headers()), new ParameterizedTypeReference<Patient103Root>() {},
            publicId);

    return patientTransformer.apply(mrAndersonClient.query(params));
  }

  /** Search by Identifier */
  @GetMapping(params = {"identifier"})
  @SneakyThrows
  public Patient searchByIdentifier(
      @RequestParam("identifier") String[] identifier, ServerWebExchange exchange) {

    MrAndersonClient mrAndersonClient = client();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("identifier", Arrays.asList(identifier));
    return patientTransformer.apply(mrAndersonClient.query(params));
  }

  /** Search by Name+Birthdate */
  @GetMapping(params = {"name", "birthdate"})
  @SneakyThrows
  public String searchByNameAndBirthdate(
      @RequestParam("name") String name,
      @RequestParam("birthdate") String birthdate,
      ServerWebExchange exchange) {
    return null;
  }

  /** Search by Name+Gender */
  @GetMapping(params = {"name", "gender"})
  @SneakyThrows
  public String searchByNameAndGender(
      @RequestParam("name") String name,
      @RequestParam("gender") String gender,
      ServerWebExchange exchange) {
    return null;
  }

  /** Search by Family+Gender */
  @GetMapping(params = {"family", "gender"})
  @SneakyThrows
  public String searchByFamilyAndGender(
      @RequestParam("family") String name,
      @RequestParam("gender") String gender,
      ServerWebExchange exchange) {
    return null;
  }

  /** Search by Given+Gender */
  @GetMapping(params = {"given", "gender"})
  @SneakyThrows
  public String searchByGivenAndGender(
      @RequestParam("given") String name,
      @RequestParam("gender") String gender,
      ServerWebExchange exchange) {
    return null;
  }
}
