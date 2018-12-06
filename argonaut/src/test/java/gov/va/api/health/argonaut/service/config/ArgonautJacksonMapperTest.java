package gov.va.api.health.argonaut.service.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.SneakyThrows;
import org.junit.Test;

public class ArgonautJacksonMapperTest {

  private Reference reference(String path) {
    return Reference.builder().display("display").reference(path).id("id").build();
  }

  @Test
  @SneakyThrows
  public void referencesAreQualified() {
    FugaziReferencemajig input =
        FugaziReferencemajig.builder()
            .whocares("noone")
            .me(true)
            .ref(reference("r1"))
            .thing(reference(null))
            .thing(reference(""))
            .thing(reference("http://qualified.is.not/touched"))
            .thing(reference("no/slash"))
            .thing(reference("/cool/a/slash"))
            .inner(FugaziReferencemajig.builder().ref(reference("me/too")).build())
            .build();

    FugaziReferencemajig expected =
        FugaziReferencemajig.builder()
            .whocares("noone")
            .me(true)
            .ref(reference("https://example.com/api/r1"))
            .thing(reference(null))
            .thing(reference(null))
            .thing(reference("http://qualified.is.not/touched"))
            .thing(reference("https://example.com/api/no/slash"))
            .thing(reference("https://example.com/api/cool/a/slash"))
            .inner(
                FugaziReferencemajig.builder()
                    .ref(reference("https://example.com/api/me/too"))
                    .build())
            .build();

    String qualifiedJson =
        new ArgonautJacksonMapper("https://example.com", "api")
            .objectMapper()
            .writeValueAsString(input);

    FugaziReferencemajig actual =
        JacksonConfig.createMapper().readValue(qualifiedJson, FugaziReferencemajig.class);

    assertThat(actual).isEqualTo(expected);
  }

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor
  @JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    isGetterVisibility = Visibility.NONE
  )
  public static class FugaziReferencemajig {
    Reference ref;
    @Singular List<Reference> things;
    FugaziReferencemajig inner;
    String whocares;
    Boolean me;
  }
}
