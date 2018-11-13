package gov.va.api.health.argonaut.service.controller.patient;

import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.api.bundle.AbstractBundle;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import java.util.Arrays;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  /** Search by Family+Gender */
  @GetMapping(params = {"family", "gender"})
  public Patient searchByFamilyAndGender(
      @RequestParam("family") String family,
      @RequestParam("gender") String gender) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("family", Arrays.asList(family));
    params.put("gender", Arrays.asList(gender));
    return null;
  }

  /** Search by Given+Gender */
  @GetMapping(params = {"given", "gender"})
  public Patient searchByGivenAndGender(
      @RequestParam("given") String given,
      @RequestParam("gender") String gender) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("given", Arrays.asList(given));
    params.put("gender", Arrays.asList(gender));
    return null;
  }

  /** Search by Identifier */
  @GetMapping(params = {"identifier"})
  public Patient.Bundle searchByIdentifier(
      @RequestParam("identifier") String identifier) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("identifier", Arrays.asList(identifier));
    return null;
  }

  /** Search by Name+Birthdate */
  @GetMapping(params = {"name", "birthdate"})
  public Patient.Bundle searchByNameAndBirthdate(
      @RequestParam("name") String name,
      @RequestParam("birthdate") String[] birthdate) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("name", Arrays.asList(name));
    params.put("birthdate", Arrays.asList(birthdate));
    return null;
  }

  /** Search by Name+Gender */
  @GetMapping(params = {"name", "gender"})
  public Patient.Bundle searchByNameAndGender(
      @RequestParam("name") String name,
      @RequestParam("gender") String gender) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("name", Arrays.asList(name));
    params.put("gender", Arrays.asList(gender));
    return null;
  }

  /** Search by _id */
  @GetMapping(params = {"_id"})
  public Patient.Bundle searchBy_id(@RequestParam("_id") String _id) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.put("_id", Arrays.asList(_id));
    return null;
  }

  public interface Transformer extends Function<Patient103Root.Patients.Patient, Patient> {}
}
