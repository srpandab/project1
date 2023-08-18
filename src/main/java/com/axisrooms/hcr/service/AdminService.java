package com.axisrooms.hcr.service;

import com.axisrooms.hcr.dto.HealthCheckReport;
import com.axisrooms.hcr.dto.Note;
import com.axisrooms.hcr.dto.product.HealthCheckReportView;
import com.axisrooms.hcr.dto.user.ERole;
import com.axisrooms.hcr.dto.user.Role;
import com.axisrooms.hcr.dto.user.User;
import com.axisrooms.hcr.payload.response.UserInfoResponse;
import com.axisrooms.hcr.repository.*;
import com.axisrooms.hcr.util.Utils;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private HealthCheckReportViewRepository viewRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    private User user;
    private List<User> userList;
    private LocalDateTime startDateTime=LocalDateTime.now();
    private LocalDateTime todayDateTime=LocalDateTime.now();
    private LocalDate todayDate=LocalDate.now();
    private LocalDate endDate=LocalDate.now();
    private Integer days;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<UserInfoResponse> getAccountManagers() {
        Set<Role>roles=new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER));
        List<UserInfoResponse> userDetailsList=new ArrayList<>();
        List<User> users = userRepository.findByActiveAndRoles(true, roles);
        return getUserInfoResponses(userDetailsList, users);
    }
    public List<UserInfoResponse> getInActiveAccountManagers() {
        Set<Role>roles=new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER));
        List<UserInfoResponse> userDetailsList=new ArrayList<>();
        List<User> users = userRepository.findByActiveAndRoles(false, roles);
        return getUserInfoResponses(userDetailsList, users);
    }

    private List<UserInfoResponse> getUserInfoResponses(List<UserInfoResponse> userDetailsList, List<User> users) {
        users.forEach(user -> {
            UserInfoResponse userInfo = new UserInfoResponse();
            userInfo.setEmail(user.getEmail());
            userInfo.setName(user.getName());
            userInfo.setId(user.getId());
            userInfo.setSuppliers(user.getSuppliers());
            List<Note> notes=noteRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
            if(Utils.isPresent(notes)) {
                userInfo.setNotes(notes);
            }
            userInfo.setLoginActivity(loginRepository.findFirst10ByEmailOrderByLastLoginDesc(user.getEmail()));
            userDetailsList.add(userInfo);
        });
        return userDetailsList;
    }
    public ResponseEntity<?> getAllProperties(Integer limit,Integer offSet,String email,String type,Integer days,Boolean active) {
        if(Utils.isPresent(days)){
            if (!(days == -1)) {
                startDateTime = startDateTime.minusDays(days);
                endDate = endDate.plusDays(days);
            }
            this.days=days;
        }
        long count=0;
        List<HealthCheckReportView> allViews = null;
        try {
            if (Utils.isPresent(limit) && Utils.isPresent(offSet)) {
                Pageable paging = getPagingWithSorting(offSet, limit,type);
                Page<HealthCheckReportView> pages = null;
                if (Utils.isPresent(email)) {
                    User user = userRepository.findByEmail(email);
                    if (Utils.isPresent(user) && Utils.isPresent(user.getSuppliers())) {
                        this.setUser(user);
                        Set<Integer> supplierList = user.getSuppliers();
                        if (Utils.isPresent(type)) {
                            switch (type) {
                                case "lastactivitydate": {
                                    pages = viewRepository.findAllBySupplierIdInAndActiveAndLastActivityDateBetween(supplierList, active, this.startDateTime, this.todayDateTime, paging);
                                    break;
                                }
                                case "lastBookingDate": {
                                    pages = viewRepository.findAllBySupplierIdInAndActiveAndLastBookingDateBetween(supplierList, active, this.startDateTime, this.todayDateTime, paging);
                                    break;
                                }
                                case "lastRateUpdateDate": {
                                    pages = viewRepository.findAllBySupplierIdInAndActiveAndLastRateUpdateDateBetween(supplierList, active, this.startDateTime, this.todayDateTime, paging);
                                    break;
                                }
                                case "inventoryEndDate": {
                                    if (Utils.isPresent(days) && days == -1) {
                                        pages = viewRepository.findAllBySupplierIdInAndActiveAndInventoryEndDateLessThan(supplierList, active,this.todayDate, paging);
                                    } else {
                                        pages = viewRepository.findAllBySupplierIdInAndActiveAndInventoryEndDateBetween(supplierList, active, this.todayDate, this.endDate, paging);
                                    }
                                    break;
                                }
                                case "priceEndDate": {
                                    if (Utils.isPresent(days) && days == -1) {
                                        pages = viewRepository.findAllBySupplierIdInAndActiveAndPriceEndDateLessThan(supplierList, active,this.todayDate, paging);
                                    } else {
                                        pages = viewRepository.findAllBySupplierIdInAndActiveAndPriceEndDateBetween(supplierList, active, this.todayDate , this.endDate, paging);
                                    }
                                    break;
                                }
                            }
                        } else {
                            pages = viewRepository.findAllBySupplierIdInAndActive(supplierList, active, paging);
                        }
                    }
                } else {
                    pages = getHealthCheckReportViewsWithoutUser(active, type,paging);
                }
                assert pages != null;
                count = pages.getTotalElements();
                allViews = pages.getContent();
            } else {
                if (Utils.isPresent(type)) {
                    switch (type) {
                        case "lastactivitydate": {
                            allViews = viewRepository.findAllByActiveOrderByLastActivityDateDesc(active);
                            break;
                        }
                        case "lastBookingDate":
                            allViews = viewRepository.findAllByActiveOrderByLastBookingDateDesc(active);
                            break;
                        case "lastRateUpdateDate":
                            allViews = viewRepository.findAllByActiveOrderByLastRateUpdateDateDesc(active);
                            break;
                        case "inventoryEndDate":
                            allViews = viewRepository.findAllByActiveOrderByInventoryEndDateDesc(active);
                            break;
                        case "priceEndDate":
                            allViews = viewRepository.findAllByActiveOrderByPriceEndDateDesc(active);
                            break;
                        case "lastRateNotUpdateDate": {
                            allViews = viewRepository.findAllByActiveOrderByLastRateUpdateDateDesc(active);
                            break;
                        }
                    }
                } else {
                    allViews = viewRepository.findAllByActive(active);
                }
                count = allViews.size();
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        List<HealthCheckReport> healthCheckReportList = getHealthCheckReports(count, allViews);
        return ResponseEntity.ok().body(healthCheckReportList);
    }

    private Page<HealthCheckReportView> getHealthCheckReportViewsWithoutUser(Boolean active,String type,Pageable paging) {
        Page<HealthCheckReportView> pages = null;
        this.setUserList(userRepository.findAll());
        if (Utils.isPresent(active)) {
            if (Utils.isPresent(type)) {
                switch (type) {
                    case "lastactivitydate": {
                        pages = viewRepository.findAllByActiveAndLastActivityDateBetween(active, this.startDateTime, this.todayDateTime, paging);
                        break;
                    }
                    case "lastBookingDate": {
                        pages = viewRepository.findAllByActiveAndLastBookingDateBetween(active, this.startDateTime, this.todayDateTime, paging);
                        break;
                    }
                    case "lastRateUpdateDate": {
                        pages = viewRepository.findAllByActiveAndLastRateUpdateDateBetween(active, this.startDateTime, this.todayDateTime, paging);
                        break;
                    }
                    case "inventoryEndDate": {
                        if (Utils.isPresent(days) && days == -1) {
                            pages = viewRepository.findAllByActiveAndInventoryEndDateLessThan(active,this.todayDate, paging);
                        } else {
                            pages = viewRepository.findAllByActiveAndInventoryEndDateBetween(active, this.todayDate, this.endDate, paging);
                        }
                        break;
                    }
                    case "priceEndDate": {
                        if (Utils.isPresent(days) && days == -1) {
                            pages = viewRepository.findAllByActiveAndPriceEndDateLessThan(active,this.todayDate, paging);
                        } else {
                            pages = viewRepository.findAllByActiveAndPriceEndDateBetween(active,this.todayDate,this.endDate, paging);
                        }
                        break;
                    }
                }
            } else {
                pages = viewRepository.findAllByActive(active, paging);
            }
        } else {
            pages = viewRepository.findAll(paging);
        }
        return pages;
    }

    public Pageable getPagingWithSorting(int pageNumber, int pageSize, String type) {
        Pageable paging=null;
        if(Utils.isPresent(type)){
            Sort sortByName = Sort.by(Sort.Direction.DESC, type);
            paging = PageRequest.of(pageNumber, pageSize,sortByName);
        }else {
            paging = PageRequest.of(pageNumber, pageSize);
        }
        return paging;
    }

    private List<HealthCheckReport> getHealthCheckReports(long count, List<HealthCheckReportView> allViews) {
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        List<HealthCheckReport> healthCheckReportList = new ArrayList<>();
        if(Utils.isPresent(allViews)) {
            allViews.forEach(view -> {
                HealthCheckReport healthCheckReport = new HealthCheckReport();
                healthCheckReport.setTotalProperties(count);
                healthCheckReport.setCmId(String.valueOf(view.getId()));
                healthCheckReport.setActive(view.isActive());
                healthCheckReport.setSupplierId(view.getSupplierId());
                if (Utils.isPresent(view.getSupplierName())) {
                    healthCheckReport.setSupplier(view.getSupplierName());
                }
                if (Utils.isPresent(view.getPmsName())) {
                    healthCheckReport.setPms(view.getPmsName());
                }
                if (Utils.isPresent(view.getName())) {
                    healthCheckReport.setHotel(view.getName());
                }
                if (Utils.isPresent(view.getPaymentLock())) {
                    healthCheckReport.setIsPaymentLockEnabled(view.getPaymentLock());
                }
                if (Utils.isPresent(view.getLastActivityDate())) {
                    healthCheckReport.setLastActivity(view.getLastActivityDate()+"");
                }
                if (Utils.isPresent(view.getLastRateUpdateDate())) {
                    healthCheckReport.setLastRateUpdated(view.getLastRateUpdateDate()+"");
                }
                if (Utils.isPresent(view.getLastBookingDate())) {
                    healthCheckReport.setLastBookingCreated(view.getLastBookingDate()+"");
                }
                if (Utils.isPresent(view.getInventoryEndDate())) {
                    healthCheckReport.setInventoryEndDate(view.getInventoryEndDate()+"");
                }
                if (Utils.isPresent(view.getPriceEndDate())) {
                    healthCheckReport.setPriceEndDate(view.getPriceEndDate()+"");
                }
                try {
                    if (Utils.isPresent(view.getChannels())) {
                        HealthCheckReport.Channel[] channelsArray = objectMapper.readValue(view.getChannels(), HealthCheckReport.Channel[].class);
                        if(Utils.isPresent(channelsArray)) {
                            healthCheckReport.setChannel(channelsArray);
                        }
                    }
//                    if (Utils.isPresent(view.getRooms())) {
//                        HealthCheckReport.Room[] roomsArray = objectMapper.readValue(view.getRooms(), HealthCheckReport.Room[].class);
//                        if(Utils.isPresent(roomsArray)) {
//                            healthCheckReport.setRooms(roomsArray);
//                        }
//                    }
                    if (Utils.isPresent(view.getRoomCount())) {
                    healthCheckReport.setRoomCount(view.getRoomCount());
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
                if (Utils.isPresent(healthCheckReport.getSupplierId())) {
                    getAccountManager(healthCheckReport);
                }
                healthCheckReportList.add(healthCheckReport);
            });
        }
        return healthCheckReportList;
    }
    private void getAccountManager(HealthCheckReport healthCheckReport) {
        if(Utils.isPresent(this.getUser())) {
            getUser(this.getUser(),healthCheckReport);
        }else {
            List<User> userList = this.getUserList();
            userList.forEach(user -> getUser(user, healthCheckReport));
        }
    }

    private void getUser(User user, HealthCheckReport healthCheckReport) {
        if (Utils.isPresent(user.getSuppliers())) {
            user.getSuppliers().forEach(supplierId -> {
                if (Utils.isPresent(healthCheckReport.getSupplierId())) {
                    if (healthCheckReport.getSupplierId().intValue()==(supplierId)) {
                        List<Note> notes = noteRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
                        if (Utils.isPresent(notes)) {
                            healthCheckReport.setNotes(notes);
                        }
                        healthCheckReport.setManagerId(user.getId());
                        healthCheckReport.setManagerName(user.getName());
                        healthCheckReport.setSuppliers(user.getSuppliers());
                    }
                }
            });
        }
    }
}
