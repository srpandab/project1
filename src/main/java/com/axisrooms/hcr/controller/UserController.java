package com.axisrooms.hcr.controller;

import com.axisrooms.hcr.dto.user.Role;
import com.axisrooms.hcr.dto.user.User;
import com.axisrooms.hcr.payload.request.LoginRequest;
import com.axisrooms.hcr.payload.request.UserRoleRequest;
import com.axisrooms.hcr.repository.RoleRepository;
import com.axisrooms.hcr.service.UserService;
import com.axisrooms.hcr.util.Recaptcha;
import com.axisrooms.hcr.util.Utils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/api")
@Api(value = "UserController")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    RoleRepository roleRepository;

    @PostMapping(value = "/createAccountManager")
    @CrossOrigin
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAccountManager(@RequestBody User user) {
        return userService.saveAccountManager(user);
    }
    @CrossOrigin
    @GetMapping("/transferAccountManagers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> transferAccountManagers(@RequestParam String fromUser, @RequestParam String toUser, @RequestParam String user) {
        return userService.transferUser(fromUser,toUser,user);
    }

    @PostMapping("/updateUserRole")
//    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> updateSupplierRole(@RequestBody UserRoleRequest request) {
        return userService.updateSupplierRole(request);
    }

    @PostMapping(value = "/updateAccountManager")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
    @GetMapping(value = "/deleteAccountManager")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestParam String accountManagerId) {
        return userService.deleteUser(accountManagerId);
    }

    @GetMapping(value = "/activateAccountManager")
    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateUser(@RequestParam String accountManagerId) {
        return userService.activateUser(accountManagerId);
    }

    @PostMapping(value = "/login")
    @CrossOrigin
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        if(Utils.isPresent(user.getEmail()) && Utils.isPresent(user.getPassword())){
            return userService.loginUserPost(user);
        } else {
            return Recaptcha.accessTokenFailed();
        }
    }
    @GetMapping( "/login")
    @CrossOrigin
    public ResponseEntity<?> authenticateUser(@RequestParam("username") String username, @RequestParam("password") String password) {
//        if (!StringUtils.isEmpty(user.getRecaptcha()) && Recaptcha.validateReCaptcha(user.getRecaptcha())) {
        if(Utils.isPresent(username) && Utils.isPresent(password)){
            LoginRequest loginRequest=new LoginRequest();
            loginRequest.setPassword(password);
            loginRequest.setUsername(username);
            return userService.authenticateUser(loginRequest);
        } else {
            return Recaptcha.accessTokenFailed();
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseEntity<?> logoutUser() {
       return userService.logoutUser();
    }
    @PostMapping("/createRole")
    public ResponseEntity<?> registerUser(@RequestBody Role role) {
        roleRepository.save(role);
        return ResponseEntity.ok("Role added successfully!");
    }
}

