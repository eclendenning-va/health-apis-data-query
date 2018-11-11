package gov.va.api.health.argonaut.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gov.va.api.health.argonaut.api.bundle.AbstractBundle;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry;
import gov.va.api.health.argonaut.api.bundle.BundleLink;
import gov.va.api.health.argonaut.api.validation.RelatedFields;
import gov.va.api.health.argonaut.api.validation.ZeroOrOneOf;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonDeserialize(builder = Patient.PatientBuilder.class)
@Schema(
  description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html"
)
@RelatedFields({
  @ZeroOrOneOf(
    fields = {"deceasedBoolean", "deceasedDateTime"},
    message = "Only one deceased value may be specified"
  ),
  @ZeroOrOneOf(
    fields = {"multipleBirthBoolean", "multipleBirthInteger"},
    message = "Only one multiple birth value may be specified"
  )
})
public class Patient {

  @NotBlank
  @Pattern(regexp = Fhir.ID)
  String id;

  @NotBlank String resourceType;
  @Valid Meta meta;

  @Pattern(regexp = Fhir.URI)
  @Schema()
  String implicitRules;

  @Pattern(regexp = Fhir.CODE)
  String language;

  @Valid Narrative text;
  @Valid List<SimpleResource> contained;
  @Valid List<Extension> extension;
  @NotEmpty @Valid List<Identifier> identifier;
  @Valid List<Extension> modifierExtension;
  Boolean active;
  @NotEmpty @Valid List<HumanName> name;
  @Valid List<ContactPoint> telecom;
  @NotNull Gender gender;

  @Pattern(regexp = Fhir.DATE)
  String birthDate;

  Boolean deceasedBoolean;

  @Pattern(regexp = Fhir.DATETIME)
  String deceasedDateTime;

  @Valid List<Address> address;
  @Valid CodeableConcept maritalStatus;
  Boolean multipleBirthBoolean;
  Integer multipleBirthInteger;
  @Valid List<Attachment> photo;
  @Valid List<Contact> contact;
  @Valid List<Communication> communication;
  @Valid List<Reference> careProvider;
  @Valid Reference managingOrganization;
  @Valid List<Link> link;

  public enum Gender {
    male,
    female,
    other,
    unknown
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @JsonDeserialize(builder = Patient.Bundle.BundleBuilder.class)
  public static class Bundle extends AbstractBundle<Patient.Entry> {

    @Builder
    public Bundle(
        @Pattern(regexp = Fhir.ID) String id,
        @Valid Meta meta,
        @Pattern(regexp = Fhir.URI) String implicitRules,
        @Pattern(regexp = Fhir.CODE) String language,
        @NotNull BundleType type,
        @Min(0) Integer total,
        @Valid List<BundleLink> link,
        @Valid List<Entry> entry,
        @Valid Signature signature) {
      super(id, meta, implicitRules, language, type, total, link, entry, signature);
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @JsonDeserialize(builder = Patient.Entry.EntryBuilder.class)
  public static class Entry extends AbstractEntry<Patient> {

    @Builder
    public Entry(
        @Pattern(regexp = Fhir.ID) String id,
        @Valid List<Extension> extension,
        @Valid List<Extension> modifierExtension,
        @Valid List<BundleLink> link,
        @Pattern(regexp = Fhir.URI) String fullUrl,
        @Valid Patient resource,
        @Valid Search search,
        @Valid Request request,
        @Valid Response response) {
      super(id, extension, modifierExtension, link, fullUrl, resource, search, request, response);
    }
  }
}
