package gov.va.api.health.mranderson.cdw;

import org.springframework.stereotype.Component;

@Component
public interface ResourceRepository {

  String execute(Query query);
}
