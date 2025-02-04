package gov.va.api.health.dataquery.service.controller.patient;

import gov.va.api.health.dataquery.service.controller.datamart.DatamartEntity;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@Entity
@Builder
@Table(name = "PatientSearch", schema = "app")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PatientSearchEntity implements DatamartEntity {
  @Id
  @Column(name = "fullIcn")
  @EqualsAndHashCode.Include
  private String icn;

  @Column(name = "firstName")
  private String firstName;

  @Column(name = "lastName")
  private String lastName;

  @Column(name = "name")
  private String name;

  @Column(name = "gender")
  private String gender;

  @Column(name = "birthDateTime")
  private Instant birthDateTime;

  @OneToOne
  @JoinColumn(name = "fullIcn", referencedColumnName = "PatientFullIcn")
  private PatientEntity patient;

  static Sort naturalOrder() {
    return Sort.by("icn").ascending();
  }

  DatamartPatient asDatamartPatient() {
    return patient.asDatamartPatient();
  }

  @Override
  public String cdwId() {
    return icn();
  }
}
