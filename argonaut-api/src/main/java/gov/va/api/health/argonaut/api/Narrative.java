package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Narrative implements Element {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> extension;

    @NotNull NarrativeStatus status;

    @NotBlank
    @Pattern(regexp = Fhir.XHTML)
    String div;

    public enum NarrativeStatus {
        generated,
        extensions,
        additional,
        empty
    }
}