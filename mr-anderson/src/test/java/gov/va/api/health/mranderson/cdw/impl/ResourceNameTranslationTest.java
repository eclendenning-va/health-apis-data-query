package gov.va.api.health.mranderson.cdw.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ResourceNameTranslationTest {

  @Test
  public void conversion() {
    assertThat(ResourceNameTranslation.create().fhirToIdentityService("Single"), is("SINGLE"));
    assertThat(
        ResourceNameTranslation.create().fhirToIdentityService("DoubleWord"), is("DOUBLE_WORD"));
    assertThat(
        ResourceNameTranslation.create().fhirToIdentityService("SoManyWords"), is("SO_MANY_WORDS"));

    assertThat(ResourceNameTranslation.create().identityServiceToFhir("SINGLE"), is("Single"));
    assertThat(
        ResourceNameTranslation.create().identityServiceToFhir("DOUBLE_WORD"), is("DoubleWord"));
    assertThat(
        ResourceNameTranslation.create().identityServiceToFhir("SO_MANY_WORDS"), is("SoManyWords"));
  }
}
