package gov.va.api.health.argonaut.service.mranderson.client;

public interface MrAndersonClient {

  <T> T search(Query<T> query);

  class BadRequest extends MrAndersonServiceException {
    public BadRequest(Query<?> query) {
      super(query);
    }
  }

  class MrAndersonServiceException extends RuntimeException {
    MrAndersonServiceException(Query<?> query) {
      super(query.toQueryString());
    }
  }

  class NotFound extends MrAndersonServiceException {
    public NotFound(Query<?> query) {
      super(query);
    }
  }

  class SearchFailed extends MrAndersonServiceException {
    public SearchFailed(Query<?> query) {
      super(query);
    }
  }
}
