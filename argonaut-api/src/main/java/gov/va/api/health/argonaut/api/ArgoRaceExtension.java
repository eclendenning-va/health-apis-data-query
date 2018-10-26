package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ArgoRaceExtension {
  @Pattern(regexp = Fhir.ID)
  String id;
  // TODO custom validator to match
  // http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-race.html
  @Valid List<Extension> extension;

  @Pattern(regexp = Fhir.URI)
  String url;
}
