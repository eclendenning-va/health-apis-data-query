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
public class Meta implements Element {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> extension;

  @Pattern(regexp = Fhir.ID)
  String versionId;

  @Pattern(regexp = Fhir.INSTANT)
  String lastUpdated;

  List<@Pattern(regexp = Fhir.URI) String> profile;
  @Valid List<Coding> security;
  @Valid List<Coding> tag;
}
