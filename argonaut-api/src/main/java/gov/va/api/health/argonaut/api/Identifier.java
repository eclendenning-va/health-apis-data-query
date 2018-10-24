package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import gov.va.api.health.argonaut.api.Fhir.CodeableConcept;
import gov.va.api.health.argonaut.api.Fhir.Extension;
import gov.va.api.health.argonaut.api.Fhir.Period;
import gov.va.api.health.argonaut.api.Fhir.Reference;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Identifier implements Fhir.Element {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> extension;

  IdentifierUse use;

  @Valid CodeableConcept type;

  @Pattern(regexp = Fhir.URI)
  String system;

  String value;
  @Valid Period period;
  @Valid Reference assigner;

  public enum IdentifierUse {
    usual,
    official,
    temp,
    secondary
  }
}
