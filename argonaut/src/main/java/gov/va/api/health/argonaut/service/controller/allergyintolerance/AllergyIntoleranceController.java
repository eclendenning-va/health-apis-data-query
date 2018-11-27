package gov.va.api.health.argonaut.service.controller.allergyintolerance;

import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Bundle;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Bundler.BundleContext;
import gov.va.api.health.argonaut.service.controller.PageLinks.LinkConfig;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root;
import java.util.Collections;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Request Mappings for the Argonaut Allergy Intolerance Profile, see
 * http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-allergyintolerance.html for
 * implementation details.
 */
@SuppressWarnings("WeakerAccess")
@RestController
@RequestMapping(
  value = {"/api/AllergyIntolerance"},
  produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class AllergyIntoleranceController {

  private Transformer transformer;
  private MrAndersonClient mrAndersonClient;
  private Bundler bundler;

  private Bundle bundle(
      MultiValueMap<String, String> parameters,
      int page,
      int count,
      HttpServletRequest servletRequest) {
    CdwAllergyIntolerance103Root root = search(parameters);
    LinkConfig linkConfig =
        LinkConfig.builder()
            .path(servletRequest.getRequestURI())
            .queryParams(parameters)
            .page(page)
            .recordsPerPage(count)
            .totalRecords(root.getRecordCount().intValue())
            .build();
    return bundler.bundle(
        BundleContext.of(
            linkConfig,
            root.getAllergyIntolerances() == null
                ? Collections.emptyList()
                : root.getAllergyIntolerances().getAllergyIntolerance(),
            transformer,
            AllergyIntolerance.Entry::new,
            AllergyIntolerance.Bundle::new));
  }

  /** Read by id. */
  @GetMapping(value = {"/{publicId}"})
  public AllergyIntolerance read(@PathVariable("publicId") String publicId) {
    return transformer.apply(
        firstPayloadItem(
            hasPayload(
                search(Parameters.forIdentity(publicId))
                    .getAllergyIntolerances()
                    .getAllergyIntolerance())));
  }

  private CdwAllergyIntolerance103Root search(MultiValueMap<String, String> params) {
    Query<CdwAllergyIntolerance103Root> query =
        Query.forType(CdwAllergyIntolerance103Root.class)
            .profile(Query.Profile.ARGONAUT)
            .resource("AllergyIntolerance")
            .version("1.03")
            .parameters(params)
            .build();
    return mrAndersonClient.search(query);
  }

  /** Search by _id. */
  @GetMapping(params = {"_id"})
  public AllergyIntolerance.Bundle searchById(
      @RequestParam("_id") String id,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "_count", defaultValue = "1") int count,
      HttpServletRequest servletRequest) {
    return bundle(
        Parameters.builder().add("_id", id).add("page", page).add("_count", count).build(),
        page,
        count,
        servletRequest);
  }

  /** Search by patient. */
  @GetMapping(params = {"patient"})
  public AllergyIntolerance.Bundle searchByPatient(
      @RequestParam("patient") String patient,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "_count", defaultValue = "15") int count,
      HttpServletRequest servletRequest) {
    return bundle(
        Parameters.builder().add("patient", patient).add("page", page).add("_count", count).build(),
        page,
        count,
        servletRequest);
  }

  public interface Transformer
      extends Function<
          CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance,
          AllergyIntolerance> {}
}
