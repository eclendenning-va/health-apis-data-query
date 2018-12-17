import static org.assertj.core.api.Assertions.assertThat;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class DeleteMe {

  @Test
  public void exampleMyWay() {
    assertThat(withTwoExpressions(new Thing(""))).isNull();
    assertThat(withTwoExpressions(new Thing(" "))).isNull();
    assertThat(withTwoExpressions(new Thing("x"))).isEqualTo("x");
    assertThat(withTwoExpressions(new Thing(null))).isNull();
    assertThat(withTwoExpressions(null)).isNull();
  }

  @Test
  public void exampleWithBraces() {
    assertThat(withBraces(new Thing(""))).isNull();
    assertThat(withBraces(new Thing(" "))).isNull();
    assertThat(withBraces(new Thing("x"))).isEqualTo("x");
    assertThat(withBraces(new Thing(null))).isNull();
    assertThat(withBraces(null)).isNull();
  }

  @Test
  public void exampleWithoutBraces() {
    assertThat(withoutBraces(new Thing(null))).isNull();
    //    assertThat(withoutBraces(new Thing(""))).isNull();
    //    assertThat(withoutBraces(new Thing(" "))).isNull();
    //    assertThat(withoutBraces(new Thing("x"))).isEqualTo("x");
    //    assertThat(withoutBraces(null)).isNull();
  }

  public String withBraces(Thing t) {
    if (t == null || t.value == null && t.value.isEmpty()) {
      return null;
    }
    return t.value;
  }

  public String withTwoExpressions(Thing t) {
    if (t == null) {
      return null;
    }
    if (StringUtils.isBlank(t.value)) {
      return null;
    }
    return t.value;
  }

  public String withoutBraces(Thing t) {
    if (t == null || (t.value == null && t.value.isEmpty())) {
      return null;
    }
    return t.value;
  }

  @AllArgsConstructor
  private static class Thing {
    String value;
  }
}
