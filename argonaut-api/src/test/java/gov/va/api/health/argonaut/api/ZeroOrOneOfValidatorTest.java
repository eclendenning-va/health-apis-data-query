package gov.va.api.health.argonaut.api;
import lombok.Data;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ZeroOrOneOfValidatorTest {
private static Validator validator;
    @BeforeClass
    public static void setUpClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validObjectTest() {
        final validObject valid = new validObject();
        final Set<ConstraintViolation<validObject>> violations = validator.validate(valid);
        assertThat(violations).isEmpty();
    }
    @Test
    public void invalidObjectTest(){
        final invalidObject invalid = new invalidObject();
        final Set<ConstraintViolation<invalidObject>> violations = validator.validate(invalid);
        assertThat(violations).isNotEmpty();
    }
}

@RelatedFields({
  @ZeroOrOneOf(fields = {"a", "b", "c"}),
  @ZeroOrOneOf(fields = {"a", "b"}),
  @ZeroOrOneOf(fields = {"a", "c"}),
  @ZeroOrOneOf(fields = {"c", "b"}),
})
@Data
class validObject {
    String a ="Valid";
    Boolean b;
    Integer c;
}
@ZeroOrOneOf(fields ={"a","b"})
@Data
class invalidObject{
    String a ="Invalid";
    Boolean b =false;
    Integer c = 1;
}
