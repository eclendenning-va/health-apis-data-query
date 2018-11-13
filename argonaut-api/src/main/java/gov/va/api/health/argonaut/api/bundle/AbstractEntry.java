package gov.va.api.health.argonaut.api.bundle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import gov.va.api.health.argonaut.api.BackboneElement;
import gov.va.api.health.argonaut.api.Extension;
import gov.va.api.health.argonaut.api.Fhir;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(description = "http://hl7.org/fhir/DSTU2/bundle.html")
public abstract class AbstractEntry<T> implements BackboneElement {
  @Pattern(regexp = Fhir.ID)
  protected final String id;

  @Valid protected final List<Extension> extension;
  @Valid protected final List<Extension> modifierExtension;
  @Valid protected final List<BundleLink> link;

  @Pattern(regexp = Fhir.URI)
  protected final String fullUrl;

  @Valid protected final T resource;
  @Valid Search search;
  @Valid Request request;
  @Valid Response response;

  public enum SearchMode {
    match,
    include,
    outcome
  }

  public enum HttpVerb {
    GET,
    POST,
    PUT,
    DELETE
  }

  @Data
  @Builder
  @Schema(description = "http://hl7.org/fhir/DSTU2/bundle.html")
  public static class Request implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    protected final String id;

    @Valid protected final List<Extension> extension;
    @Valid protected final List<Extension> modifierExtension;

    @NotNull HttpVerb method;

    @NotBlank
    @Pattern(regexp = Fhir.URI)
    String url;

    String ifNoneMatch;

    @Pattern(regexp = Fhir.INSTANT)
    String ifModifiedSince;

    String ifMatch;
    String ifNoneExist;
  }

  @Data
  @Builder
  @Schema(description = "http://hl7.org/fhir/DSTU2/bundle.html")
  public static class Response implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    protected final String id;

    @Valid protected final List<Extension> extension;
    @Valid protected final List<Extension> modifierExtension;
    @NotBlank String status;

    @Pattern(regexp = Fhir.URI)
    String location;

    String etag;

    @Pattern(regexp = Fhir.INSTANT)
    String lastModified;
  }

  @Data
  @Builder
  @Schema(description = "http://hl7.org/fhir/DSTU2/bundle.html")
  public static class Search implements BackboneElement {
    @Pattern(regexp = Fhir.ID)
    final String id;

    @Valid List<Extension> extension;
    @Valid List<Extension> modifierExtension;
    SearchMode mode;

    @Min(0)
    @Max(1)
    BigDecimal rank;
  }
}
