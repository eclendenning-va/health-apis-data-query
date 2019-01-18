package gov.va.health.api.sentinel;

import gov.va.health.api.sentinel.categories.Lab;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@Category(Lab.class)
@Slf4j
public class SwaggerAvailabilityTest {

  private static ChromeDriver driver;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void checkAvailibility() {
    GeneratedSwaggerConsole swaggerPage = new GeneratedSwaggerConsole();
    swaggerPage.initializeDriver();
    log.info(swaggerPage.title());
    List<WebElement> resources = swaggerPage.resources();
    resources.stream().skip(1).forEach(w -> swaggerPage.clickGet(w));
  }
}
