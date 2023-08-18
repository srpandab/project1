package com.axisrooms.hcr.dto.product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "health_check_report_view")
public class HealthCheckReportView {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private boolean active;
    @Column(name = "supplierid")
    private Integer supplierId;
    @Column(name = "suppliername")
    private String supplierName;
    @Column(name = "paymentlock")
    private Boolean paymentLock;
    @Column(name = "pmsname")
    private String pmsName;

    @Column(name = "lastbookingdate")
    private LocalDateTime lastBookingDate;
    @Column(name="lastactivitydate")
    private LocalDateTime lastActivityDate;
    @Column(name = "lastrateupdatedate")
    private LocalDateTime lastRateUpdateDate;
    @Column(name="inventoryenddate")
    private LocalDate inventoryEndDate;
    @Column(name="priceenddate")
    private LocalDate priceEndDate;

    @Column(name = "channels")
    private String channels;

//    @Column(name = "rooms")
//    private String rooms;

    @Column(name = "roomcount")
    private Integer roomCount;
}
