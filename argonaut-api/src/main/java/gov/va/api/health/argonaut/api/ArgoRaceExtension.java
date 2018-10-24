package gov.va.api.health.argonaut.api;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

public class ArgoRaceExtension {
    @Pattern(regexp = Fhir.ID)
    String id;
    //TODO custom validator to match http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-race.html
    @Valid List<Extension> extension;
    @Pattern(regexp = Fhir.URI)
    String url;
}
