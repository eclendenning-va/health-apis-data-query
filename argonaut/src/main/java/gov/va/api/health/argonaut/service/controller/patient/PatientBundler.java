package gov.va.api.health.argonaut.service.controller.patient;

import gov.va.api.health.argonaut.api.Patient;
import gov.va.api.health.argonaut.api.bundle.AbstractBundle;
import gov.va.api.health.argonaut.service.controller.PageLinks;
import gov.va.dvp.cdw.xsd.pojos.Patient103Root;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class PatientBundler implements PatientController.Bundler {

  private PatientController.Transformer patientTransformer;
  private PageLinks bundleLinker;

  @Override
  public Patient.Bundle apply(Patient103Root patient103Root)  {
    return bundle(patient103Root);
  }

  Patient.Bundle bundle(Patient103Root patient103Root) {

    return Patient.Bundle.builder()
        .resourceType("Bundle")
        .type(AbstractBundle.BundleType.searchset)
        .total(patient103Root.getRecordCount())
        .link(bundleLinker.create(
                PageLinks.LinkConfig.builder()
                        .page(patient103Root.getPageCount())
                        .recordsPerPage(patient103Root.getRecordsPerPage())
                        .totalRecords(patient103Root.getRecordCount())
                    //    .path()
                    //    .queryParams(params)
                        .build()))
        .entry(entries(patient103Root))
        .build();
  }

  List<Patient.Entry> entries(Patient103Root root) {
    List<Patient.Entry> entries = new LinkedList<>();
    for (Patient103Root.Patients.Patient patient : root.getPatients().getPatient()) {
      entries.add(
          Patient.Entry.builder()
              .fullUrl("full-url-here")
              .resource(patientTransformer.apply(patient))
              .build());
    }
    return entries;
  }
}
