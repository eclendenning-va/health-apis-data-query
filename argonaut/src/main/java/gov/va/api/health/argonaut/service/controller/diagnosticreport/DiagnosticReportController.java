package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;

import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport.Bundle;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.service.controller.Bundler;
import gov.va.api.health.argonaut.service.controller.Bundler.BundleContext;
import gov.va.api.health.argonaut.service.controller.PageLinks.LinkConfig;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.controller.Validator;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport;
import java.util.Collections;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Request Mappings for the Argonaut Diagnostic Report Profile, see
 * https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-diagnosticreport.html
 * for implementation details.
 */
@SuppressWarnings("WeakerAccess")
@RestController
@RequestMapping(
        value = {"/api/DiagnosticReport"},
        produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
@AllArgsConstructor(onConstructor = @_({@Autowired}))
@Slf4j
public class DiagnosticReportController {

    private Transformer transformer;
    private MrAndersonClient mrAndersonClient;
    private Bundler bundler;

    private Bundle bundle(
        MultiValueMap<String, String> parameters,
        int page,
        int count,
        HttpServletRequest servletRequest) {
        CdwDiagnosticReport102Root root = search(parameters);
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
                root.getDiagnosticReports() == null ? Collections.emptyList() :
                    root.getDiagnosticReports().getDiagnosticReport(),
                transformer,
                DiagnosticReport.Entry::new,
                DiagnosticReport.Bundle::new));
    }

    /** Read by identifier. */
    @GetMapping(value = {"/{publicId}"})
    public DiagnosticReport read(@PathVariable("publicId") String publicId) {
        return transformer.apply(
                firstPayloadItem(
                        hasPayload(
                                search(Parameters.forIdentity(publicId))
                                    .getDiagnosticReports().getDiagnosticReport()
                        )));
    }

    private CdwDiagnosticReport102Root search(MultiValueMap<String, String> params) {
        Query<CdwDiagnosticReport102Root> query =
                Query.forType(CdwDiagnosticReport102Root.class)
                        .profile(Query.Profile.ARGONAUT)
                        .resource("DiagnosticReport")
                        .version("1.02")
                        .parameters(params)
                        .build();
        return mrAndersonClient.search(query);
    }

    /** Search by _id. */
    @GetMapping(params = {"_id"})
    public DiagnosticReport.Bundle searchById(
        @RequestParam("_id") String id,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "_count", defaultValue = "15") int count,
        HttpServletRequest servletRequest
    ) {
      return bundle(
          Parameters.builder()
              .add("_id", id)
              .add("page", page)
              .add("_count", count)
              .build(),
          page,
          count,
          servletRequest);
    }

  /** Search by identifier. */
  @GetMapping(params = {"identifier"})
  public DiagnosticReport.Bundle searchByIdentifier(
      @RequestParam("identifier") String identifier,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "_count", defaultValue = "15") int count,
      HttpServletRequest servletRequest
  ) {
    return bundle(
        Parameters.builder()
            .add("identifier", identifier)
            .add("page", page)
            .add("_count", count)
            .build(),
        page,
        count,
        servletRequest);
  }

    /** Search by Patient+Category. */
    @GetMapping(params = {"patient", "category"})
    public DiagnosticReport.Bundle searchByPatientAndCategory(
        @RequestParam("patient") String patient,
        @RequestParam("category") String category,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "_count", defaultValue = "15") int count,
        HttpServletRequest servletRequest
    ) {
      return bundle(
        Parameters.builder()
            .add("patient", patient)
            .add("category", category)
            .add("page", page)
            .add("_count", count)
            .build(),
        page,
        count,
        servletRequest);
    }

    /** Search by Patient+Code. */
    @GetMapping(params = {"patient", "code"})
    public DiagnosticReport.Bundle searchByPatientAndCode(
        @RequestParam("patient") String patient,
        @RequestParam("code") String code,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "_count", defaultValue = "15") int count,
        HttpServletRequest servletRequest
    ) {
        return bundle(
            Parameters.builder()
                .add("patient", patient)
                .add("code", code)
                .add("page", page)
                .add("_count", count)
                .build(),
            page,
            count,
            servletRequest);
    }

    /** Search by Patient+Category+Date. */
    @GetMapping(params = {"patient", "code", "date"})
    public DiagnosticReport.Bundle searchByPatientAndCategoryAndDate(
        @RequestParam("patient")String patient,
        @RequestParam("code")String code,
        @RequestParam(value = "date", required = false) @Size(max = 2) String[] date,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "_count", defaultValue = "15") int count,
        HttpServletRequest servletRequest
    ) {
        return bundle(
            Parameters.builder()
                .add("patient", patient)
                .add("code", code)
                .addAll("date", date)
                .add("page", page)
                .add("_count", count)
                .build(),
            page,
            count,
            servletRequest);
    }

    /** Validate Endpoint. */
    @PostMapping(
        value = "/$validate",
        consumes = {"application/json", "application/json+fhir", "application/fhir+json"}
    )
    public OperationOutcome validate(@RequestBody Bundle bundle) {
      return Validator.create().validate(bundle);
    }

    public interface Transformer extends Function<CdwDiagnosticReport, DiagnosticReport> {}
}
