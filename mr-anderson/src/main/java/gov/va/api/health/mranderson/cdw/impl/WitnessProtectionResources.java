package gov.va.api.health.mranderson.cdw.impl;

import gov.va.api.health.ids.api.IdentityService;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.ResourceRepository;
import gov.va.api.health.mranderson.cdw.Resources;
import gov.va.api.health.mranderson.util.XmlDocuments;
import gov.va.api.health.mranderson.util.XmlDocuments.ParseFailed;
import gov.va.api.health.mranderson.util.XmlDocuments.WriteFailed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class WitnessProtectionResources implements Resources {

  private final ResourceRepository repository;
  private final IdentityService identityService;

  @Override
  public String search(final Query originalQuery) {
    log.info("Search {}", originalQuery);
    validate(originalQuery);
    Query query = replacePublicIdsWithCdwIds(originalQuery);
    String originalXml = repository.execute(query);
    log.info("Executing {}", query.toQueryString());
    Document xml = parse(originalQuery, originalXml);
    XmlResponseValidator.builder().query(originalQuery).response(xml).build().validate();
    xml = replaceCdwIdsWithPublicIds(originalQuery, xml);

    return write(query, xml);
  }

  private Document parse(Query query, String xml) {
    try {
      return XmlDocuments.create().parse(xml);
    } catch (ParseFailed e) {
      log.error("Failed to parse CDW response: {} ", e.getMessage());
      throw new SearchFailed(query, e);
    }
  }

  private String write(Query query, Document xml) {
    try {
      return XmlDocuments.create().write(xml);
    } catch (WriteFailed e) {
      log.error("Failed to write XML: {}", e.getMessage());
      throw new SearchFailed(query, e);
    }
  }

  private Document replaceCdwIdsWithPublicIds(Query query, Document xml) {
    try {
      return InPlaceReferenceReplacer.builder()
          .query(query)
          .document(xml)
          .identityService(identityService)
          .build()
          .replaceReferences();
    } catch (IdentityService.RegistrationFailed e) {
      throw new SearchFailed(query, e);
    }
  }

  private Query replacePublicIdsWithCdwIds(Query originalQuery) {
    try {
      return IdentityParameterReplacer.builder()
          .identityService(identityService)
          .identityKey("patient")
          .identityKey("patient_identifier")
          .identityKey("patient_identifier:exact")
          .identityKey("identifier")
          .identityKey("identifier:exact")
          .identityKey("_id")
          .build()
          .rebuildWithCdwIdentities(originalQuery);
    } catch (IdentityService.LookupFailed e) {
      log.error("Failed to lookup CDW identities: {}", e.getMessage());
      throw new SearchFailed(originalQuery, e);
    } catch (IdentityService.UnknownIdentity e) {
      log.error("Identity is not known: {}", e.getMessage());
      throw new UnknownIdentityInSearchParameter(originalQuery, e);
    }
  }

  private void validate(Query query) {
    if (query.parameters().isEmpty()) {
      throw new MissingSearchParameters(query);
    }
  }
}
