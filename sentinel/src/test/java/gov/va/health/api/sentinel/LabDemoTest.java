package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.resources.Immunization;
import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.api.resources.MedicationStatement;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.Patient;
import gov.va.api.health.argonaut.api.resources.Procedure;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.health.api.sentinel.IdMeOauthRobot.Configuration.Authorization;
import gov.va.health.api.sentinel.IdMeOauthRobot.Configuration.UserCredentials;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponseOptions;
import io.restassured.specification.RequestSpecification;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@Slf4j
public class LabDemoTest {

  private static boolean USE_JARGONAUT = true;

  @Parameter(0)
  public String name;

  @Getter
  @Parameter(1)
  public IdMeOauthRobot robot;

  @Parameters(name = "{index}: {0}")
  public static List<Object[]> parameters() {
    return Arrays.asList(
        //
        test("User 1", makeRobot(user1())),
        test("User 2", makeRobot(user2())),
        test("User 3", makeRobot(user3())),
        test("User 4", makeRobot(user4())),
        test("User 5", makeRobot(user5()))
        //
        );
  }

  private static Object[] test(String name, IdMeOauthRobot robot) {
    return new Object[] {name, robot};
  }

  private static IdMeOauthRobot makeRobot(UserCredentials user) {
    IdMeOauthRobot.Configuration config =
        IdMeOauthRobot.Configuration.builder()
            .authorization(
                Authorization.builder()
                    .clientId("0oa2dmpuz9fMYIujw2p7")
                    .clientSecret("XTDgBe7S3iXOCDL7Wc8H49H43NJnX5FT6RoTcjwR")
                    .authorizeUrl("https://dev-api.va.gov/oauth2/authorization")
                    .redirectUrl("https://app/after-auth")
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
            .tokenUrl("https://dev-api.va.gov/oauth2/token")
            .user(user)
            .build();

    System.setProperty("webdriver.chrome.driver", "/Users/bryanschofield/Downloads/chromedriver");
    IdMeOauthRobot robot = IdMeOauthRobot.of(config);
    return robot;
  }

  private static UserCredentials user1() {
    return UserCredentials.builder()
        .id("vasdvp+IDME_01@gmail.com")
        .icn("1017283132V631076")
        .password("Password1234!")
        .build();
  }

  private static UserCredentials user2() {
    return UserCredentials.builder()
        .id("vasdvp+IDME_02@gmail.com")
        .icn("1017283179V257219")
        .password("Password1234!")
        .build();
  }

  private static UserCredentials user3() {
    return UserCredentials.builder()
        .id("vasdvp+IDME_03@gmail.com")
        .icn("1012704686V159887")
        .password("Password1234!")
        .build();
  }

  private static UserCredentials user4() {
    return UserCredentials.builder()
        .id("vasdvp+IDME_04@gmail.com")
        .icn("1017283180V801730")
        .password("Password1234!")
        .build();
  }

  private static UserCredentials user5() {
    return UserCredentials.builder()
        .id("vasdvp+IDME_05@gmail.com")
        .icn("1017283148V813263")
        .password("Password1234!")
        .build();
  }

  @Test
  public void allergyIntolerance() {
    AllergyIntolerance.Bundle bundle =
        get(
            request()
                .get("AllergyIntolerance?patient={patient}", config().user().icn())
                .then()
                .log()
                .all()
                .statusCode(200),
            AllergyIntolerance.Bundle.class);
    if (!bundle.entry().isEmpty()) {
      request().get(bundle.entry().get(0).fullUrl()).then().statusCode(200);
    }
  }

  @Test
  public void condition() {
    Condition.Bundle bundle =
        get(
            request()
                .get("Condition?patient={patient}", config().user().icn())
                .then()
                .log()
                .all()
                .statusCode(200),
            Condition.Bundle.class);
    if (!bundle.entry().isEmpty()) {
      request().get(bundle.entry().get(0).fullUrl()).then().statusCode(200);
    }
  }

  private IdMeOauthRobot.Configuration config() {
    return robot.config();
  }

  @Test
  public void diagnosticReport() {
    DiagnosticReport.Bundle bundle =
        get(
            request()
                .get("DiagnosticReport?patient={patient}", config().user().icn())
                .then()
                .log()
                .all()
                .statusCode(200),
            DiagnosticReport.Bundle.class);
    if (!bundle.entry().isEmpty()) {
      request().get(bundle.entry().get(0).fullUrl()).then().statusCode(200);
    }
  }

  @SneakyThrows
  <T> T get(Class<T> type, String path, String... params) {
    return JacksonConfig.createMapper()
        .readValue(request().get(path, params).then().extract().body().asString(), type);
  }

  @SneakyThrows
  <T> T get(ValidatableResponseOptions r, Class<T> type) {
    return JacksonConfig.createMapper().readValue(r.extract().body().asString(), type);
  }

  @Test
  public void immunization() {
    Immunization.Bundle bundle =
        get(
            request()
                .get("Immunization?patient={patient}", config().user().icn())
                .then()
                .log()
                .all()
                .statusCode(200),
            Immunization.Bundle.class);
    if (!bundle.entry().isEmpty()) {
      request().get(bundle.entry().get(0).fullUrl()).then().statusCode(200);
    }
  }

  @Test
  @SneakyThrows
  public void medication() {
    MedicationStatement.Bundle ms =
        get(
            MedicationStatement.Bundle.class,
            "MedicationStatement?patient={patient}",
            config().user().icn());
    if (!ms.entry().isEmpty()) {
      request()
          .get(ms.entry().get(0).resource().medicationReference().reference())
          .then()
          .log()
          .all()
          .statusCode(200);
    }
  }

  @Test
  public void medicationOrder() {
    MedicationOrder.Bundle bundle =
        get(
            request()
                .get("MedicationOrder?patient={patient}", config().user().icn())
                .then()
                .log()
                .all()
                .statusCode(200),
            MedicationOrder.Bundle.class);
    if (!bundle.entry().isEmpty()) {
      request().get(bundle.entry().get(0).fullUrl()).then().statusCode(200);
    }
  }

  @Test
  public void medicationStatement() {
    MedicationStatement.Bundle bundle =
        get(
            request()
                .get("MedicationStatement?patient={patient}", config().user().icn())
                .then()
                .log()
                .all()
                .statusCode(200),
            MedicationStatement.Bundle.class);
    if (!bundle.entry().isEmpty()) {
      request().get(bundle.entry().get(0).fullUrl()).then().statusCode(200);
    }
  }

  @Test
  public void metadata() {
    request().get("metadata").then().log().all().statusCode(200);
  }

  @Test
  public void observation() {
    Observation.Bundle bundle =
        get(
            request()
                .get("Observation?patient={patient}", config().user().icn())
                .then()
                .log()
                .all()
                .statusCode(200),
            Observation.Bundle.class);
    if (!bundle.entry().isEmpty()) {
      request().get(bundle.entry().get(0).fullUrl()).then().statusCode(200);
    }
  }

  @Test
  public void patient() {
    Patient.Bundle bundle =
        get(
            request()
                .get("Patient?_id={patient}", config().user().icn())
                .then()
                .log()
                .all()
                .statusCode(200),
            Patient.Bundle.class);
    if (!bundle.entry().isEmpty()) {
      request().get(bundle.entry().get(0).fullUrl()).then().statusCode(200);
    }
  }

  @Test
  public void procedure() {
    Procedure.Bundle bundle =
        get(
            request()
                .get("Procedure?patient={patient}", config().user().icn())
                .then()
                .log()
                .all()
                .statusCode(200),
            Procedure.Bundle.class);
    if (!bundle.entry().isEmpty()) {
      request().get(bundle.entry().get(0).fullUrl()).then().statusCode(200);
    }
  }

  // No data available for Appointment,Encounter,Location,Organization,Practitioner

  private RequestSpecification request() {
    return RestAssured.given()
        .contentType("application/fhir+json")
        .header("Authorization", "Bearer " + robot().token().accessToken())
        .header("jargonaut", USE_JARGONAUT)
        .baseUri("https://dev-api.va.gov/services/argonaut/v0");
  }
}
