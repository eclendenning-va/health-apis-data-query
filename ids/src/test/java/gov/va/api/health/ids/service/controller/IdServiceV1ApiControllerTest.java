package gov.va.api.health.ids.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.ids.api.Registration;
import gov.va.api.health.ids.api.ResourceIdentity;
import gov.va.api.health.ids.service.controller.IdServiceV1ApiController.UuidGenerator;
import gov.va.api.health.ids.service.controller.impl.ResourceIdentityDetail;
import gov.va.api.health.ids.service.controller.impl.ResourceIdentityDetailRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;

public class IdServiceV1ApiControllerTest {

  @Mock ResourceIdentityDetailRepository repo;
  @Mock ServerWebExchange exchange;
  @Mock UuidGenerator uuidGenerator;

  IdServiceV1ApiController controller;

  @Before
  public void _init() {
    MockitoAnnotations.initMocks(this);
    controller = new IdServiceV1ApiController(repo, uuidGenerator);
  }

  private ResourceIdentity resourceIdentity(int i) {
    return ResourceIdentity.builder().identifier("i" + i).resource("r" + i).system("s" + i).build();
  }

  private Registration registration(String uuid, ResourceIdentity resourceIdentity) {
    return Registration.builder().uuid(uuid).resourceIdentity(resourceIdentity).build();
  }

  /**
   * What a detail will look like if it already exists in the database. The auto-incremented primary
   * key field 'pk' will be set.
   */
  private ResourceIdentityDetail existingDetail(String uuid, int i) {
    return ResourceIdentityDetail.builder()
        .pk(i)
        .identifier("i" + i)
        .resource("r" + i)
        .system("s" + i)
        .uuid(uuid)
        .build();
  }

  /**
   * What a detail will look like if it does not exist in the database. The auto-incremented primary
   * key field 'pk' will be 0.
   */
  private ResourceIdentityDetail newDetail(String uuid, int i) {
    return ResourceIdentityDetail.builder()
        .pk(0)
        .identifier("i" + i)
        .resource("r" + i)
        .system("s" + i)
        .uuid(uuid)
        .build();
  }

  @Test
  public void registrationReturn201AndRegistrationsForUnregisteredId() {
    ResourceIdentity id1 = resourceIdentity(1);
    ResourceIdentity id2 = resourceIdentity(2);
    when(repo.findByUuid(Mockito.anyString())).thenReturn(Collections.emptyList());
    when(uuidGenerator.apply(id1)).thenReturn("1");
    when(uuidGenerator.apply(id2)).thenReturn("2");

    ArgumentCaptor<Iterable> saveArgs = ArgumentCaptor.forClass(Iterable.class);

    ResponseEntity<List<Registration>> actual = controller.register(Arrays.asList(id1, id2));

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(actual.getBody())
        .containsExactlyInAnyOrder(registration("1", id1), registration("2", id2));
    verify(repo).saveAll(saveArgs.capture());
    assertThat(saveArgs.getValue()).containsExactlyInAnyOrder(newDetail("1", 1), newDetail("2", 2));
  }

  @Test
  public void lookupReturns200AndIdentitiesWhenFound() {
    List<ResourceIdentityDetail> searchResults =
        Arrays.asList(existingDetail("x", 3), existingDetail("x", 2), existingDetail("x", 1));
    when(repo.findByUuid("x")).thenReturn(searchResults);

    ResponseEntity<List<ResourceIdentity>> actual = controller.lookup("x", exchange);

    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody())
        .containsExactlyInAnyOrder(resourceIdentity(3), resourceIdentity(2), resourceIdentity(1));
  }

  @Value
  private static class PredicateAdapter<T> implements ArgumentMatcher<T> {
    PredicateAdapter<T> predicate;

    @Override
    public boolean matches(T argument) {
      return predicate.matches(argument);
    }
  }
}
