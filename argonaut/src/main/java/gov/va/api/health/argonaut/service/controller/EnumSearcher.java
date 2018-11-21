package gov.va.api.health.argonaut.service.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@Builder(toBuilder = true)
public class EnumSearcher<T extends Enum<T>> {

  Class<T> anEnum;

  /** Start a builder chain to query for a given type. */
  public static <T extends Enum<T>> EnumSearcher.EnumSearcherBuilder<T> of(Class<T> type) {
    return EnumSearcher.<T>builder().anEnum(type);
  }

  /**
   * Provided a String, search the enum by name, and by JsonProperty annotation for the matching
   * enum.
   */
  @SneakyThrows
  public T find(String s) {
    return Arrays.stream(anEnum.getEnumConstants())
        .filter(e -> isMe(e, s))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "No enum constant " + anEnum.getCanonicalName() + "." + s));
  }

  @SneakyThrows
  private boolean isMe(T e, String s) {
    if (e.name().equals(s)) {
      return true;
    }
    JsonProperty jp = e.getClass().getField(e.name()).getAnnotation(JsonProperty.class);
    if (jp != null) {
      return jp.value().equals(s);
    }
    return false;
  }
}
