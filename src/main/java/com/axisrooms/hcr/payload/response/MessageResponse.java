package com.axisrooms.hcr.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageResponse {
  private String message;
  private String status;
  private String token;
  private UserInfoResponse user_details;

  public MessageResponse() {
  }

  public MessageResponse(String message, String status) {
    this.message = message;
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserInfoResponse getUser_details() {
    return user_details;
  }

  public void setUser_details(UserInfoResponse user_details) {
    this.user_details = user_details;
  }
}
