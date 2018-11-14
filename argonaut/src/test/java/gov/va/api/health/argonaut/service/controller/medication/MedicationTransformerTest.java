package gov.va.api.health.argonaut.service.controller.medication;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.CodeableConcept;
import gov.va.api.health.argonaut.api.Coding;
import gov.va.api.health.argonaut.api.Medication.Product;
import gov.va.api.health.argonaut.api.Narrative;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication;
import gov.va.dvp.cdw.xsd.model.CdwMedication101Root.CdwMedications.CdwMedication.CdwProduct;
import gov.va.api.health.argonaut.service.controller.Transformers;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import lombok.SneakyThrows;
import org.junit.Test;

public class MedicationTransformerTest {

  private XmlSampleData data = new XmlSampleData();
  private MedicationSampleData medication = new MedicationSampleData();

  @Test
  public void codeReturnsNullForNull() {
    assertThat(transformer().code(null)).isNull();
  }

  /*@Test
  public void codeCodingListTransformsToCodingList() {
    List<Coding> testCodingList = transformer().code();
    List<Coding> expectedCodingList = medication.code().coding();
    assertThat(testCodingList).isEqualTo(expectedCodingList);
  }*/

  @Test
  public void codeTransformsToCodeableConcept() {
    CodeableConcept testCdwCode = transformer().code(data.code());
    CodeableConcept expectedCdwCode = medication.medication().code();
    assertThat(testCdwCode).isEqualTo(expectedCdwCode);
  }

  @Test
  public void productReturnsNullForNull() {
    assertThat(transformer().product(null)).isNull();
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
          .id("123456789")
          .text(text())
          .code(code())
          .product(product())
          .build();
    }

    Product product() {
      return Product.builder()
          .id("1234")
          .form(form())
          .build();
    }

    Narrative text() {
      return Narrative.builder()
          .div("<div>text test</div>")
          .build();
    }
  }

  private static class XmlSampleData {
    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private XmlSampleData() {
      datatypeFactory = DatatypeFactory.newInstance();
    }

    CdwCodeableConcept form() {
      CdwCodeableConcept form = new CdwCodeableConcept();
      form.getCoding().add(coding());
      form.setText("form text test");
      return form;
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

    CdwProduct product() {
      CdwProduct product = new CdwProduct();
      product.setId("1234");
      product.setForm(form());
      return product;
    }

    CdwMedication medication() {
      CdwMedication medication = new CdwMedication();
      medication.setCdwId("123456789");
      medication.setRowNumber(new BigInteger("1"));
      medication.setText("\"<div>text test</div>\"");
      medication.setCode(code());
      medication.setProduct(product());
      return medication;
    }
  }
}
