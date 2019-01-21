package gov.va.health.api.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Iterables;
import gov.va.health.api.sentinel.categories.Lab;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Category(Lab.class)
@Slf4j
public class SwaggerAvailabilityTest {

  @Test
  public void checkAvailability() {
    GeneratedSwaggerConsole swaggerPage = new GeneratedSwaggerConsole();
    swaggerPage.initializeDriver(10);
    log.info(swaggerPage.title());
    for (WebElement w : Iterables.skip(swaggerPage.resources(), 1)) {
      log.info("---{}---", w.getAttribute("id"));
      swaggerPage.clickChild(w, "raml-console-tab-get");
      for (WebElement e : swaggerPage.contentTypes()) {
        log.info(e.getText());
        swaggerPage.click(e);
        assertThat(swaggerPage.isElementPresent(By.className("json"))).isTrue();
      }
    }
    swaggerPage.quit();
  }
}
