package gov.va.api.health.argonaut.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ParametersTest {

  @Test
  public void add() {
    MultiValueMap<String, String> expected = new LinkedMultiValueMap<>();
    expected.put("a", Arrays.asList("1", "2"));
    expected.set("b", "3");
    assertThat(Parameters.builder().add("a", "1").add("a", "2").add("b", 3).build())
        .isEqualTo(expected);
  }

  @Test
  public void addAllWithArray() {
    MultiValueMap<String, String> expected = new LinkedMultiValueMap<>();
    expected.put("a", Arrays.asList("1", "2"));
    expected.set("b", "3");
    assertThat(Parameters.builder().addAll("a", "1", "2").add("b", "3").build())
        .isEqualTo(expected);
  }

  @Test
  public void addAllWithList() {
    MultiValueMap<String, String> expected = new LinkedMultiValueMap<>();
    expected.put("a", Arrays.asList("1", "2"));
    expected.set("b", "3");
    assertThat(Parameters.builder().addAll("a", Arrays.asList("1", "2")).add("b", "3").build())
        .isEqualTo(expected);
  }

  @Test
  public void empty() {
    MultiValueMap<String, String> expected = new LinkedMultiValueMap<>();
    assertThat(Parameters.empty()).isEqualTo(expected);
  }

  @Test
  public void forIdentity() {
    MultiValueMap<String, String> expected = new LinkedMultiValueMap<>();
    expected.set("identifier", "123");
    assertThat(Parameters.forIdentity("123")).isEqualTo(expected);
  }
}
