package com.axisrooms.hcr.dto.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "loginActivity")
public class LoginActivity {
    public LoginActivity(String email, String accountManagerId, String lastLogin) {
        this.email = email;
        this.accountManagerId = accountManagerId;
        this.lastLogin = lastLogin;
    }

    @Id
    private String id;
    @NotNull(message = "Please provide the Email Id.")
    private String email;
    private String accountManagerId;
    private String lastLogin;
}



