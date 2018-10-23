package gov.va.api.health.argonaut.api;

public interface ArgonautService {

  Patient patientRead(String id);

  class ArgonautServiceException extends RuntimeException {
    ArgonautServiceException(String message) {
      super(message);
    }
  }

  class UnknownResource extends ArgonautServiceException {
    public UnknownResource(String id) {
      super(id);
    }
  }

  class SearchFailed extends ArgonautServiceException {
    public SearchFailed(String id, String reason) {
      super(id + " Reason: " + reason);
    }
  }
}
