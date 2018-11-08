package gov.va.api.health.argonaut.api;

import static java.util.Collections.singletonList;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "get")
public class MedicationSampleData extends CommonSampleData {

  Batch batch() {
    return Batch.builder()
        .id("8888")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .lotNumber("10")
        .expirationDate("2000-01-01T00:00:00-00:00")
        .build();
  }

  CodeableConcept code() {
    return CodeableConcept.builder().coding(singletonList(coding())).text("HelloText").build();
  }

  CodeableConcept container() {
    return CodeableConcept.builder().coding(singletonList(coding())).text("HelloText").build();
  }

  Content content() {
    return Content.builder()
        .id("8888")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .item(reference())
        .amount(simpleQuantity())
        .build();
  }

  CodeableConcept form() {
    return CodeableConcept.builder().coding(singletonList(coding())).text("HelloText").build();
  }

  Ingredient ingredient() {
    return Ingredient.builder()
        .id("8888")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .item(reference())
        .amount(ratio())
        .build();
  }

  Medication medication() {
    return Medication.builder()
        .id("1234")
        .resourceType("Medication")
        .meta(meta())
        .implicitRules("http://HelloRules.com")
        .language("Hello Language")
        .text(narrative())
        .contained(singletonList(resource()))
        .extension(Arrays.asList(extension(), extension()))
        .modifierExtension(
            Arrays.asList(extension(), extensionWithQuantity(), extensionWithRatio()))
        .code(code())
        .isBrand(true)
        .manufacturer(reference())
        .product(product())
        .medicationPackage(medicationPackage())
        .build();
  }

  @JsonProperty("package")
  MedicationPackage medicationPackage() {
    return MedicationPackage.builder()
        .id("8888")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .container(container())
        .content(content())
        .build();
  }

  Product product() {
    return Product.builder()
        .id("8888")
        .extension(singletonList(extension()))
        .modifierExtension(singletonList(extension()))
        .form(form())
        .ingredient(ingredient())
        .batch(batch())
        .build();
  }

  Ratio ratio() {
    return Ratio.builder().numerator(quantity()).denominator(quantity()).build();
  }
}
