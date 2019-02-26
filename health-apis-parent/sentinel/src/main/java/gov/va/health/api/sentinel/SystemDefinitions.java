package gov.va.health.api.sentinel;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * The Sentinel provides the easy-to-use entry point to determining and accessing test
 * configurations that are specific for different environments.
 *
 * <p>It leverages the system property `sentinel` to determine which environment configuration
 * should be leveraged.
 *
 * <p>The standard system configurations for typical environments like QA or PROD.
 */
public interface SystemDefinitions {
  /**
   * Checks for system property access-token. Supplies it if it exists and throws an exception if it
   * doesn't.
   */
  static Supplier<Optional<String>> magicAccessToken() {
    final String magic = System.getProperty("access-token");
    if (isBlank(magic)) {
      throw new IllegalStateException("Access token not specified, -Daccess-token=<value>");
    }
    return () -> Optional.of(magic);
  }

  SystemDefinition systemDefinition();

  /** Specifies the particulars for interacting with the different services within a system. */
  interface SystemDefinition {}
}
