package gov.va.api.health.argonaut.service.controller.practitioner;

import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;

import gov.va.api.health.argonaut.api.resources.Practitioner;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwPractitioner100Root;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Request Mappings for the Argonaut Practitioner Profile, see
 * http://hl7.org/fhir/DSTU2/practitioner.html for implementation details.
 */
@SuppressWarnings("WeakerAccess")
@RestController
@RequestMapping(
  value = {"/api/Practitioner"},
  produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class PractitionerController {

  private Transformer transformer;
  private MrAndersonClient mrAndersonClient;

  /** Read by id. */
  @GetMapping(value = {"/{publicId}"})
  public Practitioner read(@PathVariable("publicId") String publicId) {

    return transformer.apply(
        firstPayloadItem(
            hasPayload(
                search(Parameters.forIdentity(publicId)).getPractitioners().getPractitioner())));
  }

  private CdwPractitioner100Root search(MultiValueMap<String, String> params) {
    Query<CdwPractitioner100Root> query =
        Query.forType(CdwPractitioner100Root.class)
            .profile(Query.Profile.ARGONAUT)
            .resource("Practitioner")
            .version("1.00")
            .parameters(params)
            .build();
    return mrAndersonClient.search(query);
  }

  public interface Transformer
      extends Function<CdwPractitioner100Root.CdwPractitioners.CdwPractitioner, Practitioner> {}
}
