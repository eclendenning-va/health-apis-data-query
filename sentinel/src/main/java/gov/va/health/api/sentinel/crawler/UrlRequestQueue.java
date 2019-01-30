package gov.va.health.api.sentinel.crawler;

import lombok.Builder;

@Builder
public class UrlRequestQueue implements RequestQueue {

  String forceUrl;

  RequestQueue requestQueue;

  @Override
  public void add(String url) {
    requestQueue.add(url.replace("https://dev-api.va.gov/services/argonaut/v0/", forceUrl));
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
