package gov.va.api.health.mranderson.cdw.impl;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * This utility provides the two-way translation of resource names from FHIR to Identity Service
 * based a set of rules that convert from CamelCaseNames of FHIR to UPPERCASE_NAMES of Identity
 * Service.
 *
 * <pre>
 * Patient -> PATIENT
 * AllergyIntolerance -> ALLERGY_INTOLERANCE
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ResourceNameTranslation {

  /**
   * Create a new instance.
   *
   * @return A new instance.
   */
  static ResourceNameTranslation create() {
    return new ResourceNameTranslation();
  }

  /**
   * Given a FHIR name, magically turn it into an Identity Service name. For example,
   *
   * <pre>
   * Patient -> PATIENT
   * AllergyIntolerance -> ALLERGY_INTOLERANCE
   * </pre>
   */
  String fhirToIdentityService(String fhirName) {
    return Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(fhirName))
        .stream()
        .map(s -> s.toUpperCase(Locale.ENGLISH))
        .collect(Collectors.joining("_"));
  }

  /**
   * Given an Identity Service name, magically turn it into a FHIR name. For example,
   *
   * <pre>
   * PATIENT -> Patient
   * ALLERGY_INTOLERANCE -> AllergyIntolerance
   * </pre>
   */
  String identityServiceToFhir(String identityServiceName) {
    return Arrays.asList(StringUtils.splitByWholeSeparator(identityServiceName, "_"))
        .stream()
        .map(s -> StringUtils.capitalize(s.toLowerCase(Locale.ENGLISH)))
        .collect(Collectors.joining());
  }
}
