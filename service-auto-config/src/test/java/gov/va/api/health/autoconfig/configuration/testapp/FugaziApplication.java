package gov.va.api.health.autoconfig.configuration.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** This is a test application to test auto-configuration is working. */
@SuppressWarnings("WeakerAccess")
@SpringBootApplication
public class FugaziApplication {

  /** The required main for Spring Boot applications. */
  public static void main(String[] args) {
    SpringApplication.run(FugaziApplication.class, args);
  }
}
