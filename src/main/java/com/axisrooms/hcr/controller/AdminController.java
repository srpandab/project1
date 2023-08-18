package com.axisrooms.hcr.controller;

import com.axisrooms.hcr.payload.response.UserInfoResponse;
import com.axisrooms.hcr.security.jwt.JwtUtils;
import com.axisrooms.hcr.service.AdminService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/api")
@Slf4j
@CrossOrigin
@Api(value = "AdminController", description = "REST APIs for onboarding user")
public class AdminController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AdminService adminService;
    @CrossOrigin
    @GetMapping("/getAccountManagers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAccountManagers(HttpServletRequest request, HttpServletResponse response) {
        List<UserInfoResponse> userList = adminService.getAccountManagers();
        return ResponseEntity.ok(userList);
    }
    @CrossOrigin
    @GetMapping("/getInActiveAccountManagers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getInActiveAccountManagers(HttpServletRequest request, HttpServletResponse response) {
        List<UserInfoResponse> userList = adminService.getInActiveAccountManagers();
        return ResponseEntity.ok(userList);
    }

    @CrossOrigin
    @GetMapping("/getAllProperties")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllProperties(@RequestParam(required = false) Integer limit,@RequestParam(required = false) Integer offset, @RequestParam(required = false) String email,
                                              @RequestParam(required = false) String type,@RequestParam(required = false) Integer days,@RequestParam(required = false) Boolean active) {
        return adminService.getAllProperties(limit,offset,email,type,days,active);
    }

}
