package gov.va.api.health.dataquery.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import gov.va.api.health.dataquery.tests.categories.LabDataQueryPatient;
import gov.va.api.health.dataquery.tests.categories.ProdDataQueryPatient;
import gov.va.api.health.sentinel.categories.Local;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * This class is only meant to test the "raw" functionality of data-query Regular reads and searches
 * are performed in each individual resource's IT tests
 */
@Slf4j
public class RawIT {

  ResourceVerifier verifier = ResourceVerifier.get();

  RequestSpecification raw =
      RestAssured.given()
          .spec(verifier.dataQuery().service().requestSpecification())
          .headers(ImmutableMap.of("raw", System.getProperty("raw-token", "true")));

  @Test
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void allergyIntoleranceRaw() {
    assertFhirObject("AllergyIntolerance", verifier.ids().allergyIntolerance());
  }

  @SneakyThrows
  public void assertFhirObject(String resourceName, String publicId) {
    // Verify it is a raw response from the correct resource
    String fhirObjectType =
        readRaw(resourceName, publicId)
            .jsonPath()
            .using(JsonPathConfig.jsonPathConfig().charset("UTF-8"))
            .get("objectType")
            .toString();
    assertThat(fhirObjectType).isEqualTo(resourceName);
  }

  @Test
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void conditionRaw() {
    assertFhirObject("Condition", verifier.ids().condition());
  }

  @Test
  @SneakyThrows
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void diagnosticReportRaw() {
    // objectType is not returned in a raw diagnosticReport read, so we'll make sure it has an
    // identifier instead
    Response response = readRaw("DiagnosticReport", verifier.ids().diagnosticReport());
    String resourceIdentifier =
        response
            .jsonPath()
            .using(JsonPathConfig.jsonPathConfig().charset("UTF-8"))
            .get("identifier")
            .toString();
    assertThat(resourceIdentifier).isNotBlank();
  }

  @Test
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void immunizationRaw() {
    assertFhirObject("Immunization", verifier.ids().immunization());
  }

  @Test
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void medicationOrderRaw() {
    assertFhirObject("MedicationOrder", verifier.ids().medicationOrder());
  }

  @Test
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void medicationRaw() {
    assertFhirObject("Medication", verifier.ids().medication());
  }

  @Test
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void medicationStatementRaw() {
    assertFhirObject("MedicationStatement", verifier.ids().medicationStatement());
  }

  @Test
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void observationRaw() {
    assertFhirObject("Observation", verifier.ids().observation());
  }

  @Test
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void patientRaw() {
    assertFhirObject("Patient", verifier.ids().patient());
  }

  @Test
  @Category({Local.class, LabDataQueryPatient.class, ProdDataQueryPatient.class})
  public void procedureRaw() {
    assertFhirObject("Procedure", verifier.ids().procedure());
  }

  @SneakyThrows
  public Response readRaw(String resourceName, String publicId) {
    String path = verifier.dataQuery().service().apiPath() + resourceName + "/" + publicId;
    log.info("Verify raw response for {}, with [{}]", path, publicId);
    Response response = raw.get(path);
    assertThat(response.getStatusCode()).isEqualTo(200);
    return response;
  }
}
