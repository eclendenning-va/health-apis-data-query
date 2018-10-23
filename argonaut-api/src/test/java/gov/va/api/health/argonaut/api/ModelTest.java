package gov.va.api.health.argonaut.api;

public class ModelTest {

  //  @SneakyThrows
  //  private <T> T roundTrip(T object) {
  //    ObjectMapper mapper = new JacksonConfig().objectMapper();
  //    String json = mapper.writeValueAsString(object);
  //    Object evilTwin = mapper.readValue(json, object.getClass());
  //    assertThat(evilTwin).isEqualTo(object);
  //    return object;
  //  }
  //
  //  private ResourceIdentity id() {
  //    return ResourceIdentity.builder().identifier("i1").resource("r1").system("s1").build();
  //  }
  //
  //  @Test
  //  public void resouceIdentity() {
  //    roundTrip(id());
  //  }
  //
  //  @Test
  //  public void registration() {
  //    roundTrip(Registration.builder().uuid("u1").resourceIdentity(id()).build());
  //  }
  //
  //  @SuppressWarnings("ThrowableNotThrown")
  //  @Test
  //  public void exceptionConstructors() {
  //    new UnknownResource("some id");
  //    new SearchFailed("some id", "some reason");
  //    new RegistrationFailed("some reason");
  //  }
}
