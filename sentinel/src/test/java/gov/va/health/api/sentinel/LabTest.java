package gov.va.health.api.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.health.api.sentinel.categories.Lab;
import gov.va.health.api.sentinel.crawler.ConcurrentRequestQueue;
import gov.va.health.api.sentinel.crawler.Crawler;
import gov.va.health.api.sentinel.crawler.FileResultsCollector;
import gov.va.health.api.sentinel.crawler.RequestQueue;
import gov.va.health.api.sentinel.crawler.ResourceDiscovery;
import java.io.File;
import java.util.concurrent.Executors;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Lab.class)
public class LabTest {
  LabRobots robots = LabRobots.fromSystemProperties();

  private void crawl(IdMeOauthRobot robot) {
    assertThat(robot.token().accessToken()).isNotBlank();
    ResourceDiscovery discovery =
        ResourceDiscovery.builder()
            .patientId(robot.token().patient())
            .url("https://dev-api.va.gov/services/argonaut/v0")
            .build();
    RequestQueue q = new ConcurrentRequestQueue();
    discovery.queries().forEach(q::add);
    Crawler crawler =
        Crawler.builder()
            .executor(Executors.newFixedThreadPool(4))
            .requestQueue(q)
            .results(
                new FileResultsCollector(new File("target/lab-crawl-" + robot.token().patient())))
            .authenticationToken(() -> robot.token().accessToken())
            .forceJargonaut(true)
            .build();
    crawler.crawl();
  }

  @Test
  public void crawlUser1() {
    crawl(robots.user1());
  }

  @Test
  public void crawlUser2() {
    crawl(robots.user2());
  }

  @Test
  public void crawlUser3() {
    crawl(robots.user3());
  }

  @Test
  public void crawlUser4() {
    crawl(robots.user4());
  }

  @Test
  public void crawlUser5() {
    crawl(robots.user5());
  }
}
