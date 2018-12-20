package gov.va.health.api.sentinel;

import gov.va.health.api.sentinel.nebuchadnezzar.Crawler;
import org.junit.Ignore;
import org.junit.Test;

public class NebuchadnezzarTest {

  Crawler crawler = new Crawler();

  @Test
  @Ignore
  public void testCrawl() {
    crawler.crawl();
  }
}
