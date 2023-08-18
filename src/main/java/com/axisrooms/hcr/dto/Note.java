package com.axisrooms.hcr.dto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "note")
public class Note {
    @Id
    private String id;
    @NotNull(message = "Please provide the User Id.")
    private String userId;
    @NotNull(message = "Please provide the User Id.")
    private String addedBy;
    private String name;
    @NotNull(message = "Please provide the message.")
    private String content;
    private String createdAt;
    private String updatedAt;
}