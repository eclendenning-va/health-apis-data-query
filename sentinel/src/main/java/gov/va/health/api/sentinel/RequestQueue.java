package gov.va.health.api.sentinel;

/** The RequestQueue holds the Queue utilized by the Crawler. */
public interface RequestQueue {

  ServiceDefinition service();

  ExpectedResponse get(String path);

}
