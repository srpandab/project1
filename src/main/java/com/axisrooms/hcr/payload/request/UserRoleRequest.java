package com.axisrooms.hcr.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserRoleRequest {
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> roles;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<String> getRoles() {
    return this.roles;
  }

  public void setRole(Set<String> roles) {
    this.roles = roles;
  }
}
