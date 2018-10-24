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
public class HumanName implements Element {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> extension;

    NameUse use;
    String text;
    List<String> family;
    List<String> given;
    List<String> prefix;
    List<String> suffix;
    @Valid Period period;

    public enum NameUse {
        usual,
        official,
        temp,
        nickname,
        anonymous,
        old,
        maiden
    }
}