package gov.va.api.health.argonaut.api;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

public class ArgoBirthSexExtension {
    @Pattern(regexp = Fhir.ID)
    String id;
    @Valid List<Extension> extension;
    @Pattern(regexp = Fhir.URI)
    String url;
    @Pattern(regexp = Fhir.CODE)
    String valueCode;
}
