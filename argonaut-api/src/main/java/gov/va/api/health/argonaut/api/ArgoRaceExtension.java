package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-race.html")
public class ArgoRaceExtension {
  @Pattern(regexp = Fhir.ID)
  String id;

  // TODO https://vasdvp.atlassian.net/browse/API-133
  @Valid List<Extension> extension;

  @Pattern(regexp = Fhir.URI)
  String url;
}
