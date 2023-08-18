package com.axisrooms.hcr.service;

import com.axisrooms.hcr.dto.product.ProductHistory;
import com.axisrooms.hcr.dto.user.ERole;
import com.axisrooms.hcr.dto.user.LoginActivity;
import com.axisrooms.hcr.dto.user.Role;
import com.axisrooms.hcr.dto.user.User;
import com.axisrooms.hcr.payload.request.LoginRequest;
import com.axisrooms.hcr.payload.request.UserRoleRequest;
import com.axisrooms.hcr.payload.response.MessageResponse;
import com.axisrooms.hcr.payload.response.UserInfoResponse;
import com.axisrooms.hcr.repository.*;
import com.axisrooms.hcr.security.jwt.JwtUtils;
import com.axisrooms.hcr.security.services.UserDetailsImpl;
import com.axisrooms.hcr.util.Utils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.axisrooms.hcr.util.Utils.isNotPresent;
import static com.axisrooms.hcr.util.Utils.isPresent;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private ProductHistoryRepository productHistoryRepository;
    private MessageResponse messageResponse=new MessageResponse();


    public ResponseEntity<?> saveAccountManager(User user) {
        Set<Integer> newSupplierList = new HashSet<>();
        if(isPresent(user) && isPresent(user.getSuppliers()) && user.getSuppliers().size()>0){
            newSupplierList.addAll(user.getSuppliers());
        }
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (isPresent(existingUser)) {
            messageResponse.setStatus("error");
            messageResponse.setMessage("There is already a user registered with the Email provided");
        }else {
            user.setUsername(user.getEmail());
            if(user.getPassword().equals(user.getConfirmPassword())) {
                user.setPassword(encoder.encode(user.getPassword()));
                user.setActive(true);
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByName(ERole.ROLE_USER));
                user.setRoles(roles);
                List<User> userList = userRepository.findAll();
                if (isPresent(userList)) {
                    userList.forEach(oldUser -> removeSupplierFromUser(newSupplierList, oldUser));
                    if (newSupplierList.size() == 0) {
                        user.setSuppliers(null);
                    }else {
                        user.setSuppliers(newSupplierList);
                    }
                    saveUser(user);
                    messageResponse.setStatus("success");
                    messageResponse.setMessage("User successfully Saved");

                } else {
                    saveUser(user);
                    messageResponse.setStatus("success");
                    messageResponse.setMessage("User successfully Saved");
                }
            }else {
                messageResponse.setStatus("failed");
                messageResponse.setMessage("Password and confirm password not matching!");
            }
        }
        return ResponseEntity.ok().body(messageResponse);
    }
    private static void verify(Set<Long> newProductList, User oldUser) {
        if(isPresent(oldUser) && isPresent(oldUser.getSuppliers()) && oldUser.getSuppliers().size()>0){
            newProductList.removeAll(oldUser.getSuppliers());
        }
    }
    private void removeSupplierFromUser(Set<Integer> newProductList, User oldUser) {
        if(isPresent(oldUser) && isPresent(oldUser.getSuppliers()) && oldUser.getSuppliers().size()>0){
            Set<Integer> suppliers = oldUser.getSuppliers();
            suppliers.removeAll(newProductList);
            userRepository.save(oldUser);
//            newProductList.removeAll(oldUser.getSuppliers());
        }
    }
    private static Set<Long> getSupplierList(User newUser, User oldUser) {
        Set<Long> newList=new HashSet<>();
        if(isPresent(newUser) && isPresent(newUser.getSuppliers()) && newUser.getSuppliers().size()>0){
            newUser.getSuppliers();
        }
        if(isPresent(oldUser) && isPresent(oldUser.getSuppliers()) && oldUser.getSuppliers().size()>0){
            newList.removeAll(oldUser.getSuppliers());
        }
        return newList;
    }

    public ResponseEntity<?> transferUser(String oldUser, String newUser,String updatedBy) {
        ProductHistory productHistory=new ProductHistory();

        try {
            if(Utils.isPresent(oldUser) && Utils.isPresent(newUser) && Utils.isPresent(updatedBy)){
                Optional<User> oldOptional = userRepository.findById(oldUser);
                Optional<User> newOptional = userRepository.findById(newUser);
                if(Utils.isPresent(oldOptional) && Utils.isPresent(newOptional)){
                    User fromUser=oldOptional.get();
                    User toUser=newOptional.get();
                    if(isPresent(toUser.getSuppliers())) {
                        toUser.getSuppliers().addAll(fromUser.getSuppliers());
                        fromUser.setSuppliers(null);
                    }else {
                        toUser.setSuppliers(fromUser.getSuppliers());
                        fromUser.setSuppliers(null);
                    }
                    saveUser(toUser);
                    saveUser(fromUser);
                    productHistory.setData(new Gson().toJson(fromUser));
                    messageResponse.setStatus("success");
                    messageResponse.setMessage("User Transfer successfully");
                    productHistory.setOldAccountManagerId(oldUser);
                    productHistory.setNewAccountManagerId(newUser);
                    productHistory.setUpdatedBy(updatedBy);
                    productHistoryRepository.save(productHistory);
                }else {
                    messageResponse.setStatus("failed");
                    messageResponse.setMessage("User Not found");
                }
            }else {
                messageResponse.setStatus("failed");
                messageResponse.setMessage("User Id's missing");
            }
        }catch (Exception ex){
            messageResponse.setStatus("failed");
            messageResponse.setMessage(ex.getMessage());
        }
        return ResponseEntity.ok().body(messageResponse);
    }
    public ResponseEntity<?> updateSupplierRole(UserRoleRequest user) {

        if (isPresent(user.getEmail()) && isPresent(user.getRoles())) {
            User existing = userRepository.findByEmail(user.getEmail());
                if(isPresent(existing)) {
                    Set<String> userRoles = user.getRoles();
                    Set<Role> roles = new HashSet<>();
                    if (userRoles == null) {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER);
                        roles.add(userRole);
                    } else {
                        userRoles.forEach(role -> {
                            if (role.equals("ROLE_ADMIN")) {
                                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
                                roles.add(adminRole);
                            } else {
                                Role userRole = roleRepository.findByName(ERole.ROLE_USER);
                                roles.add(userRole);
                            }
                        });
                    }
                    existing.setRoles(roles);
                    if (saveUser(existing)) {
                        messageResponse.setStatus("success");
                        messageResponse.setMessage("User Role has been Updated successfully");
                    } else {
                        messageResponse.setStatus("error");
                        messageResponse.setMessage("Something went wrong please contact support team.");
                    }
                }else {
                    messageResponse.setStatus("error");
                    messageResponse.setMessage("Please verify your account");
                }
        }else {
            messageResponse.setStatus("error");
            messageResponse.setMessage("Username or roles might be empty");
        }
        return ResponseEntity.ok().body(messageResponse);
    }
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return getLoginResponseWithAuthenticate(loginRequest.getUsername(), loginRequest.getPassword());
    }
    public ResponseEntity<?> loginUserPost(User user) {
        return getLoginResponseWithAuthenticate(user.getEmail(), user.getPassword());
    }

    private ResponseEntity<MessageResponse> getLoginResponseWithAuthenticate(String userName,String password) {
        User user = userRepository.findByEmail(userName);
        if(isNotPresent(user)){
            messageResponse.setStatus("error");
            messageResponse.setMessage("User Not Found");
            return ResponseEntity.ok().body(messageResponse);
        }else {
            if(user.isActive()){
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
                MessageResponse messageResponse = getMessageResponse(userDetails);
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(messageResponse);
            }else {
                messageResponse.setStatus("error");
                messageResponse.setMessage("User InActive, please contact admin for Activation");
                return ResponseEntity.ok().body(messageResponse);
            }
        }
    }

    private MessageResponse getMessageResponse(UserDetailsImpl userDetails) {

        User existing = userRepository.findByEmail(userDetails.getEmail());
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        messageResponse.setUser_details(new UserInfoResponse(userDetails.getId(), userDetails.getName(), userDetails.getUsername(),
                userDetails.getEmail(), roles));
        messageResponse.setToken(jwtUtils.generateTokenFromUsername(userDetails.getEmail()));
        messageResponse.setMessage("User LoggedIn successfully");
        messageResponse.setStatus("success");
        saveLogin(new LoginActivity(existing.getEmail(),existing.getId(),new DateTime().toString()));
        return messageResponse;
    }

    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("You've been signed out!","success"));
    }
    public boolean saveUser(User user) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Encountered error: {}", e.getMessage(), e);
            return false;
        }
    }
    public void saveLogin(LoginActivity loginActivity) {
        try {
            loginRepository.save(loginActivity);
        } catch (Exception e) {
            log.error("Encountered error: {}", e.getMessage(), e);
        }
    }

    public ResponseEntity<?> updateUser(User user) {

        Set<Integer> newSupplierList = new HashSet<>();
        if (isPresent(user.getEmail())) {
            User existing = userRepository.findByEmail(user.getEmail());
            if(isPresent(existing)) {
                if(isPresent(user) && isPresent(user.getSuppliers()) && user.getSuppliers().size()>0){
                    newSupplierList.addAll(user.getSuppliers());
                    List<User> userList = userRepository.findAll();
                    if(isPresent(userList)) {
                        userList.forEach(oldUser -> removeSupplierFromUser(newSupplierList, oldUser));
                    }
                        if (isPresent(existing.getSuppliers()) && isPresent(user.getSuppliers())) {
                            if(existing.getSuppliers().size()>user.getSuppliers().size()){
                                newSupplierList.addAll(user.getSuppliers());
                            }else {
                                newSupplierList.addAll(existing.getSuppliers());
                            }
                        }
                        if(isPresent(newSupplierList) && newSupplierList.size()>0) {
                            existing.setSuppliers(newSupplierList);
                        }
                        if(isPresent(user.getName())) {
                            existing.setName(user.getName());
                        }
                        if(isPresent(user.getPassword()) && isPresent(user.getConfirmPassword())) {
                            if(user.getPassword().equals(user.getConfirmPassword())) {
                                existing.setPassword(encoder.encode(user.getPassword()));
                                if (saveUser(existing)) {
                                    messageResponse.setStatus("success");
                                    messageResponse.setMessage("User has been Updated successfully");
                                } else {
                                    messageResponse.setStatus("error");
                                    messageResponse.setMessage("Something went wrong please contact support team.");
                                }
                            }else {
                                messageResponse.setStatus("failed");
                                messageResponse.setMessage("Password and confirm password not matching!");
                            }
                        }else {
                            if (saveUser(existing)) {
                                messageResponse.setStatus("success");
                                messageResponse.setMessage("User has been Updated successfully");
                            } else {
                                messageResponse.setStatus("error");
                                messageResponse.setMessage("Something went wrong please contact support team.");
                            }
                        }
                }else {
                    if(isNotPresent(user.getSuppliers())){
                        existing.setSuppliers(null);
                    }
                    if(isPresent(user.getName())) {
                        existing.setName(user.getName());
                    }
                    if(isPresent(user.getPassword()) && isPresent(user.getConfirmPassword())) {
                        if(user.getPassword().equals(user.getConfirmPassword())) {
                            existing.setPassword(encoder.encode(user.getPassword()));
                            if (saveUser(existing)) {
                                messageResponse.setStatus("success");
                                messageResponse.setMessage("User has been Updated successfully");
                            } else {
                                messageResponse.setStatus("error");
                                messageResponse.setMessage("Something went wrong please contact support team.");
                            }
                        }else {
                            messageResponse.setStatus("failed");
                            messageResponse.setMessage("Password and confirm password not matching!");
                        }
                    }else {
                        if (saveUser(existing)) {
                            messageResponse.setStatus("success");
                            messageResponse.setMessage("User has been Updated successfully");
                        } else {
                            messageResponse.setStatus("error");
                            messageResponse.setMessage("Something went wrong please contact support team.");
                        }
                    }
                }
            }else {
                messageResponse.setStatus("error");
                messageResponse.setMessage("User Not Found");
            }
        }else {
            messageResponse.setStatus("error");
            messageResponse.setMessage("Please provide valid Email");
        }
        return ResponseEntity.ok().body(messageResponse);
    }
    public ResponseEntity<?> deleteUser(String accountManagerId) {
        Optional<User> existing = userRepository.findById(accountManagerId);
        if(isPresent(existing)) {
            User user=existing.get();
            user.setActive(false);
            userRepository.save(user);
            messageResponse.setStatus("success");
            messageResponse.setMessage("User successfully De-activated");
        }else {
            messageResponse.setStatus("failed");
            messageResponse.setMessage("User not found");
        }
        return ResponseEntity.ok().body(messageResponse);
    }
    public ResponseEntity<?> activateUser(String accountManagerId) {
        Optional<User> existing = userRepository.findById(accountManagerId);
        if(existing.isPresent()) {
            User user=existing.get();
            user.setActive(true);
            userRepository.save(user);
            messageResponse.setStatus("success");
            messageResponse.setMessage("User Successfully Activated");
        }else {
            messageResponse.setStatus("failed");
            messageResponse.setMessage("User not found");
        }
        return ResponseEntity.ok().body(messageResponse);
    }
}

