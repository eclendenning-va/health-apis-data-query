package gov.va.api.health.argonaut.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "http://hl7.org/fhir/DSTU2/backboneelement.html")
public interface Resource {
  String id();

  Meta meta();

  String implicitRules();

  String language();
}
