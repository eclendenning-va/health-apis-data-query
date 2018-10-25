package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Attachment implements Element {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> extension;

  @Pattern(regexp = Fhir.CODE)
  String contentType;

  @Pattern(regexp = Fhir.CODE)
  String language;

  @Pattern(regexp = Fhir.BASE64)
  String data;

  @Pattern(regexp = Fhir.URI)
  String url;

  @Min(0)
  Integer size;

  @Pattern(regexp = Fhir.BASE64)
  String hash;

  String title;

  @Pattern(regexp = Fhir.DATETIME)
  String creation;
}
