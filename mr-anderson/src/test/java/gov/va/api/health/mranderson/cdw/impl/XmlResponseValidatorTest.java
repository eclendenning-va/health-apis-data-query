package gov.va.api.health.mranderson.cdw.impl;

import gov.va.api.health.mranderson.Samples;
import gov.va.api.health.mranderson.cdw.Profile;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources.MissingSearchParameters;
import gov.va.api.health.mranderson.cdw.Resources.SearchFailed;
import gov.va.api.health.mranderson.cdw.Resources.UnknownResource;
import gov.va.api.health.mranderson.util.Parameters;
import gov.va.api.health.mranderson.util.XmlDocuments;
import org.junit.Test;
import org.w3c.dom.Document;

public class XmlResponseValidatorTest {
  private Query query() {
    return Query.builder()
        .profile(Profile.ARGONAUT)
        .resource("whatever")
        .version("1.00")
        .page(1)
        .count(2)
        .parameters(Parameters.empty())
        .build();
  }

  @Test
  public void noErrorsForErrorNumber0() {
    parse(Samples.create().patient());
    // no exceptions thrown!
  }

  @Test(expected = UnknownResource.class)
  public void unknownResourceForErrorNumberNegative8() {
    parse(Samples.create().unknownResource());
  }

  @Test(expected = MissingSearchParameters.class)
  public void unknownResourceForErrorNumberNegative999() {
    parse(Samples.create().invalidQueryParams());
  }

  @Test(expected = SearchFailed.class)
  public void unknownResourceForErrorNumberNot0() {
    parse(Samples.create().invalidQueryParams().replace("-999", "123"));
  }

  @Test(expected = SearchFailed.class)
  public void unknownResourceForErrorNumberNotNumber() {
    parse(Samples.create().invalidQueryParams().replace("-999", "kaboom"));
  }

  private void parse(String sample) {
    Document document = XmlDocuments.create().parse(sample);
    XmlResponseValidator validator =
        XmlResponseValidator.builder().query(query()).response(document).build();
    validator.validate();
    validator.hashCode();
    validator.equals(validator);
  }
}
