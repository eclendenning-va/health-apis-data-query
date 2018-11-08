package gov.va.api.health.argonaut.api;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

public class Product {
  @Pattern(regexp = Fhir.ID)
  String id;

  @Valid List<Extension> extension;
  @Valid List<Extension> modifierExtension;
  @Valid CodeableConcept form;

  @Valid Ingredient ingredient;
  @Valid Batch batch;
}
