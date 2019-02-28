# sentinel

Sentinel is a library to support acceptance testing to verify the correctness of a system.

##### Key Concepts
- A `Service Definition` defines the particulars for interacting with a given service, such as base
  URL and port.
- A `System Definition` defines a set of `Service Definitions` for a given environment.
- A supported `Environment` is one of _Lab_, _Local_, _Production_, _QA_, or _Staging_.
     - Use the system property `sentinel` to set the active environment, e.g. `-Dsentinel=LAB`
- A `Test Client` is a Java interface that specializes with REST interaction for a specific `Service Definition`.
