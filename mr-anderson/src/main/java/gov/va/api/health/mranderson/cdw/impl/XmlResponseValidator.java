package gov.va.api.health.mranderson.cdw.impl;

import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources.MissingSearchParameters;
import gov.va.api.health.mranderson.cdw.Resources.SearchFailed;
import gov.va.api.health.mranderson.cdw.Resources.UnknownResource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import lombok.Builder;
import org.w3c.dom.Document;

/**
 * Process the CDW prc_Entity_Return XML responses for errors, throwing {@link
 * gov.va.api.health.mranderson.cdw.Resources} exceptions as necessary.
 */
class XmlResponseValidator {
  private final Query query;
  private final Document response;

  @Builder
  private XmlResponseValidator(Query query, Document response) {
    this.query = query;
    this.response = response;
  }

  private String extractErrorNumberValueOrDie() {
    XPath xpath = XPathFactory.newInstance().newXPath();
    try {
      return xpath.compile("/root/errorNumber").evaluate(response);
    } catch (XPathExpressionException e) {
      throw new SearchFailed(query, "Do not understand XML response. Error Number: <missing>");
    }
  }

  private int asIntegerOrDie(String errorNumberValue) {
    try {
      return Integer.parseInt(errorNumberValue);
    } catch (NumberFormatException e) {
      throw new SearchFailed(
          query, "Do not understand XML response. Error Number: " + errorNumberValue);
    }
  }

  void validate() {
    int errorNumber = asIntegerOrDie(extractErrorNumberValueOrDie());
    if (errorNumber == ErrorNumbers.UNKNOWN_RESOURCE) {
      throw new UnknownResource(query);
    }
    if (errorNumber == ErrorNumbers.BAD_PARAMETERS) {
      throw new MissingSearchParameters(query);
    }
    if (errorNumber != ErrorNumbers.NO_ERROR) {
      throw new SearchFailed(query, "Unknown response error: Error Number: " + errorNumber);
    }
  }

  private static class ErrorNumbers {
    static int NO_ERROR = 0;
    static int UNKNOWN_RESOURCE = -8;
    static int BAD_PARAMETERS = -999;
  }
}
