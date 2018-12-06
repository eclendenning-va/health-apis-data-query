package gov.va.api.health.argonaut.service.controller.organization;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.datatypes.Address;
import gov.va.api.health.argonaut.api.datatypes.CodeableConcept;
import gov.va.api.health.argonaut.api.datatypes.Coding;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint.ContactPointSystem;
import gov.va.api.health.argonaut.api.datatypes.ContactPoint.ContactPointUse;
import gov.va.api.health.argonaut.api.resources.Organization;
import gov.va.dvp.cdw.xsd.model.CdwOrganization100Root.CdwOrganizations.CdwOrganization;
import gov.va.dvp.cdw.xsd.model.CdwOrganization100Root.CdwOrganizations.CdwOrganization.CdwAddresses;
import gov.va.dvp.cdw.xsd.model.CdwOrganization100Root.CdwOrganizations.CdwOrganization.CdwTelecoms;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationAddress;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationAddress.CdwLines;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationTelecom;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationTelecomUse;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationType;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationType.CdwCoding;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationTypeCode;
import gov.va.dvp.cdw.xsd.model.CdwOrganizationTypeDisplay;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class OrganizationTransformerTest {

  private final XmlSampleData cdw = new XmlSampleData();
  private final Expected expected = new Expected();
  private final OrganizationTransformer tx = new OrganizationTransformer();

  @Test
  public void organization() {
    assertThat(tx.apply(cdw.organization())).isEqualTo(expected.organization());
  }

  @Test
  public void addresses() {
    assertThat(tx.addresses(null)).isNull();
    assertThat(tx.addresses(new CdwAddresses())).isNull();
    assertThat(tx.addresses(cdw.addresses())).isEqualTo(expected.addresses());
  }

  @Test
  public void addressLine() {
    assertThat(tx.addressLine(null)).isNull();
    assertThat(tx.addressLine(new CdwOrganizationAddress())).isNull();
    assertThat(tx.addressLine(cdw.address())).isEqualTo(expected.lines());
  }

  @Test
  public void telecoms() {
    assertThat(tx.telecoms(null)).isNull();
    assertThat(tx.telecoms(Collections.singletonList(new CdwOrganizationTelecom()))).isNull();
    assertThat(tx.telecoms(cdw.telecoms().getTelecom())).isEqualTo(expected.telecoms());
  }

  @Test
  public void type() {
    assertThat(tx.type(null)).isNull();
    assertThat(tx.type(new CdwOrganizationType())).isNull();
    assertThat(tx.type(cdw.type())).isEqualTo(expected.type());
  }

  @Test
  public void coding() {
    assertThat(tx.codings(null)).isNull();
    assertThat(tx.codings(new CdwCoding())).isNull();
    assertThat(tx.codings(cdw.typeCoding())).isEqualTo(expected.typeCoding());
  }

  private static class XmlSampleData {
    private CdwOrganization organization() {
      CdwOrganization cdw = new CdwOrganization();
      cdw.setCdwId("11666404-1b3e-50ed-a7a6-4acc7b1caec6");
      cdw.setActive(true);
      cdw.setType(type());
      cdw.setName("TEXAS HEALTH CHOICE");
      cdw.setTelecoms(telecoms());
      cdw.setAddresses(addresses());
      return cdw;
    }

    private CdwAddresses addresses() {
      CdwAddresses cdw = new CdwAddresses();
      cdw.getAddress().add(address());
      return cdw;
    }

    private CdwOrganizationAddress address() {
      CdwOrganizationAddress cdw = new CdwOrganizationAddress();
      CdwLines lines = new CdwLines();
      cdw.setLines(lines);
      cdw.getLines().getLine().add("GTPA");
      cdw.getLines().getLine().add("3200 SOUTHWEST FREEWAY");
      cdw.setCity("HOUSTON");
      cdw.setState("TX");
      cdw.setPostalCode("77027");
      return cdw;
    }

    private CdwTelecoms telecoms() {
      CdwTelecoms cdw = new CdwTelecoms();
      cdw.getTelecom().add(telecom());
      return cdw;
    }

    private CdwOrganizationTelecom telecom() {
      CdwOrganizationTelecom cdw = new CdwOrganizationTelecom();
      cdw.setSystem("phone");
      cdw.setValue("(800)466-8397");
      cdw.setUse(CdwOrganizationTelecomUse.WORK);
      return cdw;
    }

    private CdwOrganizationType type() {
      CdwOrganizationType cdw = new CdwOrganizationType();
      cdw.setCoding(typeCoding());
      return cdw;
    }

    private CdwCoding typeCoding() {
      CdwCoding cdw = new CdwCoding();
      cdw.setSystem("http://hl7.org/fhir/organization-type");
      cdw.setCode(CdwOrganizationTypeCode.INS);
      cdw.setDisplay(CdwOrganizationTypeDisplay.INSURANCE_COMPANY);
      return cdw;
    }
  }

  private static class Expected {
    private Organization organization() {
      return Organization.builder()
          .id("11666404-1b3e-50ed-a7a6-4acc7b1caec6")
          .active(true)
          .type(type())
          .name("TEXAS HEALTH CHOICE")
          .telecom(telecoms())
          .address(addresses())
          .build();
    }

    private List<Address> addresses() {
      return Collections.singletonList(
          Address.builder().line(lines()).city("HOUSTON").state("TX").postalCode("77027").build());
    }

    private List<String> lines() {
      return Arrays.asList("GTPA", "3200 SOUTHWEST FREEWAY");
    }

    private List<ContactPoint> telecoms() {
      return Collections.singletonList(
          ContactPoint.builder()
              .system(ContactPointSystem.phone)
              .value("(800)466-8397")
              .use(ContactPointUse.work)
              .build());
    }

    private CodeableConcept type() {
      return CodeableConcept.builder().coding(typeCoding()).build();
    }

    private List<Coding> typeCoding() {
      return Collections.singletonList(
          Coding.builder()
              .system("http://hl7.org/fhir/organization-type")
              .code("ins")
              .display("Insurance Company")
              .build());
    }
  }
}
