# data-query-tests

Data-query integration tests.

##### Usage

The tests requires a partial security settings file to be defined for `LOCAL` environment
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


###### Lab

To support testing the Lab environment, Sentinel includes a Selenium based robot for working with
OAuth and `id.me` authentication. 

1. The `id.me` robot requires the Selenium Chrome Driver to be installed locally. You'll need to 
   install it. See http://chromedriver.chromium.org/home

2. JUnit categories are used to control which tests Sentinel run. 
    - Most tests do not have a specific category and are intended to run as part of a normal build. 
    - A `Lab` category is assigned to tests intended to run against the lab. 
    - `Lab` tests are disabled by default. To run them, you must enable the `lab` profile. 
      Lab profile disables normal unit and integration tests and enables only `Lab` category tests.

3. Lab configuration is needed to interact with the lab. Sentinel will expect either System 
   properties are specified or you've created a `sentinel/config/lab.properties` file.
   It will complain about missing values if you've omitted. Please see a team member 
   for sensitive information.
