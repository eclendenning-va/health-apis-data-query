package gov.va.api.health.dataquery.tests;

import static gov.va.api.health.dataquery.tests.ResourceVerifier.test;

import gov.va.api.health.dstu2.api.resources.OperationOutcome;
import gov.va.api.health.dstu2.api.resources.Practitioner;
import gov.va.api.health.sentinel.categories.Local;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class PractitionerIT {
  ResourceVerifier verifier = ResourceVerifier.get();

  @Category({Local.class
    // , ProdDataQueryClinician.class
  })
  @Test
  public void advanced() {
    verifier.verifyAll(
        test(
            200, Practitioner.Bundle.class, "Practitioner?_id={id}", verifier.ids().practitioner()),
        test(404, OperationOutcome.class, "Practitioner?_id={id}", verifier.ids().unknown()),
        test(
            200,
            Practitioner.Bundle.class,
            "Practitioner?identifier={id}",
            verifier.ids().practitioner()));
  }

  @Category({Local.class
    // , ProdDataQueryPatient.class, ProdDataQueryClinician.class
  })
  @Test
  public void basic() {
    verifier.verifyAll(
        test(200, Practitioner.class, "Practitioner/{id}", verifier.ids().practitioner()),
        test(404, OperationOutcome.class, "Practitioner/{id}", verifier.ids().unknown()));
  }
}
