package gov.va.health.api.sentinel;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Arrays;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@Slf4j
public class SeTest {

  @Test
  public void tryMe() {

    String authorize =
        "https://deptva-eval.okta.com/oauth2/default/v1/authorize"
            + "?client_id=0oa2dmpuz9fMYIujw2p7"
            + "&scope=offline_access%20launch%2Fpatient%20patient%2FAllergyIntolerance.read%20patient%2FCondition.read%20patient%2FDiagnosticReport.read%20patient%2FImmunization.read%20patient%2FMedication.read%20patient%2FMedicationOrder.read%20patient%2FMedicationStatement.read%20patient%2FObservation.read%20patient%2FPatient.read%20patient%2FProcedure.read"
            + "&response_type=code"
            + "&redirect_uri=https%3A%2F%2Fapp%2Fafter-auth"
            + "&state=2VV5RqFzBG4GcgS-k6OKL6dMEUyt4FH5E-OcwaYaVzU"
            + "&aud=alec";

    String icn = "1017283148V813263";
    String user = "vasdvp+IDME_05@gmail.com";
    String password = "Password1234!";

    String tokenEndpoint = "https://deptva-eval.okta.com/oauth2/default/v1/token";

    String argo = "https://dev-api.va.gov/services/argonaut/v0";

    System.setProperty("webdriver.chrome.driver", "/Users/bryanschofield/Downloads/chromedriver");
    ChromeOptions chromeOptions = new ChromeOptions();
    // chromeOptions.addArguments("--headless");
    WebDriver driver = new ChromeDriver(chromeOptions);

    driver.get(authorize);
    driver.findElement(By.className("idme-signin")).click();
    WebElement userEmail = driver.findElement(By.id("user_email"));
    userEmail.sendKeys(user);
    WebElement userPassword = driver.findElement(By.id("user_password"));
    userPassword.sendKeys(password);
    driver.findElement(By.className("btn-primary")).click();
    // Continue passed authentication code send form
    driver.findElement(By.className("btn-primary")).click();
    // Continue passed entering the authentication code
    driver.findElement(By.className("btn-primary")).click();

    String url = driver.getCurrentUrl();

    log.info("Redirected {}", url);

    driver.close();
    driver.quit();

    String code =
        Arrays.stream(url.split("\\?")[1].split("&"))
            .filter(p -> p.startsWith("code="))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Cannot find code in url " + url))
            .split("=")[1];

    log.info("Code: {}", code);

    TokenExchange auth =
        RestAssured.given()
            .contentType(ContentType.URLENC.withCharset("UTF-8"))
            .formParam("client_id", "0oa2dmpuz9fMYIujw2p7")
            .formParam("client_secret", "XTDgBe7S3iXOCDL7Wc8H49H43NJnX5FT6RoTcjwR")
            .formParam("grant_type", "authorization_code")
            .formParam("redirect_uri", "https://app/after-auth")
            .formParam("code", code)
            .post(tokenEndpoint)
            .as(TokenExchange.class);

    RestAssured.given()
        .contentType("application/fhir+json")
        .header("Authorization", "Bearer " + auth.accessToken())
        .baseUri(argo)
        .get("Condition?patient={patient}", icn)
        .then()
        .log()
        .all();
  }

  @Data
  public static class TokenExchange {
    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("expires_in")
    long expiresIn;

    @JsonProperty("scope")
    String scope;
  }
}
