package gov.va.api.health.ids.service.controller.impl;

import gov.va.api.health.ids.api.ResourceIdentity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The mapping between public UUID and system private identifiers.
 *
 * <pre>
 *   CREATE TABLE `resource_identity_detail` (
 *   `pk` int(11) NOT NULL AUTO_INCREMENT,
 *   `identifier` varchar(45) NOT NULL,
 *   `station_identifier` varchar(45) DEFAULT NULL,
 *   `uuid` varchar(45) NOT NULL,
 *   `system` varchar(45) NOT NULL,
 *   `resource` varchar(45) NOT NULL,
 *   PRIMARY KEY (`pk`),
 *   UNIQUE KEY `uuid_UNIQUE` (`uuid`),
 *   UNIQUE KEY `station_urn_UNIQUE` (`station_identifier`,`identifier`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=3353 DEFAULT CHARSET=utf8;
 * </pre>
 */
@Entity
@Table(name = "resource_identity_detail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ResourceIdentityDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  int pk;

  @Column(name = "identifier")
  @Max(45)
  @NotBlank
  String identifier;

  @Column(name = "station_identifier")
  @Max(45)
  String stationIdentifier;

  @Column(name = "uuid")
  @Max(45)
  @NotBlank
  String uuid;

  @Column(name = "system")
  @Max(45)
  @NotBlank
  String system;

  @Column(name = "resource")
  @Max(45)
  @NotBlank
  String resource;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourceIdentityDetail that = (ResourceIdentityDetail) o;
    return pk == that.pk;
  }

  @Override
  public int hashCode() {
    return Objects.hash(pk);
  }

  /** Convert this database entity into a more friendly, non-JPA form. */
  public ResourceIdentity asResourceIdentity() {
    return ResourceIdentity.builder()
        .identifier(identifier())
        .system(system())
        .resource(resource())
        .build();
  }
}
