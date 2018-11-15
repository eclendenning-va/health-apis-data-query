package gov.va.api.health.argonaut.service.controller.medication;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.CodeableConcept;
import gov.va.api.health.argonaut.api.Coding;
import gov.va.api.health.argonaut.api.Medication.Product;
import gov.va.api.health.argonaut.api.Narrative;
import gov.va.api.health.argonaut.api.Narrative.NarrativeStatus;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication.CdwProduct;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import lombok.SneakyThrows;
import org.junit.Test;

public class MedicationTransformerTest {

  private XmlSampleData cdw = new XmlSampleData();
  private MedicationSampleData expectedMedication = new MedicationSampleData();

  @Test
  public void codeCodingListTransformsToCodingList() {
    List<Coding> testCodingList = transformer().code(cdw.code()).coding();
    List<Coding> expectedCodingList = expectedMedication.code().coding();
    assertThat(testCodingList).isEqualTo(expectedCodingList);
  }

  @Test
  public void codeReturnsNullForNull() {
    assertThat(transformer().code(null)).isNull();
  }

  @Test
  public void codeTransformsToCodeableConcept() {
    CodeableConcept testCdwCode = transformer().code(cdw.code());
    CodeableConcept expectedCdwCode = expectedMedication.medication().code();
    assertThat(testCdwCode).isEqualTo(expectedCdwCode);
  }

  @Test
  public void codingReturnsNullForNull() {
    assertThat(transformer().coding(null)).isNull();
  }

  @Test
  public void medication101TransformsToModelMedication() {
    gov.va.api.health.argonaut.api.Medication test = transformer().apply(cdw.medication());
    gov.va.api.health.argonaut.api.Medication expected = expectedMedication.medication();
    assertThat(test).isEqualTo(expected);
  }

  @Test
  public void productFormCodingTransformsToCodingList() {
    List<Coding> testCoding =
        transformer().productForm(cdw.medication().getProduct().getForm()).coding();
    List<Coding> expectedCoding = expectedMedication.medication().product().form().coding();
    assertThat(testCoding).isEqualTo(expectedCoding);
  }

  @Test
  public void productFormTransformsToCodeableConcept() {
    CodeableConcept testCodeableConcept =
        transformer().productForm(cdw.medication().getProduct().getForm());
    CodeableConcept expectedCodeableConcept = expectedMedication.medication().product().form();
    assertThat(testCodeableConcept).isEqualTo(expectedCodeableConcept);
  }

  @Test
  public void productReturnsNullForNull() {
    assertThat(transformer().product(null)).isNull();
  }

  @Test
  public void textNarrativeIsNullIfTextIsNull() {
    assertThat(transformer().text(null)).isNull();
  }

  @Test
  public void textNarrativeTransformsString() {
    Narrative expected =
        Narrative.builder().div("<div>hello</div>").status(NarrativeStatus.additional).build();
    Narrative actual = transformer().text("hello");
    assertThat(actual).isEqualTo(expected);
  }

  private MedicationTransformer transformer() {
    return new MedicationTransformer();
  }

  private static class MedicationSampleData {
    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private MedicationSampleData() {
      datatypeFactory = DatatypeFactory.newInstance();
    }

    CodeableConcept code() {
      return CodeableConcept.builder()
          .coding(
              Collections.singletonList(
                  Coding.builder()
                      .system("system test")
                      .code("code test")
                      .display("display test")
                      .build()))
          .text("code text test")
          .build();
    }

    CodeableConcept form() {
      return CodeableConcept.builder()
          .coding(
              Collections.singletonList(
                  Coding.builder()
                      .system("system test")
                      .code("code test")
                      .display("display test")
                      .build()))
          .text("form text test")
          .build();
    }

    gov.va.api.health.argonaut.api.Medication medication() {
      return gov.va.api.health.argonaut.api.Medication.builder()
          .resourceType("Medication")
          .id("123456789")
          .text(text())
          .code(code())
          .product(product())
          .build();
    }

    Product product() {
      return Product.builder().id("1234").form(form()).build();
    }

    Narrative text() {
      return Narrative.builder().div("<div>hello</div>").status(NarrativeStatus.additional).build();
    }
  }

  private static class XmlSampleData {
    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private XmlSampleData() {
      datatypeFactory = DatatypeFactory.newInstance();
    }

    CdwCodeableConcept code() {
      CdwCodeableConcept code = new CdwCodeableConcept();
      code.getCoding().add(coding());
      code.setText("code text test");
      return code;
    }

    CdwCoding coding() {
      CdwCoding coding = new CdwCoding();
      coding.setSystem("system test");
      coding.setCode("code test");
      coding.setDisplay("display test");
      return coding;
    }

    CdwCodeableConcept form() {
      CdwCodeableConcept form = new CdwCodeableConcept();
      form.getCoding().add(coding());
      form.setText("form text test");
      return form;
    }

    CdwMedication medication() {
      CdwMedication medication = new CdwMedication();
      medication.setCdwId("123456789");
      medication.setRowNumber(new BigInteger("1"));
      medication.setText("hello");
      medication.setCode(code());
      medication.setProduct(product());
      return medication;
    }

    CdwProduct product() {
      CdwProduct product = new CdwProduct();
      product.setId("1234");
      product.setForm(form());
      return product;
    }
  }
}
