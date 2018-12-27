package gov.va.health.api.sentinel;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.resources.Conformance;
import gov.va.api.health.argonaut.api.resources.Conformance.ResourceInteraction;
import gov.va.api.health.argonaut.api.resources.Conformance.ResourceInteractionCode;
import gov.va.api.health.argonaut.api.resources.Conformance.Rest;
import gov.va.api.health.argonaut.api.resources.Conformance.RestResource;
import gov.va.api.health.argonaut.api.resources.Conformance.SearchParam;
import java.util.Arrays;
import java.util.List;
import lombok.NoArgsConstructor;
import org.junit.Test;

public class ResourceDiscoveryTest {
  ResourceDiscovery resourceDiscovery = new ResourceDiscovery();
  private final ConformanceTestData data = ConformanceTestData.get();

  @Test
  public void extractRestResources() {
    assertThat(resourceDiscovery.extractRestResources(null)).isNull();
    assertThat(resourceDiscovery.extractRestResources(data.noRestListConformanceStatement))
        .isNull();
    assertThat(resourceDiscovery.extractRestResources(data.emptyRestListConformanceStatement))
        .isNull();
    assertThat(
            resourceDiscovery.extractRestResources(
                data.singleResourceDoubleRestListConformanceStatement))
        .isEqualTo(Arrays.asList(data.validRestResource, data.validRestResource));
    assertThat(
            resourceDiscovery.extractRestResources(
                data.doubleResourceSingleRestListConformanceStatement))
        .isEqualTo(Arrays.asList(data.validRestResource, data.validRestResource));
  }

  @Test
  public void patientSearchableResources() {
    assertThat(resourceDiscovery.patientSearchableResources(emptyList())).isEmpty();
    assertThat(resourceDiscovery.patientSearchableResources(emptyList())).isEmpty();
    assertThat(resourceDiscovery.patientSearchableResources(data.noSearchableResources)).isEmpty();
    assertThat(resourceDiscovery.patientSearchableResources(data.mixedSearchableResources))
        .isEqualTo(singletonList(data.patientSearchableRestResource.type()));
    assertThat(resourceDiscovery.patientSearchableResources(data.bothSearchableResources))
        .isEqualTo(
            Arrays.asList(
                data.patientSearchableRestResource.type(),
                data.patientSearchableRestResource.type()));
  }

  @Test
  public void patientQueries() {
    assertThat(resourceDiscovery.patientQueries(emptyList())).isEmpty();
    assertThat(
            resourceDiscovery.patientSearchableResources(
                singletonList(data.emptySearchParamRestResource)))
        .isEmpty();
    assertThat(
            resourceDiscovery
                .patientQueries(singletonList(data.readableAndSearchablePatient))
                .size())
        .isEqualTo(2);
    assertThat(
            resourceDiscovery
                .patientQueries(singletonList(data.readableAndNotSearchablePatient))
                .size())
        .isEqualTo(1);
    assertThat(
            resourceDiscovery
                .patientQueries(singletonList(data.notReadableAndSearchablePatient))
                .size())
        .isEqualTo(1);
    assertThat(
            resourceDiscovery.patientQueries(
                singletonList(data.notReadableAndNotSearchablePatient)))
        .isEmpty();
  }

  @NoArgsConstructor(staticName = "get")
  public static class ConformanceTestData {
    List<ResourceInteraction> readable =
        singletonList(ResourceInteraction.builder().code(ResourceInteractionCode.read).build());
    List<ResourceInteraction> notReadable = singletonList(ResourceInteraction.builder().build());
    List<SearchParam> searchable = singletonList(SearchParam.builder().name("_id").build());
    List<SearchParam> notSearchable = singletonList(SearchParam.builder().build());
    RestResource readableAndSearchablePatient =
        RestResource.builder()
            .type("Patient")
            .interaction(readable)
            .searchParam(searchable)
            .build();
    RestResource readableAndNotSearchablePatient =
        RestResource.builder()
            .type("Patient")
            .interaction(readable)
            .searchParam(notSearchable)
            .build();
    RestResource notReadableAndSearchablePatient =
        RestResource.builder()
            .type("Patient")
            .interaction(notReadable)
            .searchParam(searchable)
            .build();
    RestResource notReadableAndNotSearchablePatient =
        RestResource.builder()
            .type("Patient")
            .interaction(notReadable)
            .searchParam(notSearchable)
            .build();
    RestResource patientSearchableRestResource =
        RestResource.builder()
            .type("searchable by patient")
            .searchParam(singletonList(SearchParam.builder().name("patient").build()))
            .build();
    RestResource identifierSearchableRestResource =
        RestResource.builder()
            .type("not searchable by patient")
            .searchParam(singletonList(SearchParam.builder().name("_id").build()))
            .build();
    RestResource emptySearchParamRestResource =
        RestResource.builder().type("empty search param").searchParam(null).build();
    List<RestResource> noSearchableResources =
        Arrays.asList(identifierSearchableRestResource, identifierSearchableRestResource);
    List<RestResource> mixedSearchableResources =
        Arrays.asList(identifierSearchableRestResource, patientSearchableRestResource);
    List<RestResource> bothSearchableResources =
        Arrays.asList(patientSearchableRestResource, patientSearchableRestResource);
    RestResource validRestResource = RestResource.builder().type("Patient").build();
    Rest singleResourceSingleRest =
        Rest.builder().resource(singletonList(validRestResource)).build();
    Rest doubleResourceSingleRest =
        Rest.builder().resource(Arrays.asList(validRestResource, validRestResource)).build();
    List<Rest> singleResourcePerRestList =
        Arrays.asList(singleResourceSingleRest, singleResourceSingleRest);
    List<Rest> doubleResourceSingleRestList = singletonList(doubleResourceSingleRest);
    Conformance noRestListConformanceStatement = Conformance.builder().build();
    Conformance emptyRestListConformanceStatement = Conformance.builder().rest(emptyList()).build();
    Conformance singleResourceDoubleRestListConformanceStatement =
        Conformance.builder().rest(singleResourcePerRestList).build();
    Conformance doubleResourceSingleRestListConformanceStatement =
        Conformance.builder().rest(doubleResourceSingleRestList).build();
  }
}
