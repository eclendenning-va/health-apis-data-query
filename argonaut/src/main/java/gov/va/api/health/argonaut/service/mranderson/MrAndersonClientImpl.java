package gov.va.api.health.argonaut.service.mranderson;

import gov.va.api.health.argonaut.api.Patient;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;

@Builder
@Value
@Slf4j
public class MrAndersonClientImpl<T> implements MrAndersonClient {

  Profile profile;
  String resource;
  Class<T> root;
  String url;
  String version;

  private static Stream<String> toKeyValueString(Map.Entry<String, List<String>> entry) {
    return entry.getValue().stream().map((value) -> entry.getKey() + '=' + value);
  }

  @Override
  public List<Patient> query(MultiValueMap<String, String> parameterMap) {
    String mrAndersonQueryString = queryString(parameterMap);
    log.info("Mr. Anderson Request is: {}", mrAndersonQueryString);
    List<Patient> bundle = new LinkedList<>();
    bundle.add(Patient.builder().id("456").build());
    return bundle;
  }

  private String queryString(MultiValueMap<String, String> parameterMap) {
    StringBuilder msg = new StringBuilder();
    msg.append(url).append("/api/v1/resources/").append(profile.toString().toLowerCase(Locale.ENGLISH)).append(resource).append(version);
    if (parameterMap != null && !parameterMap.isEmpty()) {
      String params =
          parameterMap
              .entrySet()
              .stream()
              .sorted(Comparator.comparing(e -> e.getKey()))
              .flatMap(MrAndersonClientImpl::toKeyValueString)
              .collect(Collectors.joining("&"));
      msg.append('?').append(params);
    }
    return msg.toString();
  }
}
