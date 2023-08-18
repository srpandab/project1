package com.axisrooms.hcr.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.Gson;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@JsonPropertyOrder({
        "totalProperties",
        "totalPage",
        "cmId",
        "hotel",
        "supplier",
        "supplierId",
        "pms",
        "channel",
        "rooms",
//        "booking",
        "managerId",
        "managerName",
        "notes",
        "suppliers",
        "active",
        "isPaymentLockEnabled"

})
@Data
public class HealthCheckReport {
    private static final Logger log = LoggerFactory.getLogger(HealthCheckReport.class);
    public static synchronized HealthCheckReport fromGson(String gSonData) {
        try {
            if (gSonData != null) {
                Gson gson = new Gson();
                return gson.fromJson(gSonData, HealthCheckReport.class);
            }
        } catch (Exception e) {
            log.error("Encountered error"+e.getMessage());
        }
        return null;
    }

    @JsonProperty("cmId")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String cmId;
    @JsonProperty("totalProperties")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private long totalProperties;
    @JsonProperty("totalPage")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private long totalPage;
    @JsonProperty("hotel")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String hotel;
    @JsonProperty("supplier")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String supplier;
    @JsonProperty("supplierId")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private Integer supplierId;
    @JsonProperty("roomCount")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private Integer roomCount;
    @JsonProperty("pms")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String pms;
    @JsonProperty("channel")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private Channel[] channel;
//    @JsonProperty("booking")
//    private List<Booking> booking;
    @JsonProperty("rooms")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private Room[] rooms;
    @JsonProperty("managerId")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String managerId;
    @JsonProperty("managerName")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String managerName;
    @JsonProperty("notes")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private List<Note> notes;
    @JsonProperty("supplierIds")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private Set<Integer> suppliers;
    @JsonProperty("active")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private Boolean active;
    @JsonProperty("isPaymentLockEnabled")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private Boolean isPaymentLockEnabled;
    @JsonProperty("lastRateUpdated")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String lastRateUpdated;
    @JsonProperty("lastActivity")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String lastActivity;
    @JsonProperty("lastBookingCreated")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String lastBookingCreated;

    @JsonProperty("inventoryEndDate")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String inventoryEndDate;
    @JsonProperty("priceEndDate")
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String priceEndDate;
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    @JsonPropertyOrder({
            "roomId",
            "roomName",
            "lastRateUpdated",
            "inventoryEndDate",
            "PriceEndDate"
    })
    @Data
    public static class Room{
        @JsonProperty("roomId")
        @JsonInclude(JsonInclude.Include. NON_NULL)
        private Long roomId;
        @JsonProperty("roomName")
        @JsonInclude(JsonInclude.Include. NON_NULL)
        private String roomName;
        @JsonProperty("lastRateUpdated")
        @JsonInclude(JsonInclude.Include. NON_NULL)
        private String lastRateUpdated;
        @JsonProperty("lastActivity")
        @JsonInclude(JsonInclude.Include. NON_NULL)
        private String lastActivity;
        @JsonProperty("inventoryEndDate")
        @JsonInclude(JsonInclude.Include. NON_NULL)
        private String inventoryEndDate;
        @JsonProperty("priceEndDate")
        @JsonInclude(JsonInclude.Include. NON_NULL)
        private String priceEndDate;
    }

    @JsonPropertyOrder({
            "otaName",
            "status",
            "bookingCount"
    })
    @Data
    public static class Channel{
        @JsonProperty("otaName")
        @JsonInclude(JsonInclude.Include. NON_NULL)
        private String otaName;

        @JsonProperty("status")
        @JsonInclude(JsonInclude.Include. NON_NULL)
        private Boolean status;

        @JsonProperty("bookingCount")
        @JsonInclude(JsonInclude.Include. NON_NULL)
        private int bookingCount;
    }
//
//    @JsonPropertyOrder({
//            "otaName",
//            "count"
//    })
//    public static class Booking{
//        @JsonProperty("otaName")
//        private String otaName;
//
//        @JsonProperty("count")
//        private Integer count;
//
//        public String getOtaName() {
//            return otaName;
//        }
//
//        public void setOtaName(String otaName) {
//            this.otaName = otaName;
//        }
//
//        public Integer getCount() {
//            return count;
//        }
//
//        public void setCount(Integer count) {
//            this.count = count;
//        }
//    }
}
