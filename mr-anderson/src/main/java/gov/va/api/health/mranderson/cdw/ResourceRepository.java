package gov.va.api.health.mranderson.cdw;

import org.springframework.stereotype.Component;

/** The Resource repository processes queries and returns raw resource data. */
@Component
public interface ResourceRepository {

  /** Return raw XML for the query. */
  String execute(Query query);
}
