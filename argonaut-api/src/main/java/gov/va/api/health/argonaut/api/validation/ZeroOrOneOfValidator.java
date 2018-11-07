package gov.va.api.health.argonaut.api.validation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

public class ZeroOrOneOfValidator implements ConstraintValidator<ZeroOrOneOf, Object> {
  private ZeroOrOneOf annotation;

  private Method findGetter(Class<?> type, String name) {
    Method getter = null;
    PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(type, name);
    if (pd != null) {
      getter = pd.getReadMethod();
    }
    if (getter == null) {
      getter = BeanUtils.findMethodWithMinimalParameters(type, name);
    }
    if (getter == null) {
      throw new IllegalArgumentException(
          "Cannot find Java bean property or fluent getter: " + type.getName() + "." + name);
    }
    return getter;
  }

  @Override
  public void initialize(final ZeroOrOneOf constraintAnnotation) {
    annotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(final Object value, final ConstraintValidatorContext context) {
    int notNullCount = 0;
    for (String fieldName : annotation.fields()) {
      if (valueOf(value, fieldName) != null) {
        notNullCount++;
      }
    }
    if (notNullCount > 1) {
      return false;
    }
    return true;
  }

  @SneakyThrows
  private Object valueOf(Object o, String field) {
    return findGetter(o.getClass(), field).invoke(o);
  }
}
