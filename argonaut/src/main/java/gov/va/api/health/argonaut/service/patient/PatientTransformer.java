package gov.va.api.health.argonaut.service.patient;

import gov.va.api.health.argonaut.api.*;
import gov.va.dvp.cdw.xsd.pojos.Extensions;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Service
public class PatientTransformer implements PatientController.PatientTransformer {

  @Override
  public Patient apply(Patient103Root.Patients.Patient patient) {

    return Patient.builder()
            .id(patient.getCdwId())
            // TODO: Fix these in the model! They are zero to many
            //.argoRace(race(patient.getArgoRace()))
            //.argoEthnicity()
            //.argoBirthSex()
            .identifier(identifiers(patient.getIdentifier()))
            .build();
  }

  private ArgoRaceExtension race(List<Extensions> argoRace) {

    return ArgoRaceExtension.builder().build();
  }

  private List<Identifier> identifiers(List<Patient103Root.Patients.Patient.Identifier> identifiers) {
    List<Identifier> argoIdentifiers = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Identifier identifier: identifiers) {
      argoIdentifiers.add(
              Identifier.builder()
                      .use(identifierUse(identifier))
                      .type(codeableConcept(identifier.getType()))
                      .system(identifier.getSystem())
                      .value(identifier.getValue())
                      .assigner(reference(identifier.getAssigner()))
                      .build());
    }
    return argoIdentifiers;
  }

  private Reference reference(Patient103Root.Patients.Patient.Identifier.Assigner assigner) {
    return Reference.builder()
            .display(assigner.getDisplay())
            .build();
  }

  private Identifier.IdentifierUse identifierUse(Patient103Root.Patients.Patient.Identifier identifier) {
    return Identifier.IdentifierUse.valueOf(identifier.getUse().name());
  }

  private CodeableConcept codeableConcept(Patient103Root.Patients.Patient.Identifier.Type type) {
    return CodeableConcept.builder()
            .coding(codings(type.getCoding()))
            .build();
  }

  private List<Coding> codings(List<Patient103Root.Patients.Patient.Identifier.Type.Coding> codings) {
    List<Coding> argoCodings = new LinkedList<>();
    for (Patient103Root.Patients.Patient.Identifier.Type.Coding coding: codings) {
      argoCodings.add(
              Coding.builder()
                      .system(coding.getSystem())
                      .code(coding.getCode())
                      .build());
    }
    return argoCodings;
  }
}
