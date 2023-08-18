package com.axisrooms.hcr.payload.response;

import com.axisrooms.hcr.dto.Note;
import com.axisrooms.hcr.dto.user.LoginActivity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {
  private String id;
  private String name;
  private String username;
  private String email;
  private List<String> roles;
  private Set<Integer> suppliers;
  private List<LoginActivity> loginActivity;
  private List<Note> notes;
  public UserInfoResponse() {
  }

  public UserInfoResponse(String id, String name, String username, String email, List<String> roles) {
    this.id = id;
    this.name=name;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public String getName() {
    return name;
  }

  public List<Note> getNotes() {
    return notes;
  }

  public void setNotes(List<Note> notes) {
    this.notes = notes;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<LoginActivity> getLoginActivity() {
    return loginActivity;
  }

  public void setLoginActivity(List<LoginActivity> loginActivity) {
    this.loginActivity = loginActivity;
  }

  public Set<Integer> getSuppliers() {
    return suppliers;
  }

  public void setSuppliers(Set<Integer> suppliers) {
    this.suppliers = suppliers;
  }
}
