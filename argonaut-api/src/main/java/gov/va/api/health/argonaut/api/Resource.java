package gov.va.api.health.argonaut.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "https://www.hl7.org/fhir/resource.html")
public interface Resource {
  String id();

  Meta meta();

  String implicitRules();

  String language();
}
