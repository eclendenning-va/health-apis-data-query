package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Coding {
  @Pattern(regexp = Fhir.URI)
  String system;

  String version;

  @Pattern(regexp = Fhir.CODE)
  String code;

  String display;
  Boolean userSelected;
}
