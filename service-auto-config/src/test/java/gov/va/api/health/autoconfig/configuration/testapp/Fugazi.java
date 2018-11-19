package gov.va.api.health.autoconfig.configuration.testapp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
class Fugazi {
  String thing;
  Instant time;
  CustomBuilder cb;
  Specified specified;

  @Data
  @Builder(builderMethodName = "makeOne", builderClassName = "CustomBuilderMaker")
  @JsonDeserialize(builder = CustomBuilder.CustomBuilderMaker.class)
  static class CustomBuilder {
    int one;

    @SuppressWarnings("DefaultAnnotationParam")
    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    static class CustomBuilderMaker {}
  }

  @Value
  @Builder
  @JsonDeserialize(builder = Specified.SpecifiedBuilder.class)
  static class Specified {
    boolean troofs;
  }
}
