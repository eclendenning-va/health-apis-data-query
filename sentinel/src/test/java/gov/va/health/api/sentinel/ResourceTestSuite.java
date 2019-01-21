package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.resources.Observation;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@IncludeCategory({Local.class, Qa.class, Prod.class})
@SuiteClasses({
    AllergyIntoleranceIT.class,
    AppointmentIT.class,
    ConditionIT.class,
    DiagnosticReportIT.class,
    EncounterIT.class,
    ImmunizationIT.class,
    LocationIT.class,
    MedicationIT.class,
    MedicationOrderIT.class,
    MedicationStatementIT.class,
    Observation.class,
    OrganizationIT.class,
    PatientIT.class,
    PractitionerIT.class,
    ProcedureIT.class
})
public class ResourceTestSuite {

}
