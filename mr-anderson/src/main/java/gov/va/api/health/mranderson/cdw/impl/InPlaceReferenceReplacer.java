package gov.va.api.health.mranderson.cdw.impl;

import gov.va.api.health.ids.api.IdentityService;
import gov.va.api.health.ids.api.Registration;
import gov.va.api.health.ids.api.ResourceIdentity;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources.SearchFailed;
import gov.va.api.health.mranderson.cdw.impl.ResourceIdentities.ReferencePair;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

@Slf4j
class InPlaceReferenceReplacer {
  private final Query query;
  private final Document document;
  private final IdentityService identityService;

  private final List<ReferenceNodeHandler> handlers =
      Arrays.asList(new NormalReferenceNodeHandler(), new CdwIdReferenceNodeHandler());

  @Builder
  private InPlaceReferenceReplacer(
      Query query, Document document, IdentityService identityService) {
    this.query = query;
    this.document = document;
    this.identityService = identityService;
  }

  Document replaceReferences() {
    MultiValueMap<String, Node> referenceNodes = collectReferenceNodes();
    if (referenceNodes.isEmpty()) {
      return document;
    }
    List<Registration> registrations = registerIds(referenceNodes);
    registrations.forEach(replaceReference(referenceNodes));
    return document;
  }

  private Consumer<? super Registration> replaceReference(
      MultiValueMap<String, Node> referenceNodes) {
    return (registration) -> {
      ReferencePair reference = ResourceIdentities.referencesOf(registration);
      List<Node> nodes = referenceNodes.get(reference.cdw());
      if (nodes == null) {
        log.warn(
            "Ignoring registration reference {}. There are no associated nodes for {}",
            reference,
            registration);
        return;
      }

      nodes.forEach(node -> handlerFor(node).updateReference(node, reference.universal()));
    };
  }

  private List<Registration> registerIds(MultiValueMap<String, Node> referenceNodes) {
    List<ResourceIdentity> identities =
        referenceNodes
            .keySet()
            .stream()
            .map(ResourceIdentities::referenceToResourceIdentity)
            .collect(Collectors.toList());
    return identityService.register(identities);
  }

  private MultiValueMap<String, Node> collectReferenceNodes() {
    if (!(document instanceof DocumentTraversal)) {
      throw new CannotTraverseDocument();
    }
    MultiValueMap<String, Node> referenceNodes = new LinkedMultiValueMap<>();
    DocumentTraversal traversal = (DocumentTraversal) document;
    NodeIterator iterator =
        traversal.createNodeIterator(document, NodeFilter.SHOW_ELEMENT, filter(), false);
    Node node;
    while ((node = iterator.nextNode()) != null) {
      String reference = handlerFor(node).referenceOf(node);
      referenceNodes.add(reference, node);
    }
    return referenceNodes;
  }

  private ReferenceNodeHandler handlerFor(Node node) {
    return handlers
        .stream()
        .filter(t -> t.isReference(node))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "Node appears to no longer have a handler:" + node.getNodeName()));
  }

  private NodeFilter filter() {
    return node ->
        handlers.stream().anyMatch(t -> t.isReference(node))
            ? NodeFilter.FILTER_ACCEPT
            : NodeFilter.FILTER_SKIP;
  }

  private interface ReferenceNodeHandler {
    boolean isReference(Node node);

    String referenceOf(Node node);

    void updateReference(Node node, String reference);
  }

  static class CannotTraverseDocument extends RuntimeException {}

  private static class NormalReferenceNodeHandler implements ReferenceNodeHandler {

    @Override
    public boolean isReference(Node node) {
      return "reference".equals(node.getNodeName());
    }

    @Override
    public String referenceOf(Node node) {
      return node.getTextContent();
    }

    @Override
    public void updateReference(Node node, String reference) {
      node.setTextContent(reference);
    }
  }

  private class CdwIdReferenceNodeHandler implements ReferenceNodeHandler {

    @Override
    public boolean isReference(Node node) {
      return "cdwId".equals(node.getNodeName());
    }

    @Override
    public String referenceOf(Node node) {
      return query.resource() + "/" + node.getTextContent();
    }

    @Override
    public void updateReference(Node node, String reference) {
      int slash = reference.indexOf('/');
      if (slash < 0 || slash == reference.length() - 2) {
        throw new SearchFailed(query, "Do not understand registration value: " + reference);
      }
      node.setTextContent(reference.substring(slash + 1));
    }
  }
}
