package gov.va.health.api.sentinel.ids;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.ids.api.Registration;
import gov.va.api.health.ids.api.ResourceIdentity;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class IdsIT {

  @Test
  public void registerFlow() {
    ResourceIdentity identity =
        ResourceIdentity.builder()
            .system("CDW")
            .resource("WHATEVER")
            .identifier("whatever")
            .build();

    List<Registration> registrations =
        client()
            .post("/api/v1/ids", Arrays.asList(identity))
            .expect(201)
            .expectListOf(Registration.class);
    assertThat(registrations.size()).isEqualTo(1);

    List<ResourceIdentity> identities =
        client()
            .get("/api/v1/ids/{id}", registrations.get(0).uuid())
            .expect(200)
            .expectListOf(ResourceIdentity.class);

    assertThat(identities).containsExactly(identity);
  }

  private TestClient client() {
    return Sentinel.get().clients().ids();
  }
}
