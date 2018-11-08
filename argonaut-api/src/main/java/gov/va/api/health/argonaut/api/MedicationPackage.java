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
public class MedicationPackage implements BackboneElement {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> extension;
  @Valid List<Extension> modifierExtension;

  @Valid CodeableConcept container;

  @Valid Content content;
}
