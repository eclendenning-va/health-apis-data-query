package gov.va.api.health.argonaut.service.controller.allergyintolerance;

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
import gov.va.api.health.argonaut.service.controller.allergyintollerance.AllergyIntoleranceTransformer;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntolerance103Root.CdwAllergyIntolerances.CdwAllergyIntolerance.CdwReactions.CdwReaction.CdwManifestations;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceCategory;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceCertainty;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceCriticality;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceStatus;
import gov.va.dvp.cdw.xsd.model.CdwAllergyIntoleranceType;
import gov.va.dvp.cdw.xsd.model.CdwAllergyManifestationSystem;
import gov.va.dvp.cdw.xsd.model.CdwCodeableConcept;
import gov.va.dvp.cdw.xsd.model.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwReference;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class AllergyIntoleranceTransformerTest {
  private AllergyIntoleranceTransformer transformer() {
    return new AllergyIntoleranceTransformer();
  }

  private static class AllergyIntoleranceSampleData {
    AllergyIntolerance allergyIntolerance() {
      return AllergyIntolerance.builder()
          .resourceType("Allergy Intolerance")
          .id("123456789")
          .onset("2018-11-07")
          .recordedDate("2018-11-07")
          .recorder(reference("recorder"))
          .patient(reference("patient"))
          .status(Status.active)
          .criticality(Criticality.CRITH)
          .type(Type.allergy)
          .category(Category.environment)
          .note(note())
          .reaction(Collections.singletonList(reaction()))
          .build();
    }

    Annotation note() {
      return Annotation.builder()
          .authorReference(reference("notes"))
          .time("2018-11-07")
          .text("test text")
          .build();
    }

    private CodeableConcept codeableConcept(String prefix) {
      return CodeableConcept.builder().coding(coding(prefix)).text(prefix + " text").build();
    }

    private List<Coding> coding(String prefix) {
      return Collections.singletonList(
          Coding.builder()
              .system(prefix + " system")
              .code(prefix + " code")
              .display(prefix + " display")
              .build());
    }

    Reaction reaction() {
      return Reaction.builder()
          .manifestation(Collections.singletonList(codeableConcept("manifesation")))
          .certainty(Certainty.confirmed)
          .build();
    }

    Reference reference(String prefix) {
      return Reference.builder()
          .display(prefix + " display")
          .reference(prefix + " reference")
          .build();
    }
  }

  private static class XmlSampleData {

    private DatatypeFactory datatypeFactory;

    CdwAllergyIntolerance allergyIntolerance() {
      CdwAllergyIntolerance allergyIntolerance = new CdwAllergyIntolerance();
      allergyIntolerance.setCdwId("123456789");
      allergyIntolerance.setRowNumber(new BigInteger("1"));
      allergyIntolerance.setOnset(gregorianCalendar());
      allergyIntolerance.setRecordedDate(gregorianCalendar());
      allergyIntolerance.setRecorder(cdwReference("recorder"));
      allergyIntolerance.setPatient(cdwReference("patient"));
      allergyIntolerance.setStatus(CdwAllergyIntoleranceStatus.ACTIVE);
      allergyIntolerance.setCriticality(CdwAllergyIntoleranceCriticality.CRITH);
      allergyIntolerance.setType(CdwAllergyIntoleranceType.ALLERGY);
      allergyIntolerance.setCategory(CdwAllergyIntoleranceCategory.FOOD);
      allergyIntolerance.setNotes(notes());
      allergyIntolerance.setReactions(reactions());
      return allergyIntolerance;
    }

    CdwCodeableConcept codeableConcept(String prefix) {
      CdwCodeableConcept code = new CdwCodeableConcept();
      code.getCoding().add(coding(prefix));
      code.setText(prefix + " text");
      return code;
    }

    CdwCoding coding(String prefix) {
      CdwCoding coding = new CdwCoding();
      coding.setSystem(prefix + " system");
      coding.setCode(prefix + " code");
      coding.setDisplay(prefix + " display");
      return coding;
    }

    CdwReference cdwReference(String prefix) {
      CdwReference cdwReference = new CdwReference();
      cdwReference.setDisplay(prefix + " display");
      cdwReference.setReference(prefix + " reference");
      return cdwReference;
    }

    XMLGregorianCalendar gregorianCalendar() {
      XMLGregorianCalendar gregorianCalendar = datatypeFactory.newXMLGregorianCalendar();
      gregorianCalendar.setYear(2018);
      gregorianCalendar.setMonth(11);
      gregorianCalendar.setDay(7);
      return gregorianCalendar;
    }

    CdwAllergyIntolerance.CdwNotes notes() {
      CdwAllergyIntolerance.CdwNotes notes = new CdwAllergyIntolerance.CdwNotes();
      CdwAllergyIntolerance.CdwNotes.CdwNote note = new CdwAllergyIntolerance.CdwNotes.CdwNote();
      note.setAuthor(cdwReference("notes"));
      note.setText("test text");
      note.setTime(gregorianCalendar());
      notes.getNote().add(note);
      return notes;
    }

    CdwAllergyIntolerance.CdwReactions reactions() {
      CdwAllergyIntolerance.CdwReactions reactions = new CdwAllergyIntolerance.CdwReactions();
      CdwAllergyIntolerance.CdwReactions.CdwReaction reaction =
          new CdwAllergyIntolerance.CdwReactions.CdwReaction();
      reaction.setCertainty(CdwAllergyIntoleranceCertainty.CONFIRMED);
      reaction.setManifestations(manifestation());
      return reactions;
    }

    CdwManifestations manifestation() {
      CdwManifestations manifestations = new CdwManifestations();
      CdwManifestations.CdwManifestation manifestation = new CdwManifestations.CdwManifestation();
      CdwManifestations.CdwManifestation.CdwCoding cdwManifestationCoding =
          new CdwManifestations.CdwManifestation.CdwCoding();

      cdwManifestationCoding.setCode("manifesation coding");
      cdwManifestationCoding.setDisplay("manifesation display");
      cdwManifestationCoding.setSystem(
          CdwAllergyManifestationSystem.valueOf("manifesation system"));

      manifestation.setText("test text");
      manifestation.setCoding(cdwManifestationCoding);

      manifestations.getManifestation().add(manifestation);
      return manifestations;
    }
  }
}
