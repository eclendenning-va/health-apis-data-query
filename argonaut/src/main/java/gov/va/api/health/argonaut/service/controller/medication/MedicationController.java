package gov.va.api.health.argonaut.service.controller.medication;

import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;

import gov.va.api.health.argonaut.api.Medication;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.pojos.Medication101Root;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Request Mappings for the Argonaut Medication Profile, see
 * https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medication.html for
 * implementation details.
 */
@RestController
@RequestMapping(
  value = {"/api/Medication"},
  produces = {"application/json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class MedicationController {

  private Transformer medicationTransformer;
  private MrAndersonClient client;

  /** Read by id. */
  @GetMapping(value = {"/{publicId}"})
  public Medication read(@PathVariable("publicId") String publicId) {

    Query<Medication101Root> query =
        Query.forType(Medication101Root.class)
            .profile(Query.Profile.ARGONAUT)
            .resource("Medication")
            .version("1.01")
            .parameters(Parameters.forIdentity(publicId))
            .build();

    Medication101Root root = client.search(query);

    return medicationTransformer.apply(
        firstPayloadItem(hasPayload(root.getMedications()).getMedication()));
  }

  public interface Transformer
      extends Function<Medication101Root.Medications.Medication, Medication> {}
}
