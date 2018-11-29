package gov.va.api.health.mranderson.cdw.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import gov.va.api.health.ids.api.IdentityService;
import gov.va.api.health.ids.api.IdentityService.LookupFailed;
import gov.va.api.health.ids.api.IdentityService.RegistrationFailed;
import gov.va.api.health.ids.api.IdentityService.UnknownIdentity;
import gov.va.api.health.ids.api.Registration;
import gov.va.api.health.ids.api.ResourceIdentity;
import gov.va.api.health.mranderson.Samples;
import gov.va.api.health.mranderson.cdw.Profile;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.ResourceRepository;
import gov.va.api.health.mranderson.cdw.Resources.MissingSearchParameters;
import gov.va.api.health.mranderson.cdw.Resources.SearchFailed;
import gov.va.api.health.mranderson.cdw.Resources.UnknownIdentityInSearchParameter;
import gov.va.api.health.mranderson.cdw.Resources.UnknownResource;
import gov.va.api.health.mranderson.util.Parameters;
import gov.va.api.health.mranderson.util.XmlDocuments;
import java.util.Arrays;
import java.util.Collections;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WitnessProtectionResourcesTest {
  @Rule public final ExpectedException thrown = ExpectedException.none();
  @Mock ResourceRepository repository;
  @Mock IdentityService uis;
  private WitnessProtectionResources resources;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    resources = new WitnessProtectionResources(repository, uis);
  }

  @Test
  public void emptySearchResultsAreReturned() {
    String expected = standardizeFormatting(mockResults(Samples.create().emptySearchResults()));
    String actual = resources.search(forResource("patient", "123"));
    verify(repository).execute(forResource("patient", "123"));
    assertThat(actual).isEqualTo(expected);
  }

  private Query forAnything() {
    String identity = "identifier";
    String value = "123";
    return forResource(identity, value);
  }

  private Query forResource(String identity, String value) {
    return Query.builder()
        .profile(Profile.ARGONAUT)
        .resource("Whatever")
        .version("1.23")
        .page(1)
        .count(2)
        .raw(false)
        .parameters(Parameters.builder().add(identity, value).add("what", "ever").build())
        .build();
  }

  @Test
  public void missingParametersExceptionIsThrowWhenNoParametersAreSpecified() {
    thrown.expect(MissingSearchParameters.class);
    resources.search(
        Query.builder()
            .profile(Profile.ARGONAUT)
            .resource("Whatever")
            .version("1.23")
            .page(1)
            .count(2)
            .raw(false)
            .parameters(Parameters.empty())
            .build());
  }

  @Test
  public void missingParametersExceptionIsThrowWhenNoParametersValuesAreSpecified() {
    thrown.expect(MissingSearchParameters.class);
    resources.search(
        Query.builder()
            .profile(Profile.ARGONAUT)
            .resource("Whatever")
            .version("1.23")
            .page(1)
            .count(2)
            .raw(false)
            .parameters(Parameters.builder().add("patient", "").build())
            .build());
  }

  private void mockLookup() {
    when(uis.lookup("123"))
        .thenReturn(
            Collections.singletonList(
                ResourceIdentity.builder()
                    .identifier("MAGIC321")
                    .resource("PATIENT")
                    .system("CDW")
                    .build()));
  }

  private void mockRegister(Registration... registrations) {
    when(uis.register(Mockito.anyList())).thenReturn(Arrays.asList(registrations));
  }

  private String mockResults(String result) {
    when(repository.execute(Mockito.any())).thenReturn(result);
    return result;
  }

  private String mockWithFakeSampleAndReplaceReferences() {
    mockResults(Samples.create().fakeWithReferences());
    mockRegister(
        // first whatever
        register("WHATEVER", 1000, "MAGIC9000"),
        register("PATIENT", 1100, "MAGIC8900"),
        register("MEDICATION", 1200, "MAGIC8800"),
        register("CONDITION", 1310, "MAGIC8790"),
        register("ALLERGY_INTOLERANCE", 1320, "MAGIC8780"),
        // second whatever
        register("WHATEVER", 2000, "MAGIC8000"),
        register("PATIENT", 2100, "MAGIC7900"),
        register("CONDITION", 2310, "MAGIC7790"),
        register("ALLERGY_INTOLERANCE", 2320, "MAGIC7780"),
        // something we didn't ask for
        register("TELEMARKETER", 666, "IGNOREME"));

    return standardizeFormatting(
        Samples.create()
            .fakeWithReferences()
            // first whatever
            .replace(">1000<", ">MAGIC9000<")
            .replace("Patient/1100", "Patient/MAGIC8900")
            .replace("Medication/1200", "Medication/MAGIC8800")
            .replace("Condition/1310", "Condition/MAGIC8790")
            .replace("AllergyIntolerance/1320", "AllergyIntolerance/MAGIC8780")
            // second whatever
            .replace(">2000<", ">MAGIC8000<")
            .replace("Patient/2100", "Patient/MAGIC7900")
            .replace("Condition/2310", "Condition/MAGIC7790")
            .replace("AllergyIntolerance/2320", "AllergyIntolerance/MAGIC7780"));
  }

  @Test
  public void referencesAreNotReplacedWhenRawIsEnabled() {
    // TODO
    mockLookup();
    mockResults(Samples.create().fakeWithReferences());
    String actual = resources.search(forResource("patient", "123").toBuilder().raw(true).build());
    verify(repository).execute(forResource("patient", "MAGIC321").toBuilder().raw(true).build());
    verify(uis).lookup("123");
    verifyNoMoreInteractions(uis);
    assertThat(actual).isEqualTo(Samples.create().fakeWithReferences());
  }

  private void referencesAreReplacedUsingIdentifierParameter(
      String identifierParameter, String aliasValue) {
    mockLookup();
    String expected = mockWithFakeSampleAndReplaceReferences();
    String actual = resources.search(forResource(identifierParameter, "123"));
    verify(repository).execute(forResource(aliasValue, "MAGIC321"));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void referencesAreReplacedWithIdParameter() {
    referencesAreReplacedUsingIdentifierParameter("_id", "identifier");
  }

  @Test
  public void referencesAreReplacedWithIdentifierParameter() {
    referencesAreReplacedUsingIdentifierParameter("identifier", "identifier");
  }

  @Test
  public void referencesAreReplacedWithNoIdentityTypeParameter() {
    String expected = mockWithFakeSampleAndReplaceReferences();
    String actual = resources.search(forResource("patient", "123"));
    verify(repository).execute(forResource("patient", "123"));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void referencesAreReplacedWithPatientParameter() {
    referencesAreReplacedUsingIdentifierParameter("patient", "patient");
  }

  private Registration register(String resource, int systemId, String uuid) {
    return Registration.builder()
        .uuid(uuid)
        .resourceIdentity(
            ResourceIdentity.builder()
                .identifier(Integer.toString(systemId))
                .resource(resource)
                .system("CDW")
                .build())
        .build();
  }

  @Test
  public void searchFailedExceptionIsThrownWhenLookupFails() {
    thrown.expect(SearchFailed.class);
    when(uis.lookup(Mockito.any())).thenThrow(new LookupFailed("mock", "mock"));
    resources.search(forAnything());
  }

  @Test
  public void searchFailedExceptionIsThrownWhenRegisterFails() {
    thrown.expect(SearchFailed.class);
    mockResults(Samples.create().fakeWithReferences());
    when(uis.register(Mockito.any())).thenThrow(new RegistrationFailed("mock"));
    resources.search(forAnything());
  }

  @Test
  public void searchFailedExceptionIsThrownWhenXmlCannotBeParsed() {
    thrown.expect(SearchFailed.class);
    mockResults("not valid xml");
    resources.search(forAnything());
  }

  @SneakyThrows
  private String standardizeFormatting(String xml) {
    return XmlDocuments.create().write(XmlDocuments.create().parse(xml));
  }

  @Test
  public void unknownIdentityExceptionIsThrownWhenLookupReportsUnknownIdentity() {
    thrown.expect(UnknownIdentityInSearchParameter.class);
    when(uis.lookup(Mockito.any())).thenThrow(new UnknownIdentity("mock"));
    resources.search(forAnything());
  }

  @Test
  public void unknownResourceExceptionIsThrownIfSearchResultsIndicateNoResourceFound() {
    thrown.expect(UnknownResource.class);
    mockResults(Samples.create().unknownResource());
    resources.search(forAnything());
  }
}
