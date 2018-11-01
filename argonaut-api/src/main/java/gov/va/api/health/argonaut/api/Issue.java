package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(description = "https://www.hl7.org/fhir/operationoutcome-definitions.html#OperationOutcome.issue")
public class Issue implements BackboneElement {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> modifierExtension;

  @Valid List<Extension> extension;

  @NotBlank
  @Pattern(regexp = Fhir.CODE)
  String severity;

  @NotBlank
  @Pattern(regexp = Fhir.CODE)
  String code;

  @Valid CodeableConcept details;

  String diagnostics;

  List<String> location;

  List<String> expression;
}
