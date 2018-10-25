package gov.va.api.health.mranderson.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ChecksTest {

  @Test
  public void notNullReturnsNonNullValue() {
    assertThat(Checks.notNull("something")).isEqualTo("something");
  }

  @Test(expected = IllegalStateException.class)
  public void notNullThrowsExceptionForNullValue() {
    Checks.notNull(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void argumentMatchesThrowsExceptionForNullValue() {
    Checks.argumentMatches(null, ".*");
  }

  @Test(expected = IllegalArgumentException.class)
  public void argumentMatchesThrowsExceptionForNoMatch() {
    Checks.argumentMatches("no", "yes");
  }

  @Test
  public void argumentMatchesReturnsMatchingValue() {
    assertThat(Checks.argumentMatches("yes", "yes")).isEqualTo("yes");
  }
}
