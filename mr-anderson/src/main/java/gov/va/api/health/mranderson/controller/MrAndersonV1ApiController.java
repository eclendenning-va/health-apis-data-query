package gov.va.api.health.mranderson.controller;

import gov.va.api.health.mranderson.cdw.Profile;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources;
import java.beans.PropertyEditorSupport;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

@RestController
@Validated
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class MrAndersonV1ApiController {

  private final Resources resources;

  /**
   * Implementation of /v1/resources/{profile}/{resourceType}/{resourceVersion}. See api-v1.yaml.
   */
  @RequestMapping(
    value = "/v1/resources/{profile}/{resourceType}/{resourceVersion}",
    produces = {
      "application/xml",
      "application/json",
    },
    method = RequestMethod.GET
  )
  @SneakyThrows
  public ResponseEntity<String> queryResourceVersion(
      @Valid @PathVariable("profile") Profile profile,
      @Valid @PathVariable("resourceType") @Pattern(regexp = "[A-Za-z]+") String resourceType,
      @Valid @PathVariable("resourceVersion") @Pattern(regexp = "[0-9]+\\.[0-9]+")
          String resourceVersion,
      @Valid @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) int page,
      @Valid @RequestParam(value = "_count", required = false, defaultValue = "15") @Min(0)
          int count,
      ServerWebExchange exchange) {

    String xml =
        resources.search(
            Query.builder()
                .profile(profile)
                .resource(resourceType)
                .version(resourceVersion)
                .page(page)
                .count(count)
                .parameters(exchange.getRequest().getQueryParams())
                .build());

    return ResponseEntity.ok().body(xml);
  }

  /** Support profile values in any case. */
  @InitBinder
  public void initBinder(WebDataBinder dataBinder) {
    dataBinder.registerCustomEditor(
        Profile.class,
        new PropertyEditorSupport() {
          @Override
          public void setAsText(String text) throws IllegalArgumentException {
            setValue(Profile.fromValue(text));
          }
        });
  }
}
