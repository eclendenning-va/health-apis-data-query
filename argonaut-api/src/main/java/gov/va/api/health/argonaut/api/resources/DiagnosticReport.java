package gov.va.api.health.argonaut.api.resources;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import gov.va.api.health.argonaut.api.Fhir;
import gov.va.api.health.argonaut.api.datatypes.Identifier;
import gov.va.api.health.argonaut.api.datatypes.SimpleResource;
import gov.va.api.health.argonaut.api.elements.Extension;
import gov.va.api.health.argonaut.api.elements.Meta;
import gov.va.api.health.argonaut.api.elements.Narrative;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(
        description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-diagnosticreport.html"
)
public class DiagnosticReport implements Resource{

    @Pattern(regexp = Fhir.ID)
    String id;

    @NotBlank String resourceType;
    @Valid Meta meta;

    @Pattern(regexp = Fhir.URI)
    String implicitRules;

    @Pattern(regexp = Fhir.CODE)
    String language;

    @Valid Narrative text;
    @Valid List<SimpleResource> contained;
    @Valid List<Extension> extension;
    @Valid List<Extension> modifierExtension;
    @NotEmpty @Valid List<Identifier> identifier;

    @Valid Code status;

    public enum Code {
        /** registered,
         partial,
         final,
         corrected,
         appended,
         cancelled,
         entered-in-error
         */

        FINAL
    }

}
