package gov.va.health.api.sentinel.crawler;

import static org.apache.commons.lang3.StringUtils.isBlank;

import lombok.Builder;

@Builder
public class UrlReplacementRequestQueue implements RequestQueue {

  String forceUrl;

  String baseUrl;

  RequestQueue requestQueue;

  @Override
  public void add(String url) {
    if (isBlank(baseUrl)) {
      throw new IllegalStateException("baseUrl not specified.");
    }
    if (isBlank(forceUrl)) {
      throw new IllegalStateException("forceUrl not specified.");
    }
    requestQueue.add(url.replace(baseUrl, forceUrl));
  }

  @Override
  public boolean hasNext() {
    return requestQueue.hasNext();
  }

  @Override
  public String next() {
    return requestQueue.next();
  }
}
