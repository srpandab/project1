package com.axisrooms.hcr.repository;
import com.axisrooms.hcr.dto.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByUserIdOrderByCreatedAtDesc(String userId);
}
