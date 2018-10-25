package gov.va.api.health.mranderson.util;

import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@NoArgsConstructor(staticName = "builder")
public class Parameters {

  private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

  public static MultiValueMap<String, String> empty() {
    return builder().build();
  }

  public Parameters add(String key, String value) {
    params.add(key, value);
    return this;
  }

  public Parameters addAll(String key, List<String> values) {
    params.addAll(key, values);
    return this;
  }

  public MultiValueMap<String, String> build() {
    return CollectionUtils.unmodifiableMultiValueMap(params);
  }
}
