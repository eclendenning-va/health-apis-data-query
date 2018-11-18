package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(description = "https://www.hl7.org/fhir/domainresource.html")
public interface DomainResource extends Resource {
  Narrative text();

  List<SimpleResource> contained();

  List<Extension> modifierExtension();

  List<Extension> extension();
}
