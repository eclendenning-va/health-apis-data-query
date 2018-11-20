package gov.va.api.health.argonaut.service.controller.conformance;

import gov.va.api.health.argonaut.api.resources.Conformance;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = {"/api/metadata"},
    produces = {"application/json", "application/json+fhir", "application/fhir+json"})
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class MetadataController {

  private final ConformanceStatementProperties properties;

  @GetMapping
  Conformance read() {
    return Conformance.builder().build();
  }
}
