package com.server.training.poc.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.server.training.poc.model.dos.FileDO;

@Repository
public interface FileUploadRepository extends MongoRepository<FileDO, UUID> {

	List<FileDO> findAllByUserName(String userName);

}
