package gov.va.api.health.sentinel;

import static gov.va.api.health.sentinel.ResourceVerifier.test;

import gov.va.api.health.argonaut.api.resources.MedicationStatement;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.sentinel.categories.LabArgo;
import gov.va.api.health.sentinel.categories.LabCargo;
import gov.va.api.health.sentinel.categories.Local;
import gov.va.api.health.sentinel.categories.ProdArgo;
import gov.va.api.health.sentinel.categories.ProdCargo;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class MedicationStatementIT {

  ResourceVerifier verifier = ResourceVerifier.get();

  @Test
  @Category({Local.class, LabCargo.class, ProdCargo.class})
  public void advanced() {
    verifier.verifyAll(
        test(
            200,
            MedicationStatement.Bundle.class,
            "MedicationStatement?_id={id}",
            verifier.ids().medicationStatement()),
        test(404, OperationOutcome.class, "MedicationStatement?_id={id}", verifier.ids().unknown()),
        test(
            200,
            MedicationStatement.Bundle.class,
            "MedicationStatement?identifier={id}",
            verifier.ids().medicationStatement()));
  }

  @Test
  @Category({Local.class, LabArgo.class, LabCargo.class, ProdArgo.class, ProdCargo.class})
  public void basic() {
    verifier.verifyAll(
        test(
            200,
            MedicationStatement.class,
            "MedicationStatement/{id}",
            verifier.ids().medicationStatement()),
        test(404, OperationOutcome.class, "MedicationStatement/{id}", verifier.ids().unknown()),
        test(
            200,
            MedicationStatement.Bundle.class,
            "MedicationStatement?patient={patient}",
            verifier.ids().patient()));
  }

  @Test
  @Category({LabArgo.class, LabCargo.class, ProdArgo.class, ProdCargo.class})
  public void searchNotMe() {
    verifier.verifyAll(
        test(
            403,
            OperationOutcome.class,
            "MedicationStatement?patient={patient}",
            verifier.ids().unknown()));
  }
}
