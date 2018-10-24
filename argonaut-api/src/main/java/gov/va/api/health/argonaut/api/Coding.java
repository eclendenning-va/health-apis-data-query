package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Coding {
    @Pattern(regexp = Fhir.URI)
    String system;

    String version;

    @Pattern(regexp = Fhir.CODE)
    String code;

    String display;
    Boolean userSelected;
}