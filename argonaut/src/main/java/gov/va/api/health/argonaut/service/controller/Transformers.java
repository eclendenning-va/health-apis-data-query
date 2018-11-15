package gov.va.api.health.argonaut.service.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/** Utility methods for transforming CDW results to Argonaut. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Transformers {

  /** Throw a MissingPayload exception if the value is null. */
  public static <T> T hasPayload(T value) {
    if (value == null) {
      throw new MissingPayload();
    }
    return value;
  }

  /**
   * Return the result of the given extractor function if the given object is present. The object
   * will be passed to the apply method of the extractor function.
   *
   * <p>Consider this example:
   *
   * <pre>
   * ifPresent(patient.getGender(), gender -> Patient.Gender.valueOf(gender.value()))
   * </pre>
   *
   * This is equivalent to this standard Java code.
   *
   * <pre>
   * Gender gender = patient.getGender();
   * if (gender == null) {
   *   return null;
   * } else {
   *   return Patient.Gender.valueOf(gender.value());
   * }
   * </pre>
   */
  public static <T, R> R ifPresent(T object, Function<T, R> extract) {
    if (object == null) {
      return null;
    }
    return extract.apply(object);
  }

  /** Throw a MissingPayload exception if the list does not have at least 1 item. */
  public static <T> T firstPayloadItem(@NonNull List<T> items) {
    if (items.isEmpty()) {
      throw new MissingPayload();
    }
    return items.get(0);
  }

  /** Return null if the date is null, otherwise return ands ISO-8601 date. */
  public static String asDateString(XMLGregorianCalendar maybeDate) {
    if (maybeDate == null) {
      return null;
    }
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    return formatter.format(maybeDate.toGregorianCalendar().getTime());
  }

  /** Return null if the date is null, otherwise return an ISO-8601 date time. */
  public static String asDateTimeString(XMLGregorianCalendar maybeDateTime) {
    if (maybeDateTime == null) {
      return null;
    }
    return maybeDateTime.toString();
  }

  /**
   * Indicates the CDW payload is missing, but no errors were reported. This exception indicates
   * there is a bug in CDW, Mr. Anderson, or the Mr. Anderson client.
   */
  public static class MissingPayload extends TransformationException {
    public MissingPayload() {
      super("Payload is missing, but no errors reported by Mr. Anderson.");
    }
  }

  /** Base exception for controller errors. */
  public static class TransformationException extends RuntimeException {
    TransformationException(String message) {
      super(message);
    }
  }
}
