package gov.va.api.health.argonaut.service.controller.medicationorder;

import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;

import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.controller.medicationorder.MedicationOrderController.Transformer;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.api.health.argonaut.service.mranderson.client.Query.Profile;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root;
import gov.va.dvp.cdw.xsd.model.CdwMedicationOrder103Root.CdwMedicationOrders.CdwMedicationOrder;
import groovy.util.logging.Slf4j;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = {"/api/MedicationOrder"},
    produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class MedicationOrderController {

  private Transformer transformer;
  private MrAndersonClient mrAndersonClient;
  private Bundler bundler;

  /** Read by identifier */
  @GetMapping(value = {"/{publicId}"})
  public MedicationOrder read(@PathVariable("publicId") String publicId) {
    return transformer.apply(
        firstPayloadItem(
            hasPayload(
                search(Parameters.forIdentity(publicId))
                  .getMedicationOrders()
                  .getMedicationOrder())));
  }

  private CdwMedicationOrder103Root search(MultiValueMap<String, String> params) {
    Query<CdwMedicationOrder103Root> query =
        Query.forType(CdwMedicationOrder103Root.class)
          .profile(Profile.ARGONAUT)
          .resource("MedicationOrder")
          .version("1.03")
          .parameters(params)
          .build();
    return mrAndersonClient.search(query);
  }

  public interface Transformer extends Function<CdwMedicationOrder, MedicationOrder> {}
}
