package gov.va.health.api.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.health.api.sentinel.categories.Lab;
import gov.va.health.api.sentinel.crawler.ConcurrentRequestQueue;
import gov.va.health.api.sentinel.crawler.Crawler;
import gov.va.health.api.sentinel.crawler.RequestQueue;
import gov.va.health.api.sentinel.crawler.ResourceDiscovery;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Lab.class)
public class LabTest {
  LabRobots robots = LabRobots.fromSystemProperties();

  @Test
  public void crawl() {
    assertThat(robots.user1().token().accessToken()).isNotBlank();
    ResourceDiscovery discovery =
        ResourceDiscovery.builder()
            .patientId(robots.userCredentials1().icn())
            .url("https://dev-api.va.gov/services/argonaut/v0")
            .build();
    RequestQueue q = new ConcurrentRequestQueue();
    discovery.queries().forEach(q::add);
    Crawler crawler =
        Crawler.builder()
            .requestQueue(q)
            .authenticationToken(() -> robots.user1().token().accessToken())
            .build();
    crawler.crawl();
  }
}
