package gov.va.health.api.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import lombok.Builder;
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
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public class GeneratedSwaggerConsole {

  private static ChromeDriver driver;
  private static WebDriverWait wait;
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

  public void initializeDriver(){
    Config config = new Config(new File("config/lab.properties"));
    ChromeOptions chromeOptions = new ChromeOptions();
    if (StringUtils.isNotBlank(config.driver())) {
      System.setProperty("webdriver.chrome.driver", config.driver());
    }
    driver = new ChromeDriver(chromeOptions);
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    driver.manage().window().maximize();
    driver.get("https://argonaut.lighthouse.va.gov/console/");
  }

  public void initializeWait(){
    wait = new WebDriverWait(driver, 10);
  }


  public String title() {
    return driver.getTitle();
  }

  public List<WebElement> resources() {
    return driver.findElement(By.id("raml-console-resources-container")).findElements(By.tagName("li"));
  }

  public void clickGet(WebElement resource) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript(
        "arguments[0].click()",
        resource.findElement(By.className("raml-console-tab-get")));
  }

  public void close() {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript(
        "arguments[0].click()",
        driver.findElement(By.className("raml-console-resource-close-btn")));
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

}