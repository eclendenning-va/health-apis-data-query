package gov.va.api.health.argonaut.api;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class ZeroOrOneOfValidator implements ConstraintValidator<ZeroOrOneOf, Object>
{
  private String [] fieldNames;
  private int notNullCount = 0;
  @Override
  public void initialize(final ZeroOrOneOf constraintAnnotation)
  {
    fieldNames = constraintAnnotation.fields();
  }

  @Override
  public boolean isValid(final Object value, final ConstraintValidatorContext context)
  {
      for (String fieldName : fieldNames  ) {
         if (valueOf(value,fieldName) != null){
             notNullCount ++;
         }
      }
      if (notNullCount > 1){
          return false;
      }
    return true;
  }
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

    @SneakyThrows
    private Object valueOf(Object o, String field) {
        return findGetter(o.getClass(), field).invoke(o);
    }
}