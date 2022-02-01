package com.server.training.poc.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.server.training.poc.exception.RequestedItemNotFoundException;
import com.server.training.poc.model.dos.FileDO;
import com.server.training.poc.model.ros.FileRO;

public interface FileUploadService {

	FileDO uploadFile(FileRO fileRo) throws IOException;

	FileDO getFileById(UUID id) throws RequestedItemNotFoundException, IOException;

	List<FileDO> getFilesByUserName(String userName) throws RequestedItemNotFoundException;

}
