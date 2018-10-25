package gov.va.api.health.argonaut.api;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

public class ArgoRaceExtension {
  @Pattern(regexp = Fhir.ID)
  String id;
  // TODO custom validator to match
  // http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-race.html
  @Valid List<Extension> extension;

  @Pattern(regexp = Fhir.URI)
  String url;
}
