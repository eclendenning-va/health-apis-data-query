package gov.va.api.health.autoconfig.configuration.testapp;

import gov.va.api.health.autoconfig.configuration.testapp.Fugazi.CustomBuilder;
import gov.va.api.health.autoconfig.configuration.testapp.Fugazi.Specified;
import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("WeakerAccess")
@RestController
public class FugaziController {

  @GetMapping(path = "/hello")
  public Fugazi hello() {
    return Fugazi.builder()
        .thing("Howdy")
        .time(Instant.now())
        .specified(Specified.builder().troofs(true).build())
        .cb(CustomBuilder.makeOne().one(1).build())
        .build();
  }
}
