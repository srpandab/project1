package com.axisrooms.hcr.dto.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user")
public class User {
    @Id
    private String id;
    
    private String name;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    private String username;
    @NotNull(message = "Please provide the Email Id.")
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    private String email;
    
    private String password;

    private String confirmPassword;
    @Builder.Default
    private Date createdAt=new Date();
    @Builder.Default
    private boolean active=false;

    private String accountManagerId;
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    private Set<Integer> suppliers;
}



