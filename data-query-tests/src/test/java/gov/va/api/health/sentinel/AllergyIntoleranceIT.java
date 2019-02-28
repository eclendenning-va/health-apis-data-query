package gov.va.api.health.sentinel;

import static gov.va.api.health.sentinel.ResourceVerifier.test;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.sentinel.categories.LabArgo;
import gov.va.api.health.sentinel.categories.LabCargo;
import gov.va.api.health.sentinel.categories.Local;
import gov.va.api.health.sentinel.categories.ProdArgo;
import gov.va.api.health.sentinel.categories.ProdCargo;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class AllergyIntoleranceIT {

  ResourceVerifier verifier = ResourceVerifier.get();

  @Test
  @Category({Local.class, LabCargo.class, ProdCargo.class})
  public void advanced() {
    verifier.verifyAll(
        test(
            200,
            AllergyIntolerance.Bundle.class,
            "AllergyIntolerance?_id={id}",
            verifier.ids().allergyIntolerance()),
        test(404, OperationOutcome.class, "AllergyIntolerance?_id={id}", verifier.ids().unknown()),
        test(
            200,
            AllergyIntolerance.Bundle.class,
            "AllergyIntolerance?identifier={id}",
            verifier.ids().allergyIntolerance()));
  }

  @Test
  @Category({Local.class, LabArgo.class, LabCargo.class, ProdArgo.class, ProdCargo.class})
  public void basic() {
    verifier.verifyAll(
        test(
            200,
            AllergyIntolerance.class,
            "AllergyIntolerance/{id}",
            verifier.ids().allergyIntolerance()),
        test(404, OperationOutcome.class, "AllergyIntolerance/{id}", verifier.ids().unknown()),
        test(
            200,
            AllergyIntolerance.Bundle.class,
            "AllergyIntolerance?patient={patient}",
            verifier.ids().patient()));
  }

  @Test
  @Category({LabArgo.class, LabCargo.class, ProdArgo.class, ProdCargo.class})
  public void searchNotMe() {
    verifier.verifyAll(
        test(
            403,
            OperationOutcome.class,
            "AllergyIntolerance?patient={patient}",
            verifier.ids().unknown()));
  }
}
