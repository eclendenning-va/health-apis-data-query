package gov.va.api.health.argonaut.api.samples;

import gov.va.api.health.argonaut.api.resources.Location;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@NoArgsConstructor(staticName = "get")
public class SampleLocations {
  @Delegate SampleDataTypes dataTypes = SampleDataTypes.get();

  public Location location;

  
}
