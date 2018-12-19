package gov.va.health.api.sentinel;

import org.junit.Test;

public class NebuchadnezzarTest {

  Crawler crawler = new Crawler();

  @Test
  public void testCrawl() {
    crawler.crawl();
  }
}
