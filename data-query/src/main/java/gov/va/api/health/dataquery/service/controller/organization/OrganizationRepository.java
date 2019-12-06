package gov.va.api.health.dataquery.service.controller.organization;

import gov.va.api.health.autoconfig.logging.Loggable;
import gov.va.api.health.dataquery.service.controller.observation.ObservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

@Loggable
public interface OrganizationRepository extends PagingAndSortingRepository<ObservationEntity, String> {
//  Page<ObservationEntity> findByIcn(String icn, Pageable pageable);
//
//  Page<ObservationEntity> findByIcnAndCode(String icn, String code, Pageable pageable);
//
//  Page<ObservationEntity> findByIcnAndCategoryAndDate(String icn, String category, String date, Pageable pageable);
}
