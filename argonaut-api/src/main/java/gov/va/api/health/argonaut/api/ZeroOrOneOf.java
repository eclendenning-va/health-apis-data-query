package gov.va.api.health.argonaut.api;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ZeroOrOneOfValidator.class)
@Documented
public @interface ZeroOrOneOf {
  /** Assigns message for when validation fails. */
  String message() default "Only one value may be specified";
  /** Assigns default set of constraints during validation */
  Class<?>[] groups() default {};
  /** Assigns default payload to constraints */
  Class<? extends Payload>[] payload() default {};

  /** @return The fields */
  String[] fields();
}
