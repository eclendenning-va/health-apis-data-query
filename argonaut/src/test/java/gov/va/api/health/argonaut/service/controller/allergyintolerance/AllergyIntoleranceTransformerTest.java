package gov.va.api.health.argonaut.service.controller.allergyintolerance;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.datatypes.Annotation;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Category;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Certainty;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Criticality;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Reaction;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Status;
import gov.va.api.health.argonaut.api.resources.AllergyIntolerance.Type;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwNotes;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwReactions.CdwReaction.CdwManifestations;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwSubstance;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceCategory;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceCertainty;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceCriticality;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceStatus;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceType;
import gov.va.dvp.cdw.xsd.model.CdwAllergyManifestationSystem;
import gov.va.dvp.cdw.xsd.model.CdwAllergySubstanceSystem;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.SneakyThrows;
import org.junit.Test;

public class AllergyIntoleranceTransformerTest {

  private final CdwSampleData cdw = new CdwSampleData();
  private final Expected expected = new Expected();
  AllergyIntoleranceTransformer tx = new AllergyIntoleranceTransformer();

  @Test
  public void allergyIntolerance103TransformsToModelAllergyIntolerance() {
    assertThat(tx.apply(cdw.allergyIntolerance())).isEqualTo(expected.allergyIntolerance());
  }

  @Test
  public void gregorianCalendarTransformsToDateTime() {
    assertThat(tx.note(cdw.notes()).time()).isEqualTo(expected.note().time());
  }

  @Test
  public void manifestationCodingTransformsToCodingList() {
    assertThat(
            tx.reactionManifestationCoding(
                cdw.manifestation().getManifestation().get(0).getCoding()))
        .isEqualTo(expected.manifestation().get(0).coding());
  }

  @Test
  public void manifestationReturnsEmptyForNull() {
    assertThat(tx.reactionManifestation(null).isEmpty());
  }

  @Test
  public void note() {
    assertThat(tx.note(null)).isNull();
    assertThat(tx.note(new CdwNotes())).isNull();
    assertThat(tx.note(cdw.notes())).isEqualTo(expected.note());
  }

  @Test
  public void patientTransformsToReference() {
    assertThat(tx.reference(cdw.patient())).isEqualTo(expected.allergyIntolerance().patient());
  }

  @Test
  public void reactionReturnsEmptyForNull() {
    assertThat(tx.reaction(null).isEmpty());
  }

  @Test
  public void reactionTransformsToReactionList() {
    assertThat(tx.reaction(cdw.reactions())).isEqualTo(expected.reaction());
  }

  @Test
  public void substanceCodingTransformsToCodingList() {
    assertThat(tx.substanceCoding(cdw.substance().getCoding()))
        .isEqualTo(expected.substanceCoding());
  }

  @Test
  public void substanceTransformsToCodeableConcept() {
    assertThat(tx.substance(cdw.substance())).isEqualTo(expected.substance());
  }

  private static class CdwSampleData {

    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private CdwSampleData() {
      datatypeFactory = DatatypeFactory.newInstance();
    }

    CdwAllergyIntolerance allergyIntolerance() {
      CdwAllergyIntolerance allergyIntolerance = new CdwAllergyIntolerance();
      allergyIntolerance.setCdwId("123456789");
      allergyIntolerance.setRowNumber(new BigInteger("1"));
      allergyIntolerance.setOnset(onset());
      allergyIntolerance.setRecordedDate(recordedDate());
      allergyIntolerance.setRecorder(recorder());
      allergyIntolerance.setPatient(patient());
      allergyIntolerance.setSubstance(substance());
      allergyIntolerance.setStatus(CdwAllergyIntoleranceStatus.ENTERED_IN_ERROR);
      allergyIntolerance.setCriticality(CdwAllergyIntoleranceCriticality.CRITH);
      allergyIntolerance.setType(CdwAllergyIntoleranceType.ALLERGY);
      allergyIntolerance.setCategory(CdwAllergyIntoleranceCategory.FOOD);
      allergyIntolerance.setNotes(notes());
      allergyIntolerance.setReactions(reactions());
      return allergyIntolerance;
    }

    CdwReference author() {
      CdwReference cdwReference = new CdwReference();
      cdwReference.setDisplay("author display");
      cdwReference.setReference("author reference");
      return cdwReference;
    }

    CdwManifestations manifestation() {
      CdwManifestations manifestations = new CdwManifestations();
      CdwManifestations.CdwManifestation manifestation = new CdwManifestations.CdwManifestation();
      CdwManifestations.CdwManifestation.CdwCoding cdwManifestationCoding =
          new CdwManifestations.CdwManifestation.CdwCoding();

      cdwManifestationCoding.setCode("manifestation code");
      cdwManifestationCoding.setDisplay("manifestation display");
      cdwManifestationCoding.setSystem(
          CdwAllergyManifestationSystem.URN_OID_2_16_840_1_113883_6_233);

      manifestation.setText("manifestation text");
      manifestation.setCoding(cdwManifestationCoding);

      manifestations.getManifestation().add(manifestation);
      return manifestations;
    }

    XMLGregorianCalendar noteTime() {
      XMLGregorianCalendar gregorianCalendar = datatypeFactory.newXMLGregorianCalendar();
      gregorianCalendar.setYear(2018);
      gregorianCalendar.setMonth(11);
      gregorianCalendar.setDay(7);
      return gregorianCalendar;
    }

    CdwAllergyIntolerance.CdwNotes notes() {
      CdwAllergyIntolerance.CdwNotes notes = new CdwAllergyIntolerance.CdwNotes();
      CdwAllergyIntolerance.CdwNotes.CdwNote note = new CdwAllergyIntolerance.CdwNotes.CdwNote();
      note.setAuthor(author());
      note.setText("note text");
      note.setTime(noteTime());
      notes.getNote().add(note);
      return notes;
    }

    XMLGregorianCalendar onset() {
      XMLGregorianCalendar gregorianCalendar = datatypeFactory.newXMLGregorianCalendar();
      gregorianCalendar.setYear(2018);
      gregorianCalendar.setMonth(11);
      gregorianCalendar.setDay(7);
      return gregorianCalendar;
    }

    CdwReference patient() {
      CdwReference cdwReference = new CdwReference();
      cdwReference.setDisplay("patient display");
      cdwReference.setReference("patient reference");
      return cdwReference;
    }

    CdwAllergyIntolerance.CdwReactions reactions() {
      CdwAllergyIntolerance.CdwReactions reactions = new CdwAllergyIntolerance.CdwReactions();
      CdwAllergyIntolerance.CdwReactions.CdwReaction reaction =
          new CdwAllergyIntolerance.CdwReactions.CdwReaction();
      reaction.setCertainty(CdwAllergyIntoleranceCertainty.CONFIRMED);
      reaction.setManifestations(manifestation());
      reactions.getReaction().add(reaction);
      return reactions;
    }

    XMLGregorianCalendar recordedDate() {
      XMLGregorianCalendar gregorianCalendar = datatypeFactory.newXMLGregorianCalendar();
      gregorianCalendar.setYear(2018);
      gregorianCalendar.setMonth(11);
      gregorianCalendar.setDay(7);
      return gregorianCalendar;
    }

    CdwReference recorder() {
      CdwReference cdwReference = new CdwReference();
      cdwReference.setDisplay("recorder display");
      cdwReference.setReference("recorder reference");
      return cdwReference;
    }

    CdwSubstance substance() {
      CdwSubstance substance = new CdwSubstance();
      CdwSubstance.CdwCoding cdwSubstanceCoding = new CdwSubstance.CdwCoding();

      cdwSubstanceCoding.setCode("substance code");
      cdwSubstanceCoding.setDisplay("substance display");
      cdwSubstanceCoding.setSystem(CdwAllergySubstanceSystem.HTTP_HL_7_ORG_FHIR_NDFRT);

      substance.setCoding(cdwSubstanceCoding);
      substance.setText("substance text");
      return substance;
    }
  }

  static class Expected {
    AllergyIntolerance allergyIntolerance() {
      return AllergyIntolerance.builder()
          .resourceType("AllergyIntolerance")
          .id("123456789")
          .onset("2018-11-07")
          .recordedDate("2018-11-07")
          .recorder(recorder())
          .substance(substance())
          .patient(patient())
          .status(Status.entered_in_error)
          .criticality(Criticality.CRITH)
          .type(Type.allergy)
          .category(Category.food)
          .note(note())
          .reaction(reaction())
          .build();
    }

    Reference author() {
      return Reference.builder().display("author display").reference("author reference").build();
    }

    List<CodeableConcept> manifestation() {
      List<CodeableConcept> manifestations = new LinkedList<>();
      CodeableConcept manifestation =
          CodeableConcept.builder()
              .coding(manifestationCoding())
              .text("manifestation text")
              .build();
      manifestations.add(manifestation);
      return manifestations;
    }

    List<Coding> manifestationCoding() {
      return Collections.singletonList(
          Coding.builder()
              .system("urn:oid:2.16.840.1.113883.6.233")
              .code("manifestation code")
              .display("manifestation display")
              .build());
    }

    Annotation note() {
      return Annotation.builder()
          .authorReference(author())
          .time("2018-11-07")
          .text("note text")
          .build();
    }

    Reference patient() {
      return Reference.builder().display("patient display").reference("patient reference").build();
    }

    List<Reaction> reaction() {
      List<Reaction> reactions = new LinkedList<>();
      reactions.add(
          Reaction.builder().manifestation(manifestation()).certainty(Certainty.confirmed).build());
      return reactions;
    }

    Reference recorder() {
      return Reference.builder()
          .display("recorder display")
          .reference("recorder reference")
          .build();
    }

    CodeableConcept substance() {
      return CodeableConcept.builder().coding(substanceCoding()).text("substance text").build();
    }

    List<Coding> substanceCoding() {
      return Collections.singletonList(
          Coding.builder()
              .system("http://hl7.org/fhir/ndfrt")
              .code("substance code")
              .display("substance display")
              .build());
    }
  }
}
