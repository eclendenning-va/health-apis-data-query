# sentinel

The Sentinel is a set of acceptance tests that can be used to verify the correctness of a system.


## Framework
##### Key Concepts
- The `System` is a collection of services in a particular environment. 
  - Our services are _Argonaut_, _Mr. Anderson_, _Identity Service_.
  - Our environments are _Local_, _QA_, _Staging_, _Production_, and _Stand by_.
- A `Service Definition` defines the particulars for interacting with a given service, such as base
  URL and port.
- A `System Definition` defines a set of `Service Definitions` for a given environment.
- A `Test Client` is a Java interface that specializes with REST interaction for a specific
  service. `Test Clients` are grouped for a system.


##### Usage

The `Sentinel` provides the entry point for obtaining a set of test clients. Using the system
property `sentinel`, it determines with environment should be accessed. 
Supported `sentinel` values are `LOCAL`, `QA`, `STAGING`, `PROD`, and `STANDBY`. 
For example, 

```
mvn install -Dsentinel=LOCAL
```

The `Sentinel` requires a partial security settings file to be defined for `LOCAL` environment
The `config/secrets.properties` file must be created and define passwords used with the
_DVP-DVP-NONPROD_ keystore and truststores. 
See the template [secrets.properties](src/test/config/secrets.properties) for requried values.

###### Interactive local system
The applications can be started for ad-hoc, interactive testing.

```
mvn pre-integration-test -Dsentinel=LOCAL -Dexec.waitForInterrupt=true
```

###### Local databases

When ran in `LOCAL` mode, `ids` is started with an empty in-memory H2 database and `mr-anderson`
is started with a [mock database](mock-database.md).


##### Java API
To make a service request, use the `Sentinel` to create test clients for the configured environment.

```
List<ResourceIdentity> identities = Sentinel.get()
  .clients()
  .ids()
  .get("/api/v1/ids/{id}", "12345")
  .expect(200)
  .expectListOf(ResourceIdentity.class)

// validate identities
```

