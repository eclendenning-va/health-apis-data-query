package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Period implements Element {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> extension;

    @Pattern(regexp = Fhir.DATETIME)
    String start;

    @Pattern(regexp = Fhir.DATETIME)
    String end;
}