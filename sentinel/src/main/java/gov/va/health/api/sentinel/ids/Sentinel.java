package gov.va.health.api.sentinel.ids;

import lombok.Value;

@Value
public class Sentinel {

  private SystemDefinition system;

  public static Sentinel get() {
    // Choose system magically!
    return new Sentinel(SystemDefinitions.get().local());
  }

  public TestClients clients() {
    return TestClients.of(system());
  }
}
