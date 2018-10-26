package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.service.mranderson.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.MrAndersonQuery;
import gov.va.api.health.argonaut.service.mranderson.Profile;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;

/**
 * Request Mappings for the Argonaut Patient Profile, see
 * https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html
 * for implementation details
 */


@RestController
@RequestMapping(
        value = {"/api/Patient"},
        produces = {"application/json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class PatientController {

    private final MrAndersonClient mrAndersonClient;
    private final PatientTransformer patientTransformer;
    private final String VERSION = "1.03";

    /**
     * Read
     */
    @GetMapping(
            value = {"/{publicId}"}
    )
    @SneakyThrows
    public Patient read(@PathVariable("publicId") String publicId, ServerWebExchange exchange) {

        MrAndersonQuery query = MrAndersonQuery.builder()
                .version(VERSION)
                .profile(Profile.ARGONAUT)
                .resource(Patient.class)
                .build();
        return patientTransformer.apply(mrAndersonClient.query(query));
    }

    /**
     * Search by Identifier
     */
    @GetMapping(
            params = {"identifier"}
    )
    @SneakyThrows
    public List<String> searchByIdentifier(@RequestParam("identifier") String[] identifier, ServerWebExchange exchange) {

        return Arrays.asList(identifier);
    }

    /**
     * Search by Name+Birthdate
     */
    @GetMapping(
            params = {"name", "birthdate"}
    )
    @SneakyThrows
    public String searchByNameAndBirthdate(@RequestParam("name") String name, @RequestParam("birthdate") String birthdate, ServerWebExchange exchange) {
        return null;
    }

    /**
     * Search by Name+Gender
     */
    @GetMapping(
            params = {"name", "gender"}
    )
    @SneakyThrows
    public String searchByNameAndGender(@RequestParam("name") String name, @RequestParam("gender") String gender, ServerWebExchange exchange) {
        return null;
    }

    /**
     * Search by Family+Gender
     */
    @GetMapping(
            params = {"family", "gender"}
    )
    @SneakyThrows
    public String searchByFamilyAndGender(@RequestParam("family") String name, @RequestParam("gender") String gender, ServerWebExchange exchange) {
        return null;
    }

    /**
     * Search by Given+Gender
     */
    @GetMapping(
            params = {"given", "gender"}
    )
    @SneakyThrows
    public String searchByGivenAndGender(@RequestParam("given") String name, @RequestParam("gender") String gender, ServerWebExchange exchange) {
        return null;
    }

}