package gov.va.api.health.argonaut.service.controller;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/** Provides utilities for working with MultiValueMap typically used for request parameters. */
public class Parameters {

  /** Create a new parameter map with single 'identity' entry. */
  public static MultiValueMap<String, String> forIdentity(String identity) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("identifier", identity);
    return params;
  }
}
