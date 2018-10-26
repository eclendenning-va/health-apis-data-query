package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ContactPoint implements Element {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> extension;
  @NotBlank ContactPointSystem system;
  @NotBlank ContactPointUse use;
  String value;

  @Min(1)
  Integer rank;

  @Valid Period period;

  enum ContactPointSystem {
    phone,
    fax,
    email,
    pager,
    other
  }

  enum ContactPointUse {
    home,
    work,
    temp,
    old,
    mobile
  }
}
