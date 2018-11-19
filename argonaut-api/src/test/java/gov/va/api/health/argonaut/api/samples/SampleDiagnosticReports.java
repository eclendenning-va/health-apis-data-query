package gov.va.api.health.argonaut.api.samples;

import gov.va.api.health.argonaut.api.Fhir;
import gov.va.api.health.argonaut.api.datatypes.Attachment;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

@SuppressWarnings("WeakerAccess")
@NoArgsConstructor(staticName = "get")
public class SampleDiagnosticReports {

    @Delegate
    SampleDataTypes dataTypes = SampleDataTypes.get();

    public DiagnosticReport diagnosticReport() {
        return DiagnosticReport.builder()
                .id("1234")
                .resourceType("Diagnostic Report")
                .meta(meta())
                .implicitRules("https://HelloRules.com")
                .language("Hello Language")
                .text(narrative())
                .contained(singletonList(resource()))
                .extension(Arrays.asList(extension(), extension()))
                .modifierExtension(
                        Arrays.asList(extension(), extensionWithQuantity(), extensionWithRatio()))
                .identifier(singletonList(identifier()))
                .status(DiagnosticReport.Code._final)
                .category(codeableConcept())
                .code(codeableConcept())
                .subject(reference())
                .encounter(reference())
                .effectiveDateTime("2000-10-01")
                .issued("1970-01-01T00:00:00Z")
                .performer(reference())
                .request(Arrays.asList(reference(),reference()))
                .specimen(Arrays.asList(reference(),reference(),reference()))
                .result(singletonList(reference()))
                .imagingStudy(singletonList(reference()))
                .image(image())
                .conclusion("The end.")
                .codedDiagnosis(Arrays.asList(codeableConcept(), codeableConcept()))
                .presentedForm(Arrays.asList(attachment(), attachment()))
                .build();
    }

    private Attachment attachment() {
        return Attachment.builder()
                .id("3579")
                .extension(singletonList(extension()))
                .contentType("Attachment")
                .language("wordz")
                .data("abc")
                .url("http://urlz.com")
                .size(10)
                .hash("hashz")
                .title("hello")
                .creation("1990-11-11")
                .build();
    }

    private List<DiagnosticReport.Image> image() {
        return singletonList(DiagnosticReport.Image.builder()
                .id("456")
                .extension(Arrays.asList(extension(),extension()))
                .modifierExtension(
                        Arrays.asList(extension(), extensionWithQuantity(), extensionWithRatio()))
                .comment("Hello?")
                .link(reference())
                .build());
    }

}
