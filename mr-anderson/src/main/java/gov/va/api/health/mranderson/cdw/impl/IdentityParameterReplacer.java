package gov.va.api.health.mranderson.cdw.impl;

import gov.va.api.health.ids.api.IdentityService;
import gov.va.api.health.ids.api.ResourceIdentity;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources.MissingSearchParameters;
import gov.va.api.health.mranderson.util.Parameters;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;

/** Leverages the Identity Service to replace _identifier_ type parameters in Queries. */
class IdentityParameterReplacer {

  private final IdentityService identityService;
  private final Set<String> identityKeys;
  private final Map<String, String> aliases;

  @Builder
  private IdentityParameterReplacer(
      IdentityService identityService,
      @Singular Set<String> identityKeys,
      @Singular Map<String, String> aliases) {
    this.identityService = identityService;
    this.identityKeys = identityKeys;
    this.aliases = aliases;
  }

  private String aliasOf(String key) {
    return aliases.getOrDefault(key, key);
  }

  private boolean isIdentity(String key) {
    return identityKeys.contains(key);
  }

  private String lookupCdwId(String uuid) {
    List<ResourceIdentity> identities = identityService.lookup(uuid);
    return identities
        .stream()
        .filter(ResourceIdentities::isCdw)
        .map(ResourceIdentity::identifier)
        .findFirst()
        .orElse(uuid);
  }

  /**
   * Return a new Query that matches the given original query except identity type parameters will
   * have been replaced with CDW identity values returned for the Identity Service.
   */
  Query rebuildWithCdwIdentities(Query originalQuery) {
    Parameters parameters = Parameters.builder();
    for (Entry<String, List<String>> entry : originalQuery.parameters().entrySet()) {
      if (isIdentity(entry.getKey())) {
        for (String value : entry.getValue()) {
          if (StringUtils.isBlank(value)) {
            throw new MissingSearchParameters(originalQuery);
          }
          parameters.add(aliasOf(entry.getKey()), lookupCdwId(value));
        }
      } else {
        parameters.addAll(aliasOf(entry.getKey()), entry.getValue());
      }
    }
    return originalQuery.toBuilder().parameters(parameters.build()).build();
  }
}
