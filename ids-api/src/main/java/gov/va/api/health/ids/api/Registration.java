package gov.va.api.health.ids.api;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class Registration {
  String uuid;
  @Singular List<ResourceIdentity> resourceIdentities;
}
