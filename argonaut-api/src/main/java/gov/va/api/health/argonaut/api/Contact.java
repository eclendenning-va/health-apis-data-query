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
public class Contact implements BackboneElement {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> modifierExtension;
  @Valid List<Extension> extension;
  @Valid List<CodeableConcept> relationship;
  @Valid HumanName name;
  @Valid List<ContactPoint> telecom;
  @Valid Address address;

  Gender gender;

  @Valid Reference organization;
  @Valid Period period;

  public enum Gender {
    male,
    female,
    other,
    unknown
  }
}
