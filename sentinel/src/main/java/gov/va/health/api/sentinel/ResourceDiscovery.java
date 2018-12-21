package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.resources.Conformance;
import gov.va.api.health.argonaut.api.resources.Conformance.ResourceInteractionCode;
import gov.va.api.health.argonaut.api.resources.Conformance.RestResource;
import io.restassured.RestAssured;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceDiscovery {
  private String url = "https://localhost:8090/api/";
  private String patientId = "185601V825290";

  List<String> builtQueries() {

    List<String> queries = new ArrayList<>();

    Conformance conformanceStatement =
        RestAssured.given().get(url + "metadata").as(Conformance.class);
    List<RestResource> restResources = extractRestResources(conformanceStatement);

    if (restResources != null) {

      queries = patientSearchableResources(restResources);
      queries.replaceAll(r -> url + r + "?patient=" + patientId);

      queries.addAll(patientQueries(restResources));
    }
    return queries;
  }

  List<RestResource> extractRestResources(Conformance conformanceStatement) {
    if (conformanceStatement == null) {
      return null;
    }
    if (conformanceStatement.rest() == null || conformanceStatement.rest().isEmpty()) {
      return null;
    }
    //dont get(0)
    return conformanceStatement.rest().get(0).resource();
  }

  private List<String> patientQueries(List<RestResource> restResources) {
    List<String> patientQueries = new ArrayList<>();
    Boolean isReadable;
    Boolean isSearchable;

    Optional<RestResource> patientMetadata =
        restResources.stream().filter(n -> "Patient".equals(n.type())).findFirst();

    if (patientMetadata.isPresent()) {
      isReadable =
          patientMetadata
              .get()
              .interaction()
              .stream()
              .anyMatch(p -> ResourceInteractionCode.read.equals(p.code()));
      isSearchable =
          nullableListToStream(patientMetadata.get().searchParam())
              .anyMatch(s -> "_id".equals(s.name()));

      if (isReadable) {
        patientQueries.add(url + "Patient/" + patientId);
      }

      if (isSearchable) {
        patientQueries.add((url + "Patient?_id=" + patientId));
      }
    }
    return patientQueries;
  }

  private List<String> patientSearchableResources(List<RestResource> restResources) {
    return restResources
        .stream()
        .filter(this::isSearchableByPatient)
        .map(p -> p.type())
        .collect(Collectors.toList());
  }

  private boolean isSearchableByPatient(RestResource supportedResources) {
    if (supportedResources == null || supportedResources.searchParam() == null) {
      return false;
    }
    return supportedResources.searchParam().stream().anyMatch(o -> o.name().equals("patient"));
  }

  static <T> Stream<T> nullableListToStream(List<T> list) {
    return list == null ? Stream.empty() : list.stream();
  }
}
