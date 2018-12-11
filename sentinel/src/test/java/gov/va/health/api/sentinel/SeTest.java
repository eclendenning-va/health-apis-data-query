package gov.va.health.api.sentinel;

import gov.va.health.api.sentinel.IdMeOauthRobot.Configuration.Authorization;
import gov.va.health.api.sentinel.IdMeOauthRobot.Configuration.UserCredentials;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SeTest {

  @Test
  public void tryMeBetter() {
    IdMeOauthRobot.Configuration config =
        IdMeOauthRobot.Configuration.builder()
            .authorization(
                Authorization.builder()
                    .clientId("0oa2dmpuz9fMYIujw2p7")
                    .clientSecret("XTDgBe7S3iXOCDL7Wc8H49H43NJnX5FT6RoTcjwR")
                    // .authorizeUrl("https://deptva-eval.okta.com/oauth2/default/v1/authorize")
                    .authorizeUrl("https://dev-api.va.gov/oauth2/authorization")
                    .redirectUrl("https://app/after-auth")
                    // .redirectUrl("https://dev-api.va.gov/oauth2/callback")
                    .state("2VV5RqFzBG4GcgS-k6OKL6dMEUyt4FH5E-OcwaYaVzU")
                    .aud("alec")
                    .scope("openid")
                    .scope("profile")
                    .scope("offline_access")
                    .scope("launch/patient")
                    .scope("patient/AllergyIntolerance.read")
                    .scope("patient/Condition.read")
                    .scope("patient/DiagnosticReport.read")
                    .scope("patient/Immunization.read")
                    .scope("patient/Medication.read")
                    .scope("patient/MedicationOrder.read")
                    .scope("patient/MedicationStatement.read")
                    .scope("patient/Observation.read")
                    .scope("patient/Patient.read")
                    .scope("patient/Procedure.read")
                    .build())
            // .tokenUrl("https://deptva-eval.okta.com/oauth2/default/v1/token")
            .tokenUrl("https://dev-api.va.gov/oauth2/token")
            .user(
                UserCredentials.builder()
                    .id("vasdvp+IDME_05@gmail.com")
                    .icn("1017283148V813263")
                    .password("Password1234!")
                    .build())
            .build();

    System.setProperty("webdriver.chrome.driver", "/Users/bryanschofield/Downloads/chromedriver");
    IdMeOauthRobot robot = IdMeOauthRobot.of(config);
    log.info("{}", robot.token().accessToken());

    String argo = "https://dev-api.va.gov/services/argonaut/v0";

    RestAssured.given()
        .contentType("application/fhir+json")
        .header("Authorization", "Bearer " + robot.token().accessToken())
        .baseUri(argo)
        .get("Condition?patient={patient}", config.user().icn())
        .then()
        .log()
        .all();
  }
}
