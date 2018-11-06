package gov.va.api.health.argonaut.api;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ZeroOrOneOfValidator.class)
@Documented
public @interface ZeroOrOneOf {
    String message() default "Only one value may be specified";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** @return The fields */
    String[] fields();
}
