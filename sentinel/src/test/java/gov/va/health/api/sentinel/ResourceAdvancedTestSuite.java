package gov.va.health.api.sentinel;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@IncludeCategory({Local.class, Qa.class})
@ExcludeCategory(Prod.class)
@SuiteClasses({
    ConditionAdvancedIT.class,
    DiagnosticReportAdvancedIT.class,
    ObservationAdvancedIT.class,
    PatientAdvancedIT.class,
    ProcedureAdvancedIT.class
})
public class ResourceAdvancedTestSuite {
}
