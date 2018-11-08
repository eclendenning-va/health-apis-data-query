package gov.va.api.health.argonaut.api.validation;

import gov.va.api.health.argonaut.api.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "get")
class SampleExtensions {

  Patient patientWithSingleRequiredEthnicityExtension() {
    return Patient.builder()
        .resourceType("Patient")
        .extension(singleRequiredEthnicityExtension())
        .identifier(identifier())
        .gender(Patient.Gender.unknown)
        .name(name())
        .build();
  }

  Patient patientWithNullEthnicityExtension() {
    return Patient.builder()
        .resourceType("Patient")
        .extension(nullEthnicityExtension())
        .identifier(identifier())
        .gender(Patient.Gender.unknown)
        .name(name())
        .build();
  }

  Patient patientWithSingleOptionalEthnicityExtension() {
    return Patient.builder()
        .resourceType("Patient")
        .extension(singleOptionalEthnicityExtension())
        .identifier(identifier())
        .gender(Patient.Gender.unknown)
        .name(name())
        .build();
  }

  Patient patientWithMultipleOptionalEthnicityExtension() {
    return Patient.builder()
        .resourceType("Patient")
        .extension(multipleOptionalEthnicityExtension())
        .identifier(identifier())
        .gender(Patient.Gender.unknown)
        .name(name())
        .build();
  }

  Patient patientWithNoRequiredEthnicityExtension() {
    return Patient.builder()
        .resourceType("Patient")
        .extension(noRequiredEthnicityExtension())
        .identifier(identifier())
        .gender(Patient.Gender.unknown)
        .name(name())
        .build();
  }

  Patient patientWithTooManyRequiredEthnicityExtension() {
    return Patient.builder()
        .resourceType("Patient")
        .extension(tooManyRequiredEthnicityExtension())
        .identifier(identifier())
        .gender(Patient.Gender.unknown)
        .name(name())
        .build();
  }

  Patient patientWithTooManyOptionalEthnicityExtension() {
    return Patient.builder()
        .resourceType("Patient")
        .extension(tooManyOptionalEthnicityExtension())
        .identifier(identifier())
        .gender(Patient.Gender.unknown)
        .name(name())
        .build();
  }

  List<Extension> singleRequiredEthnicityExtension() {
    List<Extension> extensions = new ArrayList<>(3);

    List<Extension> raceExtensions = new LinkedList<>();
    raceExtensions.add(
        Extension.builder()
            .url("ombCategory")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/ValueSet/v3-Ethnicity")
                    .code("2135-2")
                    .display("Hispanic or Latino")
                    .build())
            .build());
    raceExtensions.add(Extension.builder().url("text").valueString("tester").build());

    List<Extension> ethnicityExtensions = new LinkedList<>();
    ethnicityExtensions.add(Extension.builder().url("text").valueString("Spaniard").build());

    extensions.add(Extension.builder().url("http://test-race").extension(raceExtensions).build());
    extensions.add(
        Extension.builder()
            .url("http://fhir.org/guides/argonaut/StructureDefinition/argo-ethnicity")
            .extension(ethnicityExtensions)
            .build());
    extensions.add(Extension.builder().url("http://test-birthsex").valueCode("M").build());
    return extensions;
  }

  List<Extension> nullEthnicityExtension() {
    List<Extension> extensions = new ArrayList<>(3);

    List<Extension> raceExtensions = new LinkedList<>();
    raceExtensions.add(
        Extension.builder()
            .url("ombCategory")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/ValueSet/v3-Ethnicity")
                    .code("2135-2")
                    .display("Hispanic or Latino")
                    .build())
            .build());
    raceExtensions.add(Extension.builder().url("text").valueString("tester").build());
    extensions.add(Extension.builder().url("http://test-race").extension(raceExtensions).build());
    extensions.add(Extension.builder().url("http://test-birthsex").valueCode("M").build());
    return extensions;
  }

  List<Extension> singleOptionalEthnicityExtension() {
    List<Extension> extensions = new ArrayList<>(3);

    List<Extension> ethnicityExtensions = new LinkedList<>();
    ethnicityExtensions.add(
        Extension.builder()
            .url("ombCategory")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/ValueSet/v3-Ethnicity")
                    .code("2135-2")
                    .display("Hispanic or Latino")
                    .build())
            .build());
    ethnicityExtensions.add(
        Extension.builder()
            .url("detailed")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/v3/Ethnicity")
                    .code("2137-8")
                    .display("Spaniard")
                    .build())
            .build());
    ethnicityExtensions.add(Extension.builder().url("text").valueString("testa").build());

    extensions.add(
        Extension.builder()
            .url("http://fhir.org/guides/argonaut/StructureDefinition/argo-ethnicity")
            .extension(ethnicityExtensions)
            .build());
    return extensions;
  }

  List<Extension> multipleOptionalEthnicityExtension() {
    List<Extension> extensions = new ArrayList<>(3);

    List<Extension> ethnicityExtensions = new LinkedList<>();
    ethnicityExtensions.add(
        Extension.builder()
            .url("ombCategory")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/ValueSet/v3-Ethnicity")
                    .code("2135-2")
                    .display("Hispanic or Latino")
                    .build())
            .build());
    ethnicityExtensions.add(
        Extension.builder()
            .url("detailed")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/v3/Ethnicity")
                    .code("2137-8")
                    .display("Spaniard")
                    .build())
            .build());
    ethnicityExtensions.add(
        Extension.builder()
            .url("detailed")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/v3/Ethnicity")
                    .code("2138-6")
                    .display("Andalusian")
                    .build())
            .build());
    ethnicityExtensions.add(
        Extension.builder()
            .url("detailed")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/v3/Ethnicity")
                    .code("2139-4")
                    .display("Asturian")
                    .build())
            .build());
    ethnicityExtensions.add(Extension.builder().url("text").valueString("Spaniard").build());

    extensions.add(
        Extension.builder()
            .url("http://fhir.org/guides/argonaut/StructureDefinition/argo-ethnicity")
            .extension(ethnicityExtensions)
            .build());
    return extensions;
  }

  List<Extension> noRequiredEthnicityExtension() {
    List<Extension> extensions = new ArrayList<>(3);

    List<Extension> ethnicityExtensions = new LinkedList<>();
    extensions.add(
        Extension.builder()
            .url("http://fhir.org/guides/argonaut/StructureDefinition/argo-ethnicity")
            .extension(ethnicityExtensions)
            .build());
    return extensions;
  }

  List<Extension> tooManyRequiredEthnicityExtension() {
    List<Extension> extensions = new ArrayList<>(3);

    List<Extension> ethnicityExtensions = new LinkedList<>();
    ethnicityExtensions.add(Extension.builder().url("text").valueString("Spaniard").build());
    ethnicityExtensions.add(Extension.builder().url("text").valueString("Andalusian").build());

    extensions.add(
        Extension.builder()
            .url("http://fhir.org/guides/argonaut/StructureDefinition/argo-ethnicity")
            .extension(ethnicityExtensions)
            .build());
    return extensions;
  }

  List<Extension> tooManyOptionalEthnicityExtension() {
    List<Extension> extensions = new ArrayList<>(3);
    List<Extension> ethnicityExtensions = new LinkedList<>();

    ethnicityExtensions.add(
        Extension.builder()
            .url("ombCategory")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/ValueSet/v3-Ethnicity")
                    .code("2135-2")
                    .display("Hispanic or Latino")
                    .build())
            .build());

    ethnicityExtensions.add(
        Extension.builder()
            .url("ombCategory")
            .valueCoding(
                Coding.builder()
                    .system("http://hl7.org/fhir/ValueSet/v3-Ethnicity")
                    .code("2135-2")
                    .display("Hispanic or Latino")
                    .build())
            .build());

    ethnicityExtensions.add(Extension.builder().url("text").valueString("Spaniard").build());

    extensions.add(
        Extension.builder()
            .url("http://fhir.org/guides/argonaut/StructureDefinition/argo-ethnicity")
            .extension(ethnicityExtensions)
            .build());

    return extensions;
  }

  List<HumanName> name() {
    return Collections.singletonList(
        HumanName.builder()
            .use(HumanName.NameUse.usual)
            .text("FOOMAN FOO")
            .family(Collections.singletonList("FOO"))
            .given(Collections.singletonList("FOOMAN"))
            .build());
  }

  List<Identifier> identifier() {
    List<Identifier> identifiers = new LinkedList<>();
    identifiers.add(
        Identifier.builder()
            .use(Identifier.IdentifierUse.usual)
            .type(
                CodeableConcept.builder()
                    .coding(
                        Collections.singletonList(
                            Coding.builder().system("http://test-code").code("C0D3").build()))
                    .build())
            .system("http://test-system")
            .value("123456789")
            .assigner(Reference.builder().display("tester-test-index").build())
            .build());

    return identifiers;
  }
}
