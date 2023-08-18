package com.axisrooms.hcr.dto.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "productHistory")
public class ProductHistory {
    @Id
    private String id;
    private Date createdAt=new Date();
    private String oldAccountManagerId;
    private String newAccountManagerId;
    private String updatedBy;
    private String data;
}



