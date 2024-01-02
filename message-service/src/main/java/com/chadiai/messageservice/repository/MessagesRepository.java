package com.chadiai.messageservice.repository;

import com.chadiai.messageservice.model.Messages;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface MessagesRepository extends MongoRepository<Messages, String> {
    Optional<Messages> findByParticipantsIn(Collection<List<Integer>> participants);
}