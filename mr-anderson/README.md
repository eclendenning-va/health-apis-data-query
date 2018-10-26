# mr-anderson - Rest API for CDW

Mr. Anderson provides a simple Rest interface for accessing FHIR related structures from the
Corporate Data Warehouse (CDW). This service protects CDW-internal IDs. The exposed API works
exclusively with public IDs managed by the [Identity Service](../ids/README.md). 

Mr. Anderson blends FHIR-like queries with more raw data access.


#### Queries

```
GET /api/v1/resources/{profile}/{resource}/{resourceVersion}?param=value&param=value
```

- `profile` -
  The FHIR profile determines the _shape_ of the data returned. Supported values are `argonaut`, 
  `dstu2`, and `stu3`
- `resource` -
  The resource type to query, as defined by the profile. For example, `Patient` or
  `AllergyIntolerance`. Resource names follow _CamelCase_.
- `resourceVersion` -
  The resource version defines the set of information and format that is available in results. 
  Versions are used in context with a resource and do not synchronize across resources.
  For example, `1.03` for `Patient` and `1.01` for `Condition` may be used.
  The version is maintained by a database team. Mr. Anderson is not able to determine the versions
  that are available. 
- _parameters_ -
  FHIR query parameters defined for each resource and profile. Note that not all parameters may
  be supported for the version, defer to FHIR specification for parameter definition.
  For example, `/api/v1/resources/argonaut/Patient/1.03?name=Anderson&birthdate=2018` uses 
  parameters that defined in the
  [Patient](http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html)
  spec and supported by the backing stored procedure.


#### Identifiers

Mr. Anderson keeps CDW internal IDs contained. It interacts with Identity Service to provide ID
substitution for input and output. For queries, Mr. Anderson will perform identity look up for
`identifier`, `patient`, and `_id`. For returned documents, Mr. Anderson will perform registration 
and substitution for all `reference` nodes.

Reference nodes are identified as one of the following forms:
- `<cdwId>id</cdwId>`
- `<reference>resource/id</reference>`

Resource Identifier _resources_ are always specified in all uppercase with underscores when 
interacting with the Identity Service, e.g `PATIENT` or `ALLERGY_INTOLLERANCE`. 
For `<cdwId>` nodes, the resource type is defined byt the resource in the query. 
For `<reference>` nodes, the resource type is determined by splitting the value on `/` and
using the first part.


## Behavior


```
GIVEN a known value for one or more of the following id query parameters:
  | identifier |
  | patient    |
  | _id        |
WHEN a request is made 
THEN each instance of an id parameter should be replaced with a CDW id with a value
looked up using the Identity Service before the query is forwarded to CDW.
``` 

```
GIVEN one or more of the following id query parameters:
  | identifier |
  | patient    |
  | _id        |
WHEN a request is made
AND the query parameter value is empty (e.g. patient=)
THEN a 400 response is returned.
```

```
GIVEN an unknown value for one or more of the following id query parameters:
  | identifier |
  | patient    |
  | _id        |
WHEN a request is made
THEN a 404 response is returned.
```

```
GIVEN a query with no parameters specified
WHEN a request is made
THEN a 400 response is returned.
```

```
GIVEN an unknown resource type in the url
WHEN a request is made
THEN a 404 response is returned.
```

```
GIVEN a known value for one or more of the following id query parameters:
  | identifier |
  | patient    |
  | _id        |
WHEN a request is made 
AND the Identity Service encounters a error when performing a lookup or registration
THEN a 500 response is returned.
```

```
GIVEN a valid query
WHEN a request is made
AND the database returns unparseable XML
THEN a 500 response is returned
```

```
GIVEN a valid query
WHEN a request is made
AND the database returns unparseable XML
THEN a 500 response is returned
```

```
GIVEN a valid query
WHEN a request is made
AND the database returns XML with root/errorNumber of -999
THEN a 400 response is returned
```

```
GIVEN a valid query
WHEN a request is made
AND the database returns XML with root/errorNumber of -8
THEN a 400 response is returned
```

```
GIVEN a valid query
WHEN a request is made
AND the database returns XML with root/errorNumber not 0, -8, or -999
THEN a 500 response is returned
```

