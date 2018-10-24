package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Fhir {
  public static final String ID = "[A-Za-z0-9\\-\\.]{1,64}";
  public static final String CODE = "[^\\s]+([\\s]+[^\\s]+)*";
  public static final String URI = ".+";
  public static final String BASE64 = "[^-A-Za-z0-9+/=]|=[^=]|={3,}$";
  public static final String DATE = "-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1]))?)?";
  public static final String DATETIME =
      "-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?";
  public static final String TIME = "([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?";
  public static final String INSTANT =
      "^[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))))$";

  public static final String OID = "urn:oid:[0-2](\\.[1-9]\\d*)+";

  public interface Element {
    String id();

    List<Extension> extension();
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Address implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    AddressUse use;
    AddressType type;
    String text;
    List<String> line;
    String city;
    String district;
    String state;
    String postalCode;
    String country;
    @Valid Period period;

    public enum AddressUse {
      home,
      work,
      temp,
      old
    }

    private enum AddressType {
      postal,
      physical,
      both
    }
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Attachment implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    @Pattern(regexp = CODE)
    String contentType;

    @Pattern(regexp = CODE)
    String language;

    @Pattern(regexp = BASE64)
    String data;

    @Pattern(regexp = URI)
    String url;

    @Min(0)
    Integer size;

    @Pattern(regexp = BASE64)
    String hash;

    String title;

    @Pattern(regexp = DATETIME)
    String creation;
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class CodeableConcept implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    @Valid List<Coding> coding;
    String text;
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Coding {
    @Pattern(regexp = URI)
    String system;

    String version;

    @Pattern(regexp = CODE)
    String code;

    String display;
    Boolean userSelected;
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class ContactPoint implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;
    ContactPointSystem system;
    String value;

    @Min(1)
    Integer rank;

    @Valid Period period;

    enum ContactPointSystem {
      phone,
      fax,
      email,
      pager,
      other
    }
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Extension implements Element {

    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    @Pattern(regexp = URI)
    String url;

    Integer valueInteger;
    Double valueDecimal;

    @Pattern(regexp = DATETIME)
    String valueDateTime;

    @Pattern(regexp = DATE)
    String valueDate;

    @Pattern(regexp = INSTANT)
    String valueInstant;

    String valueString;

    @Pattern(regexp = URI)
    String valueUri;

    Boolean valueBoolean;

    @Pattern(regexp = CODE)
    String valueCode;

    @Pattern(regexp = BASE64)
    String valueBase64Binary;

    @Valid Coding valueCoding;
    @Valid CodeableConcept valueCodeableConcept;
    @Valid Attachment valueAttachment;
    @Valid Identifier valueIdentifier;
    @Valid Quantity valueQuantity;
    @Valid Range valueRange;
    @Valid Period valuePeriod;
    @Valid Ratio valueRatio;
    @Valid HumanName valueHumanName;
    @Valid Address valueAddress;
    @Valid ContactPoint valueContactPoint;
    @Valid Reference valueReference;
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class HumanName implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    NameUse use;
    String text;
    List<String> family;
    List<String> given;
    List<String> prefix;
    List<String> suffix;
    @Valid Period period;

    public enum NameUse {
      usual,
      official,
      temp,
      nickname,
      anonymous,
      old,
      maiden
    }
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Period implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    @Pattern(regexp = DATETIME)
    String start;

    @Pattern(regexp = DATETIME)
    String end;
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Quantity implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    Double value;

    @Pattern(regexp = "(<|<=|>=|>)")
    String comparator;

    String unit;

    @Pattern(regexp = CODE)
    String code;
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Range implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    @Valid SimpleQuantity low;
    @Valid SimpleQuantity high;
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Ratio implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    @Valid Quantity numerator;
    @Valid Quantity denominator;
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Reference implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    String reference;
    String display;
  }

  @Data
  @Builder
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class SimpleQuantity implements Element {
    @Pattern(regexp = ID)
    String id;

    @Valid List<Extension> extension;

    Double value;

    String unit;

    @Pattern(regexp = CODE)
    String code;
  }
}
