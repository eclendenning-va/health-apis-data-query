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
public class Extension implements Element {

    @Pattern(regexp = Fhir.ID)
    String id;

    @Valid List<Extension> extension;

    @Pattern(regexp = Fhir.URI)
    String url;

    Integer valueInteger;
    Double valueDecimal;

    @Pattern(regexp = Fhir.DATETIME)
    String valueDateTime;

    @Pattern(regexp = Fhir.DATE)
    String valueDate;

    @Pattern(regexp = Fhir.INSTANT)
    String valueInstant;

    String valueString;

    @Pattern(regexp = Fhir.URI)
    String valueUri;

    Boolean valueBoolean;

    @Pattern(regexp = Fhir.CODE)
    String valueCode;

    @Pattern(regexp = Fhir.BASE64)
    String valueBase64Binary;

    @Valid Coding valueCoding;
    @Valid CodeableConcept valueCodeableConcept;
    @Valid Attachment valueAttachment;
    @Valid Identifier valueIdentifier;
    @Valid Quantity valueQuantity;
    @Valid Range valueRange;
    @Valid Period valuePeriod;
    @Valid Ratio valueRatio;
    @Valid HumanName valueHumanName;
    @Valid Address valueAddress;
    @Valid ContactPoint valueContactPoint;
    @Valid Reference valueReference;
}