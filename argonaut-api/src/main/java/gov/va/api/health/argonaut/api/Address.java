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
public class Address implements Element {
    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> extension;

    AddressUse use;
    AddressType type;
    String text;
    List<String> line;
    String city;
    String district;
    String state;
    String postalCode;
    String country;
    @Valid Period period;

    public enum AddressUse {
        home,
        work,
        temp,
        old
    }

    private enum AddressType {
        postal,
        physical,
        both
    }
}