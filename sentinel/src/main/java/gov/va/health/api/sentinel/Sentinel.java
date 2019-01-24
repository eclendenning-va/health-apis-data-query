package gov.va.health.api.sentinel;

import java.util.Locale;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * The Sentinel provides the easy-to-use entry point to determining and accessing test
 * configurations that are specific for different environments.
 *
 * <p>It leverages the system property `sentinel` to determine which environment configuration
 * should be leveraged.
 */
@Value
@Slf4j
public class Sentinel {

  static {
    String env = System.getProperty("sentinel", "LOCAL").toUpperCase(Locale.ENGLISH);
    log.info("Using {} Sentinel environment (Override with -Dsentinel=LOCAL|QA|PROD)", env);
  }

  public enum Environment {
    LOCAL,
    PROD,
    QA
  }

  private SystemDefinition system;

  /** Parse the system property 'sentinel' into the appropriate enum. */
  public static Environment environment() {
    String env = System.getProperty("sentinel", "LOCAL").toUpperCase(Locale.ENGLISH);
    switch (env) {
      case "LOCAL":
        return Environment.LOCAL;
      case "PROD":
        return Environment.PROD;
      case "QA":
        return Environment.QA;
      default:
        throw new IllegalArgumentException("Unknown sentinel environment: " + env);
    }
  }

  /**
   * Create a new Sentinel configured with the system definition based on the environment specified
   * by the `sentinel` system property.
   */
  public static Sentinel get() {
    switch (environment()) {
      case LOCAL:
        return new Sentinel(SystemDefinitions.get().local());
      case PROD:
        return new Sentinel(SystemDefinitions.get().prod());
      case QA:
        return new Sentinel(SystemDefinitions.get().qa());
      default:
        throw new IllegalArgumentException("Unknown sentinel environment: " + environment());
    }
  }

  public TestClients clients() {
    return TestClients.of(system());
  }
}
