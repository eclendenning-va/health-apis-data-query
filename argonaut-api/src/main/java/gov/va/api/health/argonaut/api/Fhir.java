package gov.va.api.health.argonaut.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Fhir {
  public static final String ID = "[A-Za-z0-9\\-\\.]{1,64}";
  public static final String CODE = "[^\\s]+([\\s]+[^\\s]+)*";
  public static final String URI = ".+";
  public static final String BASE64 = "[^-A-Za-z0-9+/=]|=[^=]|={3,}$";
  public static final String DATE = "-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1]))?)?";
  public static final String DATETIME =
      "-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])"
          + "(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?(Z|(\\+|-)"
          + "((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?";
  public static final String TIME = "([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?";
  public static final String INSTANT =
      "^[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])"
          + "(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]"
          + "(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))))$";

  public static final String OID = "urn:oid:[0-2](\\.[1-9]\\d*)+";
  public static final String XHTML = "<.+>";
}
