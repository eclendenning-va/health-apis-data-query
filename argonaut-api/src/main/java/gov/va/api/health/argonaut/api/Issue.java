package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(
  description = "https://www.hl7.org/fhir/operationoutcome-definitions.html#OperationOutcome.issue"
)
public class Issue implements BackboneElement {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> modifierExtension;

  @Valid List<Extension> extension;

  @NotNull IssueSeverity severity;

  @NotBlank
  @Pattern(regexp = Fhir.CODE)
  @Schema(description = "http://hl7.org/fhir/DSTU2/valueset-issue-type.html")
  String code;

  @Valid CodeableConcept details;

  String diagnostics;

  List<String> location;

  List<String> expression;

  public enum IssueSeverity {
    fatal,
    error,
    warning,
    information
  }
}
