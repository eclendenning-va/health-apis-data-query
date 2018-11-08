package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Content implements BackboneElement {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> extension;
  @Valid List<Extension> modifierExtension;

  @NotBlank @Valid Reference item;

  @Valid SimpleQuantity amount;
}
