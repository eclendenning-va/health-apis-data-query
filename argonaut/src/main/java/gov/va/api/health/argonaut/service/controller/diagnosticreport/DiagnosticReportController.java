package gov.va.api.health.argonaut.service.controller.diagnosticreport;

import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.service.controller.Parameters;
import gov.va.api.health.argonaut.service.mranderson.client.Query;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root;
import gov.va.dvp.cdw.xsd.model.CdwDiagnosticReport102Root.CdwDiagnosticReports.CdwDiagnosticReport;
import gov.va.api.health.argonaut.service.mranderson.client.MrAndersonClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;

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

    /** Read by id. */
    @GetMapping(value = {"/{publicId}"})
    public DiagnosticReport read(@PathVariable("publicId") String publicId) {
        return transformer.apply(
                firstPayloadItem(
                        hasPayload(
                                search(Parameters.forIdentity(publicId)).getDiagnosticReports().getDiagnosticReport()
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

    public interface Transformer extends Function<CdwDiagnosticReport, DiagnosticReport> {}
}
