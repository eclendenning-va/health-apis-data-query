package gov.va.api.health.argonaut.service.mranderson;

import lombok.Builder;
import lombok.Value;
import org.springframework.util.MultiValueMap;

@Builder
@Value
public class MrAndersonQuery {
  String version;
  Profile profile;
  MultiValueMap<String, String> queryParams;
  Class<?> resource;
}
