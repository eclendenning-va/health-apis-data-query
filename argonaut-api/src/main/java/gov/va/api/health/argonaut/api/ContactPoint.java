package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
@Schema(description = "http://hl7.org/fhir/DSTU2/datatypes.html#ContactPoint")
public class ContactPoint implements Element {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> extension;
  @NotBlank ContactPointSystem system;
  String value;
  @NotBlank ContactPointUse use;

  @Min(1)
  Integer rank;

  @Valid Period period;

  public enum ContactPointSystem {
    phone,
    fax,
    email,
    pager,
    other
  }

  public enum ContactPointUse {
    home,
    work,
    temp,
    old,
    mobile
  }
}
