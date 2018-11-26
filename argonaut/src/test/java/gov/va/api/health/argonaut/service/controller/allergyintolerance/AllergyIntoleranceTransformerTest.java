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

  private final XmlSampleData cdw = new XmlSampleData();
  private final AllergyIntoleranceSampleData expectedAllergyIntolerance =
      new AllergyIntoleranceSampleData();

  @Test
  public void allergyIntolerance103TransformsToModelAllergyIntolerance() {
    AllergyIntolerance test = transformer().apply(cdw.allergyIntolerance());
    AllergyIntolerance expected = expectedAllergyIntolerance.allergyIntolerance();
    assertThat(test).isEqualTo(expected);
  }

  @Test
  public void gregorianCalendarTransformsToDateTime() {
    String testTime = transformer().note(cdw.notes()).time();
    String expectedTime = expectedAllergyIntolerance.note().time();
    assertThat(testTime).isEqualTo(expectedTime);
  }

  @Test
  public void manifestationCodingTransformsToCodingList() {
    List<Coding> testCodings =
        transformer()
            .reactionManifestationCoding(cdw.manifestation().getManifestation().get(0).getCoding());
    List<Coding> expectedCodings = expectedAllergyIntolerance.manifestation().get(0).coding();
    assertThat(testCodings).isEqualTo(expectedCodings);
  }

  @Test
  public void manifestationReturnsEmptyForNull() {
    assertThat(transformer().reactionManifestation(null).isEmpty());
  }

  @Test
  public void noteReturnsNullForNull() {
    assertThat(transformer().note(null)).isNull();
  }

  @Test
  public void noteTransformsToAnnotation() {
    Annotation testNote = transformer().note(cdw.notes());
    Annotation expectedNote = expectedAllergyIntolerance.note();
    assertThat(testNote).isEqualTo(expectedNote);
  }

  @Test
  public void patientTransformsToReference() {
    Reference testPatient = transformer().patient(cdw.patient());
    Reference expectedPatient = expectedAllergyIntolerance.allergyIntolerance().patient();
    assertThat(testPatient).isEqualTo(expectedPatient);
  }

  @Test
  public void recorderTransformsToReference() {
    Reference testPatient = transformer().recorder(cdw.recorder());
    Reference expectedPatient = expectedAllergyIntolerance.allergyIntolerance().recorder();
    assertThat(testPatient).isEqualTo(expectedPatient);
  }

  @Test
  public void reactionTransformsToReactionList() {
    List<Reaction> testReactions = transformer().reaction(cdw.reactions());
    List<Reaction> expectedReactions = expectedAllergyIntolerance.reaction();
    assertThat(testReactions).isEqualTo(expectedReactions);
  }

  @Test
  public void reactionReturnsEmptyForNull() {
    assertThat(transformer().reaction(null).isEmpty());
  }

  @Test
  public void substanceTransformsToCodeableConcept() {
    CodeableConcept testSubstance = transformer().substance(cdw.substance());
    CodeableConcept expectedSubstance = expectedAllergyIntolerance.substance();
    assertThat(testSubstance).isEqualTo(expectedSubstance);
  }

  @Test
  public void substanceCodingTransformsToCodingList() {
    List<Coding> testCoding = transformer().substanceCoding(cdw.substance().getCoding());
    List<Coding> expectedCoding = expectedAllergyIntolerance.substanceCoding();
    assertThat(testCoding).isEqualTo(expectedCoding);
  }

  private AllergyIntoleranceTransformer transformer() {
    return new AllergyIntoleranceTransformer();
  }

  static class AllergyIntoleranceSampleData {
    AllergyIntolerance allergyIntolerance() {
      return AllergyIntolerance.builder()
          .resourceType("AllergyIntolerance")
          .id("123456789")
          .onset("2018-11-07")
          .recordedDate("2018-11-07")
          .recorder(recorder())
          .substance(substance())
          .patient(patient())
          .status(Status.active)
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

  private static class XmlSampleData {

    private DatatypeFactory datatypeFactory;

    @SneakyThrows
    private XmlSampleData() {
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
      allergyIntolerance.setStatus(CdwAllergyIntoleranceStatus.ACTIVE);
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

    CdwAllergyIntolerance.CdwNotes notes() {
      CdwAllergyIntolerance.CdwNotes notes = new CdwAllergyIntolerance.CdwNotes();
      CdwAllergyIntolerance.CdwNotes.CdwNote note = new CdwAllergyIntolerance.CdwNotes.CdwNote();
      note.setAuthor(author());
      note.setText("note text");
      note.setTime(noteTime());
      notes.getNote().add(note);
      return notes;
    }

    XMLGregorianCalendar noteTime() {
      XMLGregorianCalendar gregorianCalendar = datatypeFactory.newXMLGregorianCalendar();
      gregorianCalendar.setYear(2018);
      gregorianCalendar.setMonth(11);
      gregorianCalendar.setDay(7);
      return gregorianCalendar;
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
  }
}
