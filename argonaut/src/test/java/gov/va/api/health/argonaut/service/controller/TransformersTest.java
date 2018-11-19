package gov.va.api.health.argonaut.service.controller;

import static gov.va.api.health.argonaut.service.controller.Transformers.asDateString;
import static gov.va.api.health.argonaut.service.controller.Transformers.asDateTimeString;
import static gov.va.api.health.argonaut.service.controller.Transformers.convert;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertAll;
import static gov.va.api.health.argonaut.service.controller.Transformers.convertString;
import static gov.va.api.health.argonaut.service.controller.Transformers.firstPayloadItem;
import static gov.va.api.health.argonaut.service.controller.Transformers.hasPayload;
import static gov.va.api.health.argonaut.service.controller.Transformers.ifPresent;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.service.controller.Transformers.MissingPayload;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.SneakyThrows;
import org.junit.Test;

public class TransformersTest {

  @Test
  public void asDateStringReturnsNullWhenCalendarIsNull() {
    assertThat(asDateString(null)).isNull();
  }

  @SneakyThrows
  @Test
  public void asDateStringReturnsStringWhenCalendarIsNotNull() {
    XMLGregorianCalendar time =
        DatatypeFactory.newInstance().newXMLGregorianCalendar(2005, 1, 21, 7, 57, 0, 0, 0);
    assertThat(asDateString(time)).isEqualTo("2005-01-21");
  }

  @Test
  public void asDateTimeStringReturnsNullWhenCalendarIsNull() {
    assertThat(asDateTimeString(null)).isNull();
  }

  @SneakyThrows
  @Test
  public void asDateTimeStringReturnsStringWhenCalendarIsNotNull() {
    XMLGregorianCalendar time =
        DatatypeFactory.newInstance().newXMLGregorianCalendar(2005, 1, 21, 7, 57, 0, 0, 0);
    assertThat(asDateTimeString(time)).isEqualTo("2005-01-21T07:57:00.000Z");
  }

  @Test
  public void convertAllReturnsConvertedWhenListIsPopulated() {
    assertThat(convertAll(Arrays.asList(1, 2, 3), o -> "x" + o))
        .isEqualTo(Arrays.asList("x1", "x2", "x3"));
  }

  @Test
  public void convertAllReturnsNullWhenListIsEmpty() {
    assertThat(convertAll(Collections.emptyList(), o -> "x" + o)).isNull();
  }

  @Test
  public void convertAllReturnsNullWhenListIsNull() {
    assertThat(convertAll(null, o -> "x" + o)).isNull();
  }

  @Test
  public void convertReturnsConvertedWhenItemIsPopulated() {
    Function<Integer, String> tx = o -> "x" + o;
    assertThat(convert(1, tx)).isEqualTo("x1");
  }

  @Test
  public void convertReturnsNullWhenItemIsNull() {
    Function<String, String> tx = o -> "x" + o;
    assertThat(convert(null, tx)).isNull();
  }

  @Test
  public void convertStringReturnsConvertedWhenItemIsPopulated() {
    Function<String, String> tx = o -> "x" + o;
    assertThat(convertString("1", tx)).isEqualTo("x1");
  }

  @Test
  public void convertStringReturnsNullWhenItemIsEmpty() {
    Function<String, String> tx = o -> "x" + o;
    assertThat(convertString("", tx)).isNull();
  }

  @Test
  public void convertStringReturnsNullWhenItemIsNull() {
    Function<String, String> tx = o -> "x" + o;
    assertThat(convertString(null, tx)).isNull();
  }

  @Test
  public void firstPayloadItemReturnsFirstItemInListWhenPresent() {
    assertThat(firstPayloadItem(Arrays.asList("a", "b"))).isEqualTo("a");
  }

  @Test(expected = MissingPayload.class)
  public void firstPayloadItemThrowsMissingPayloadExceptionWhenEmpty() {
    firstPayloadItem(Collections.emptyList());
  }

  @Test
  public void hasPayloadReturnsPayloadWhenNotNull() {
    assertThat(hasPayload("x")).isEqualTo("x");
  }

  @Test(expected = MissingPayload.class)
  public void hasPayloadThrowsMissingPayloadExceptionWhenNull() {
    hasPayload(null);
  }

  @Test
  public void ifPresentReturnsExtractWhenObjectIsNull() {
    Function<Object, String> extract = (o) -> "x" + o;
    assertThat(ifPresent("a", extract)).isEqualTo("xa");
  }

  @Test
  public void ifPresentReturnsNullWhenObjectIsNull() {
    Function<Object, String> extract = (o) -> "x" + o;
    assertThat(ifPresent(null, extract)).isNull();
  }
}
