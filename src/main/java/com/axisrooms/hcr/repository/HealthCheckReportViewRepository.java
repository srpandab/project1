package com.axisrooms.hcr.repository;

import com.axisrooms.hcr.dto.product.HealthCheckReportView;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface HealthCheckReportViewRepository extends JpaRepository<HealthCheckReportView, Long> {
    List<HealthCheckReportView> findAllByActive(Boolean active);

    List<HealthCheckReportView> findAllByActiveOrderByLastActivityDateDesc(Boolean active);
    List<HealthCheckReportView> findAllByActiveOrderByLastBookingDateDesc(Boolean active);
    List<HealthCheckReportView> findAllByActiveOrderByLastRateUpdateDateDesc(Boolean active);
    List<HealthCheckReportView> findAllByActiveOrderByInventoryEndDateDesc(Boolean active);
    List<HealthCheckReportView> findAllByActiveOrderByPriceEndDateDesc(Boolean active);

    Page<HealthCheckReportView> findAllByActive(Boolean active, Pageable pageable);

    Page<HealthCheckReportView> findAllBySupplierIdInAndActive(Set<Integer> supplier_ids,Boolean active, Pageable pageable);
    Page<HealthCheckReportView> findAllBySupplierIdInAndActiveAndLastActivityDateBetween(Set<Integer> supplier_ids, Boolean active, LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllBySupplierIdInAndActiveAndLastBookingDateBetween(Set<Integer> supplier_ids, Boolean active, LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllBySupplierIdInAndActiveAndLastRateUpdateDateBetween(Set<Integer> supplier_ids, Boolean active, LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllBySupplierIdInAndActiveAndInventoryEndDateBetween(Set<Integer> supplier_ids, Boolean active, LocalDate startDate,LocalDate endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllBySupplierIdInAndActiveAndPriceEndDateBetween(Set<Integer> supplier_ids,Boolean active,LocalDate startDate,LocalDate endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllBySupplierIdInAndActiveAndPriceEndDateLessThan(Set<Integer> supplier_ids,Boolean active,LocalDate dateTime, Pageable pageable);
    Page<HealthCheckReportView> findAllBySupplierIdInAndActiveAndInventoryEndDateLessThan(Set<Integer> supplier_ids,Boolean active,LocalDate dateTime, Pageable pageable);

    Page<HealthCheckReportView> findAllByActiveAndLastActivityDateBetween(Boolean active, LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllByActiveAndLastBookingDateBetween(Boolean active, LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllByActiveAndLastRateUpdateDateBetween(Boolean active, LocalDateTime startDate,LocalDateTime endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllByActiveAndInventoryEndDateBetween(Boolean active, LocalDate startDate,LocalDate endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllByActiveAndPriceEndDateBetween(Boolean active,LocalDate startDate,LocalDate endDate, Pageable pageable);
    Page<HealthCheckReportView> findAllByActiveAndInventoryEndDateLessThan(Boolean active,LocalDate dateTime, Pageable pageable);
    Page<HealthCheckReportView> findAllByActiveAndPriceEndDateLessThan(Boolean active,LocalDate dateTime, Pageable pageable);

    Page<HealthCheckReportView> findAllByActiveAndLastRateUpdateDateLessThan(Boolean active,LocalDateTime dateTime, Pageable pageable);


}