package gov.va.health.api.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.health.api.sentinel.categories.Lab;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Lab.class)
public class LabTest {
  LabRobots robots = LabRobots.fromSystemProperties();

  @Test
  public void login() {
    assertThat(robots.user1().token().accessToken()).isNotBlank();
  }
}
