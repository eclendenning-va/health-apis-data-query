package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(description = "http://hl7.org/fhir/DSTU2/datatypes.html#Coding")
public class Coding {
  @Pattern(regexp = Fhir.URI)
  String system;

  String version;

  @Pattern(regexp = Fhir.CODE)
  String code;

  String display;
  Boolean userSelected;
}
