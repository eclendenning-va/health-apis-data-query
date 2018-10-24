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
public class Quantity implements Element {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> extension;

    Double value;

    @Pattern(regexp = "(<|<=|>=|>)")
    String comparator;

    String unit;

    @Pattern(regexp = Fhir.CODE)
    String code;
}