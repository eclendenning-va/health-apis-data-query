package gov.va.api.health.argonaut.service.mranderson.client;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import org.springframework.util.MultiValueMap;

public interface MrAndersonClient {

  <T> T search(Query<T> query);

  enum Profile {
    ARGONAUT,
    DSTU2,
    STU3;

    public String toPathString() {
      return toString().toLowerCase(Locale.ENGLISH);
    }
  }

  class BadRequest extends MrAndersonServiceException {
    public BadRequest(Query<?> query) {
      super(query);
    }
  }

  class MrAndersonServiceException extends RuntimeException {
    MrAndersonServiceException(Query<?> query) {
      super(query.toQueryString());
    }

    MrAndersonServiceException(Query<?> query, Exception cause) {
      super(query.toQueryString(), cause);
    }
  }

  class NotFound extends MrAndersonServiceException {
    public NotFound(Query<?> query) {
      super(query);
    }
  }

  @Value
  @Builder(toBuilder = true)
  class Query<T> {
    Profile profile;
    String resource;
    String version;
    MultiValueMap<String, String> parameters;
    Class<T> type;

    public static <R> QueryBuilder<R> forType(Class<R> forType) {
      return Query.<R>builder().type(forType);
    }

    private static Stream<String> toKeyValueString(Map.Entry<String, List<String>> entry) {
      return entry.getValue().stream().map((value) -> entry.getKey() + '=' + value);
    }

    /**
     * Returns a CDW formatted query string, also called a "FHIRString" in the format of
     * /resource:version?key=value&key=value. The query parameters will be sorted alphabetically.
     */
    public String toQueryString() {
      StringBuilder msg = new StringBuilder();
      msg.append('/')
          .append(profile.toPathString())
          .append('/')
          .append(resource)
          .append('/')
          .append(version);
      if (parameters != null && !parameters.isEmpty()) {
        String params =
            parameters
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getKey()))
                .flatMap(Query::toKeyValueString)
                .collect(Collectors.joining("&"));
        msg.append('?').append(params);
      }
      return msg.toString();
    }
  }

  class SearchFailed extends MrAndersonServiceException {
    public SearchFailed(Query<?> query, Exception cause) {
      super(query, cause);
    }
  }
}
