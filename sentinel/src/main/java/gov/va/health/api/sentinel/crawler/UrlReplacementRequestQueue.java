package gov.va.health.api.sentinel.crawler;

import static org.apache.commons.lang3.StringUtils.isBlank;

import lombok.Builder;

public class UrlReplacementRequestQueue implements RequestQueue {

  String baseUrl;
  String forceUrl;

  RequestQueue requestQueue;

  @Override
  public void add(String url) {
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

  @Builder
  UrlReplacementRequestQueue(String baseUrl, String forceUrl, RequestQueue requestQueue) {
    this.baseUrl = baseUrl;
    this.forceUrl = forceUrl;
    this.requestQueue = requestQueue;
    if (isBlank(forceUrl)) {
      throw new IllegalStateException("forceUrl not specified.");
    }
    if (isBlank(baseUrl)) {
      throw new IllegalStateException("baseUrl not specified.");
    }
  }
}
