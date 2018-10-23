package gov.va.api.health.ids.service.controller;

import gov.va.api.health.ids.api.Registration;
import gov.va.api.health.ids.api.ResourceIdentity;
import gov.va.api.health.ids.service.controller.impl.ResourceIdentityDetail;
import gov.va.api.health.ids.service.controller.impl.ResourceIdentityDetailRepository;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

@RestController
@Validated
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class IdServiceV1ApiController {

  private final ResourceIdentityDetailRepository repository;
  private final UuidGenerator uuidGenerator;

  /** Implementation of GET /v1/ids/{publicId}. See api-v1.yaml. */
  @RequestMapping(
    value = "/v1/ids/{publicId}",
    produces = {"application/json"},
    method = RequestMethod.GET
  )
  @SneakyThrows
  public ResponseEntity<List<ResourceIdentity>> lookup(
      @Valid @PathVariable("publicId") @Pattern(regexp = "[-A-Za-z0-9]+") String publicId,
      ServerWebExchange exchange) {

    List<ResourceIdentity> identities =
        repository
            .findByUuid(publicId)
            .stream()
            .map(ResourceIdentityDetail::asResourceIdentity)
            .collect(Collectors.toList());

    log.info("Found {}", identities);

    return ResponseEntity.ok().body(identities);
  }

  /** Implementation of POST /v1/ids. See api-v1.yaml. */
  @RequestMapping(
    value = "v1/ids",
    produces = {"application/json"},
    consumes = {"application/json"},
    method = RequestMethod.POST
  )
  public ResponseEntity<List<Registration>> register(
      @Valid @RequestBody List<ResourceIdentity> identities) {

    List<Registration> registrations =
        identities.stream().map(this::toRegistration).collect(Collectors.toList());

    List<ResourceIdentityDetail> newRegistrations =
        registrations
            .stream()
            .filter(this::isNotRegistered)
            .map(this::toDatabaseEntry)
            .collect(Collectors.toList());

    log.info("Register {} entries ({} are new)", identities.size(), newRegistrations.size());
    repository.saveAll(newRegistrations);

    return ResponseEntity.status(HttpStatus.CREATED).body(registrations);
  }

  private boolean isNotRegistered(Registration registration) {
    return repository.findByUuid(registration.uuid()).isEmpty();
  }

  private Registration toRegistration(ResourceIdentity resourceIdentity) {
    return Registration.builder()
        .uuid(uuidGenerator.apply(resourceIdentity))
        .resourceIdentity(resourceIdentity)
        .build();
  }

  private ResourceIdentityDetail toDatabaseEntry(Registration registration) {
    ResourceIdentity resourceIdentity = registration.resourceIdentities().get(0);
    return ResourceIdentityDetail.builder()
        .uuid(registration.uuid())
        .system(resourceIdentity.system())
        .resource(resourceIdentity.resource())
        .identifier(resourceIdentity.identifier())
        .build();
  }

  /**
   * Generates consistent public UUIDs for a given resource identity. This function should be
   * deterministic. Failure to do so will result in multiple registrations for the same identity.
   */
  public interface UuidGenerator extends Function<ResourceIdentity, String> {}
}
