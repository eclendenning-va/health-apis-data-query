package gov.va.health.api.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public class GeneratedSwaggerConsole {

  private ChromeDriver driver;
  private WebDriverWait wait;

  public static class Config {
    private Properties properties;

    @SneakyThrows
    Config(File file) {
      if (file.exists()) {
        log.info("Loading lab properties from: {}", file);
        properties = new Properties(System.getProperties());
        try (FileInputStream in = new FileInputStream(file)) {
          properties.load(in);
        }
      } else {
        log.info("Lab properties not found: {}, using System properties", file);
        properties = System.getProperties();
      }
    }

    String driver() {
      return valueOf("webdriver.chrome.driver");
    }

    boolean headless() {
      return BooleanUtils.toBoolean(valueOf("webdriver.chrome.headless"));
    }

    private String valueOf(String name) {
      String value = properties.getProperty(name, "");
      assertThat(value).withFailMessage("System property %s must be specified.", name).isNotBlank();
      return value;
    }
  }

  /** Set up the chrome driver with the proper properties. */
  public void initializeDriver(int defaultTimeOutInSeconds) {
    Config config = new Config(new File("config/lab.properties"));
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.setHeadless(config.headless());
    if (StringUtils.isNotBlank(config.driver())) {
      System.setProperty("webdriver.chrome.driver", config.driver());
    }
    driver = new ChromeDriver(chromeOptions);
    wait = new WebDriverWait(driver, defaultTimeOutInSeconds);
    driver.manage().timeouts().implicitlyWait(defaultTimeOutInSeconds, TimeUnit.SECONDS);
    driver.get("https://argonaut.lighthouse.va.gov/console/");
  }

  public String title() {
    return driver.getTitle();
  }

  /** Return a list of resources listed on the console. */
  public List<WebElement> resources() {
    return driver
        .findElement(By.id("raml-console-resources-container"))
        .findElements(By.tagName("li"));
  }

  /** Return a list of content types available for a resource. */
  public List<WebElement> contentTypes() {
    return driver
        .findElement(By.className("raml-console-resource-body-heading"))
        .findElements(By.tagName("span"));
  }

  /** Clicks the child found by class name on the element provided. */
  public void clickChild(WebElement element, String childClassName) {
    wait.until(ExpectedConditions.elementToBeClickable(By.className(childClassName)));
    try {
      click(element.findElement(By.className(childClassName)));
    } catch (NoSuchElementException e) {
      log.error("Child does not exist");
    }
  }

  /** Clicks the element provided. */
  public void click(WebElement element) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    wait.until(ExpectedConditions.elementToBeClickable(element));
    js.executeScript("arguments[0].click()", element);
  }

  /** Tears down driver. */
  public void quit() {
    driver.quit();
  }

  /** Returns true if the element exists, and false if not. */
  public boolean isElementPresent(By by) {
    wait.until(ExpectedConditions.presenceOfElementLocated(by));
    try {
      driver.findElement(by);
      log.info("Exists");
      return true;
    } catch (NoSuchElementException e) {
      log.info("Does Not Exist");
      return false;
    }
  }
}
