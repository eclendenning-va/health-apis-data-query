package gov.va.api.health.argonaut.api.bundle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import gov.va.api.health.argonaut.api.Fhir;
import gov.va.api.health.argonaut.api.Meta;
import gov.va.api.health.argonaut.api.Resource;
import gov.va.api.health.argonaut.api.Signature;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(description = "http://hl7.org/fhir/DSTU2/bundle.html")
public abstract class AbstractBundle<E extends AbstractEntry<?>> implements Resource {
  @Pattern(regexp = Fhir.ID)
  protected final String id;

  @NotBlank String resourceType;
  @Valid protected final Meta meta;

  @Pattern(regexp = Fhir.URI)
  protected final String implicitRules;

  @Pattern(regexp = Fhir.CODE)
  protected final String language;

  @NotNull protected final BundleType type;

  @Min(0)
  protected final Integer total;

  @Valid protected final List<BundleLink> link;
  @Valid protected final List<E> entry;
  @Valid Signature signature;

  public enum BundleType {
    document,
    message,
    transaction,
    @JsonProperty("transaction-response")
    transaction_response,
    batch,
    @JsonProperty("batch-response")
    batch_response,
    history,
    searchset,
    collection
  }
}
