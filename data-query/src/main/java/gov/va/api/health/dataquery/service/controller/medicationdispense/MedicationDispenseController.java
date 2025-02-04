package gov.va.api.health.dataquery.service.controller.medicationdispense;

import static gov.va.api.health.dataquery.service.controller.Dstu2Transformers.firstPayloadItem;
import static gov.va.api.health.dataquery.service.controller.Dstu2Transformers.hasPayload;

import gov.va.api.health.dataquery.service.controller.CountParameter;
import gov.va.api.health.dataquery.service.controller.Dstu2Bundler;
import gov.va.api.health.dataquery.service.controller.Dstu2Bundler.BundleContext;
import gov.va.api.health.dataquery.service.controller.Dstu2Validator;
import gov.va.api.health.dataquery.service.controller.PageLinks.LinkConfig;
import gov.va.api.health.dataquery.service.controller.Parameters;
import gov.va.api.health.dataquery.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.dataquery.service.mranderson.client.Query;
import gov.va.api.health.dstu2.api.resources.MedicationDispense;
import gov.va.api.health.dstu2.api.resources.OperationOutcome;
import gov.va.dvp.cdw.xsd.model.CdwMedicationDispense100Root;
import java.util.Collections;
import java.util.function.Function;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("WeakerAccess")
@RestController
@RequestMapping(
  value = {"/dstu2/MedicationDispense"},
  produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class MedicationDispenseController {
  private Transformer transformer;

  private MrAndersonClient mrAndersonClient;

  private Dstu2Bundler bundler;

  private MedicationDispense.Bundle bundle(
      MultiValueMap<String, String> parameters, int page, int count) {
    CdwMedicationDispense100Root root = search(parameters);
    LinkConfig linkConfig =
        LinkConfig.builder()
            .path("MedicationDispense")
            .queryParams(parameters)
            .page(page)
            .recordsPerPage(count)
            .totalRecords(root.getRecordCount())
            .build();
    return bundler.bundle(
        BundleContext.of(
            linkConfig,
            root.getMedicationDispenses() == null
                ? Collections.emptyList()
                : root.getMedicationDispenses().getMedicationDispense(),
            transformer,
            MedicationDispense.Entry::new,
            MedicationDispense.Bundle::new));
  }

  /** Reading by id. */
  @GetMapping(value = {"/{publicId}"})
  public MedicationDispense read(@PathVariable("publicId") String publicId) {
    return transformer.apply(
        firstPayloadItem(
            hasPayload(
                search(Parameters.forIdentity(publicId))
                    .getMedicationDispenses()
                    .getMedicationDispense())));
  }

  /**
   * The XML should remain the same, but the version of the resource needs to be incremented for
   * SQL52.
   */
  private CdwMedicationDispense100Root search(MultiValueMap<String, String> params) {
    Query<CdwMedicationDispense100Root> query =
        Query.forType(CdwMedicationDispense100Root.class)
            .profile(Query.Profile.DSTU2)
            .resource("MedicationDispense")
            .version("1.02")
            .parameters(params)
            .build();
    return mrAndersonClient.search(query);
  }

  /** Searching by _id. */
  @GetMapping(params = {"_id"})
  public MedicationDispense.Bundle searchById(
      @RequestParam("_id") String id,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    return bundle(
        Parameters.builder().add("identifier", id).add("page", page).add("_count", count).build(),
        page,
        count);
  }

  /** Searching by identifier. */
  @GetMapping(params = {"identifier"})
  public MedicationDispense.Bundle searchByIdentifier(
      @RequestParam("identifier") String id,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    return bundle(
        Parameters.builder().add("identifier", id).add("page", page).add("_count", count).build(),
        page,
        count);
  }

  /** Searching by patient. */
  @GetMapping(params = {"patient"})
  public MedicationDispense.Bundle searchByPatient(
      @RequestParam("patient") String patient,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    return bundle(
        Parameters.builder().add("patient", patient).add("page", page).add("_count", count).build(),
        page,
        count);
  }

  /** Searching by patient and status. */
  @GetMapping(params = {"patient", "status"})
  public MedicationDispense.Bundle searchByPatientAndStatus(
      @RequestParam("patient") String patient,
      @RequestParam("status") String status,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    return bundle(
        Parameters.builder()
            .add("patient", patient)
            .add("status", status)
            .add("page", page)
            .add("_count", count)
            .build(),
        page,
        count);
  }

  /** Searching by patient and type. */
  @GetMapping(params = {"patient", "type"})
  public MedicationDispense.Bundle searchByPatientAndType(
      @RequestParam("patient") String patient,
      @RequestParam("type") String type,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    return bundle(
        Parameters.builder()
            .add("patient", patient)
            .add("type", type)
            .add("page", page)
            .add("_count", count)
            .build(),
        page,
        count);
  }

  /** Validation endpoint. */
  @PostMapping(
    value = "/$validate",
    consumes = {"application/json", "application/json+fhir", "application/fhir+json"}
  )
  public OperationOutcome validate(@RequestBody MedicationDispense.Bundle bundle) {
    return Dstu2Validator.create().validate(bundle);
  }

  public interface Transformer
      extends Function<
          CdwMedicationDispense100Root.CdwMedicationDispenses.CdwMedicationDispense,
          MedicationDispense> {}
}
