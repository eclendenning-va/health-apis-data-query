package gov.va.api.health.dataquery.service.controller.patient;

import static gov.va.api.health.dataquery.service.controller.Dstu2Transformers.firstPayloadItem;
import static gov.va.api.health.dataquery.service.controller.Dstu2Transformers.hasPayload;
import static java.util.Collections.emptyList;

import gov.va.api.health.argonaut.api.resources.Patient;
import gov.va.api.health.dataquery.service.controller.AbstractIncludesIcnMajig;
import gov.va.api.health.dataquery.service.controller.CountParameter;
import gov.va.api.health.dataquery.service.controller.Dstu2Bundler;
import gov.va.api.health.dataquery.service.controller.Dstu2Validator;
import gov.va.api.health.dataquery.service.controller.PageLinks;
import gov.va.api.health.dataquery.service.controller.Parameters;
import gov.va.api.health.dataquery.service.controller.ResourceExceptions;
import gov.va.api.health.dataquery.service.controller.WitnessProtection;
import gov.va.api.health.dataquery.service.mranderson.client.MrAndersonClient;
import gov.va.api.health.dataquery.service.mranderson.client.Query;
import gov.va.api.health.dstu2.api.resources.OperationOutcome;
import gov.va.dvp.cdw.xsd.model.CdwPatient103Root;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Request Mappings for Patient Profile, see
 * https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html for implementation
 * details.
 */
@Slf4j
@Validated
@RestController
@SuppressWarnings("WeakerAccess")
@RequestMapping(
  value = {"/dstu2/Patient"},
  produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
public class PatientController {
  private final Datamart datamart = new Datamart();

  private Transformer transformer;

  private MrAndersonClient mrAndersonClient;

  private Dstu2Bundler bundler;

  private PatientSearchRepository repository;

  private WitnessProtection witnessProtection;

  private boolean defaultToDatamart;

  /** Autowired constructor. */
  public PatientController(
      @Value("${datamart.patient}") boolean defaultToDatamart,
      @Autowired Transformer transformer,
      @Autowired MrAndersonClient mrAndersonClient,
      @Autowired Dstu2Bundler bundler,
      @Autowired PatientSearchRepository repository,
      @Autowired WitnessProtection witnessProtection) {
    this.defaultToDatamart = defaultToDatamart;
    this.transformer = transformer;
    this.mrAndersonClient = mrAndersonClient;
    this.bundler = bundler;
    this.repository = repository;
    this.witnessProtection = witnessProtection;
  }

  private Patient.Bundle mrAndersonBundle(MultiValueMap<String, String> parameters) {
    CdwPatient103Root root = mrAndersonSearch(parameters);
    PageLinks.LinkConfig linkConfig =
        PageLinks.LinkConfig.builder()
            .path("Patient")
            .queryParams(parameters)
            .page(Parameters.pageOf(parameters))
            .recordsPerPage(Parameters.countOf(parameters))
            .totalRecords(root.getRecordCount())
            .build();
    return bundler.bundle(
        Dstu2Bundler.BundleContext.of(
            linkConfig,
            root.getPatients() == null ? Collections.emptyList() : root.getPatients().getPatient(),
            transformer,
            Patient.Entry::new,
            Patient.Bundle::new));
  }

  private CdwPatient103Root mrAndersonSearch(MultiValueMap<String, String> params) {
    Query<CdwPatient103Root> query =
        Query.forType(CdwPatient103Root.class)
            .profile(Query.Profile.ARGONAUT)
            .resource("Patient")
            .version("1.03")
            .parameters(params)
            .build();
    return hasPayload(mrAndersonClient.search(query));
  }

  /** Read by id. */
  @GetMapping(value = {"/{publicId}"})
  public Patient read(
      @RequestHeader(value = "Datamart", defaultValue = "") String datamartHeader,
      @PathVariable("publicId") String publicId) {
    if (datamart.isDatamartRequest(datamartHeader)) {
      return datamart.read(publicId);
    }
    return transformer.apply(
        firstPayloadItem(
            hasPayload(mrAndersonSearch(Parameters.forIdentity(publicId)).getPatients())
                .getPatient()));
  }

  /** Return the raw Datamart document for the given identifier. */
  @GetMapping(
    value = "/{publicId}",
    headers = {"raw=true"}
  )
  public String readRaw(@PathVariable("publicId") String publicId, HttpServletResponse response) {
    PatientSearchEntity entity = datamart.readRaw(publicId);
    AbstractIncludesIcnMajig.addHeader(response, entity.icn());
    return entity.patient().payload();
  }

  /** Search by Family+Gender. */
  @GetMapping(params = {"family", "gender"})
  public Patient.Bundle searchByFamilyAndGender(
      @RequestHeader(value = "Datamart", defaultValue = "") String datamartHeader,
      @RequestParam("family") String family,
      @RequestParam("gender") String gender,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    if (datamart.isDatamartRequest(datamartHeader)) {
      return datamart.searchByFamilyAndGender(family, gender, page, count);
    }
    return mrAndersonBundle(
        Parameters.builder()
            .add("family", family)
            .add("gender", gender)
            .add("page", page)
            .add("_count", count)
            .build());
  }

  /** Search by Given+Gender. */
  @GetMapping(params = {"given", "gender"})
  public Patient.Bundle searchByGivenAndGender(
      @RequestHeader(value = "Datamart", defaultValue = "") String datamartHeader,
      @RequestParam("given") String given,
      @RequestParam("gender") String gender,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    if (datamart.isDatamartRequest(datamartHeader)) {
      return datamart.searchByGivenAndGender(given, gender, page, count);
    }
    return mrAndersonBundle(
        Parameters.builder()
            .add("given", given)
            .add("gender", gender)
            .add("page", page)
            .add("_count", count)
            .build());
  }

  /** Search by _id. */
  @GetMapping(params = {"_id"})
  public Patient.Bundle searchById(
      @RequestHeader(value = "Datamart", defaultValue = "") String datamartHeader,
      @RequestParam("_id") String id,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    if (datamart.isDatamartRequest(datamartHeader)) {
      return datamart.searchById(id, page, count);
    }
    return mrAndersonBundle(
        Parameters.builder().add("identifier", id).add("page", page).add("_count", count).build());
  }

  /** Search by Identifier. */
  @GetMapping(params = {"identifier"})
  public Patient.Bundle searchByIdentifier(
      @RequestHeader(value = "Datamart", defaultValue = "") String datamartHeader,
      @RequestParam("identifier") String id,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    return searchById(datamartHeader, id, page, count);
  }

  /** Search by Name+Birthdate. */
  @GetMapping(params = {"name", "birthdate"})
  public Patient.Bundle searchByNameAndBirthdate(
      @RequestHeader(value = "Datamart", defaultValue = "") String datamartHeader,
      @RequestParam("name") String name,
      @RequestParam("birthdate") String[] birthdate,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    if (datamart.isDatamartRequest(datamartHeader)) {
      return datamart.searchByNameAndBirthdate(name, birthdate, page, count);
    }
    return mrAndersonBundle(
        Parameters.builder()
            .add("name", name)
            .addAll("birthdate", birthdate)
            .add("page", page)
            .add("_count", count)
            .build());
  }

  /** Search by Name+Gender. */
  @GetMapping(params = {"name", "gender"})
  public Patient.Bundle searchByNameAndGender(
      @RequestHeader(value = "Datamart", defaultValue = "") String datamartHeader,
      @RequestParam("name") String name,
      @RequestParam("gender") String gender,
      @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
      @CountParameter @Min(0) int count) {
    if (datamart.isDatamartRequest(datamartHeader)) {
      return datamart.searchByNameAndGender(name, gender, page, count);
    }
    return mrAndersonBundle(
        Parameters.builder()
            .add("name", name)
            .add("gender", gender)
            .add("page", page)
            .add("_count", count)
            .build());
  }

  /** Hey, this is a validate endpoint. It validates. */
  @PostMapping(
    value = "/$validate",
    consumes = {"application/json", "application/json+fhir", "application/fhir+json"}
  )
  public OperationOutcome validate(@RequestBody Patient.Bundle bundle) {
    return Dstu2Validator.create().validate(bundle);
  }

  public interface Transformer
      extends Function<CdwPatient103Root.CdwPatients.CdwPatient, Patient> {}

  /**
   * This class is being used to help organize the code such that all the datamart logic is
   * contained together. In the future when Mr. Anderson support is dropped, this class can be
   * eliminated.
   */
  private class Datamart {
    Patient.Bundle bundle(
        MultiValueMap<String, String> parameters, List<Patient> reports, int totalRecords) {
      PageLinks.LinkConfig linkConfig =
          PageLinks.LinkConfig.builder()
              .path("Patient")
              .queryParams(parameters)
              .page(Parameters.pageOf(parameters))
              .recordsPerPage(Parameters.countOf(parameters))
              .totalRecords(totalRecords)
              .build();
      return bundler.bundle(
          Dstu2Bundler.BundleContext.of(
              linkConfig, reports, Patient.Entry::new, Patient.Bundle::new));
    }

    Patient.Bundle bundle(
        MultiValueMap<String, String> parameters, Page<PatientSearchEntity> entities) {
      log.info("Search {} found {} results", parameters, entities.getTotalElements());
      if (Parameters.countOf(parameters) == 0) {
        return bundle(parameters, emptyList(), (int) entities.getTotalElements());
      }
      return bundle(
          parameters,
          entities
              .get()
              .map(PatientSearchEntity::asDatamartPatient)
              .map(this::transform)
              .collect(Collectors.toList()),
          (int) entities.getTotalElements());
    }

    String cdwGender(String fhirGender) {
      String cdw = GenderMapping.toCdw(fhirGender);
      if (cdw == null) {
        throw new IllegalArgumentException("unknown gender: " + fhirGender);
      }
      return cdw;
    }

    PatientSearchEntity findById(String publicId) {
      Optional<PatientSearchEntity> entity =
          repository.findById(witnessProtection.toCdwId(publicId));
      return entity.orElseThrow(() -> new ResourceExceptions.NotFound(publicId));
    }

    boolean isDatamartRequest(String datamartHeader) {
      if (StringUtils.isBlank(datamartHeader)) {
        return defaultToDatamart;
      }
      return BooleanUtils.isTrue(BooleanUtils.toBooleanObject(datamartHeader));
    }

    PageRequest page(int page, int count) {
      return PageRequest.of(page - 1, count == 0 ? 1 : count, PatientSearchEntity.naturalOrder());
    }

    Patient read(String publicId) {
      return transform(findById(publicId).asDatamartPatient());
    }

    PatientSearchEntity readRaw(String publicId) {
      return findById(publicId);
    }

    Patient.Bundle searchByFamilyAndGender(String family, String gender, int page, int count) {
      return bundle(
          Parameters.builder()
              .add("family", family)
              .add("gender", gender)
              .add("page", page)
              .add("_count", count)
              .build(),
          repository.findByLastNameAndGender(family, cdwGender(gender), page(page, count)));
    }

    Patient.Bundle searchByGivenAndGender(String given, String gender, int page, int count) {
      return bundle(
          Parameters.builder()
              .add("given", given)
              .add("gender", gender)
              .add("page", page)
              .add("_count", count)
              .build(),
          repository.findByFirstNameAndGender(given, cdwGender(gender), page(page, count)));
    }

    Patient.Bundle searchById(String publicId, int page, int count) {
      Patient resource = read(publicId);
      return bundle(
          Parameters.builder()
              .add("identifier", publicId)
              .add("page", page)
              .add("_count", count)
              .build(),
          resource == null || count == 0 ? emptyList() : List.of(resource),
          resource == null ? 0 : 1);
    }

    Patient.Bundle searchByNameAndBirthdate(String name, String[] birthdate, int page, int count) {
      PatientSearchRepository.NameAndBirthdateSpecification spec =
          PatientSearchRepository.NameAndBirthdateSpecification.builder()
              .name(name)
              .dates(birthdate)
              .build();
      log.info("Looking for {} {}", name, spec);
      Page<PatientSearchEntity> entities = repository.findAll(spec, page(page, count));
      return bundle(
          Parameters.builder()
              .add("name", name)
              .addAll("birthdate", birthdate)
              .add("page", page)
              .add("_count", count)
              .build(),
          entities);
    }

    Patient.Bundle searchByNameAndGender(String name, String gender, int page, int count) {
      return bundle(
          Parameters.builder()
              .add("name", name)
              .add("gender", gender)
              .add("page", page)
              .add("_count", count)
              .build(),
          repository.findByNameAndGender(name, cdwGender(gender), page(page, count)));
    }

    Patient transform(DatamartPatient dm) {
      return DatamartPatientTransformer.builder().datamart(dm).build().toFhir();
    }
  }
}
