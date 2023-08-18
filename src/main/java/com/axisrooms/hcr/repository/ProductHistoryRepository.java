package com.axisrooms.hcr.repository;

import com.axisrooms.hcr.dto.product.ProductHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductHistoryRepository extends MongoRepository<ProductHistory, String> {
}