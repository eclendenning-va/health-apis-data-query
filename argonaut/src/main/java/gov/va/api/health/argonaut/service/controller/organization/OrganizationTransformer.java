package gov.va.api.health.argonaut.service.controller.organization;

import gov.va.api.health.argonaut.api.resources.Organization;
import gov.va.dvp.cdw.xsd.model.CdwOrganization101Root.CdwOrganizations.CdwOrganization;
import org.springframework.stereotype.Service;

@Service
public class OrganizationTransformer implements OrganizationController.Transformer {

  @Override
  public Organization apply(CdwOrganization organization) {
    return null;
  }
}
