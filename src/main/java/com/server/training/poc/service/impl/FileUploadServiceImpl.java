package com.server.training.poc.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.server.training.poc.common.PocConstants;
import com.server.training.poc.exception.RequestedItemNotFoundException;
import com.server.training.poc.model.dos.FileDO;
import com.server.training.poc.model.ros.FileRO;
import com.server.training.poc.repository.FileUploadRepository;
import com.server.training.poc.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadServiceImpl.class);

	@Autowired
	private FileUploadRepository fileUploadRepository;
	
	@Value("${file.upload.dir}")
	private String dirPath;

	@Override
	public FileDO uploadFile(FileRO fileRo) throws IOException {
		
		FileDO fileDo = populateFileDo(fileRo);
		writeMultipartToFile(fileRo.getFile(), dirPath, fileDo.getFileName());
		return fileUploadRepository.save(fileDo);
	}

	private FileDO populateFileDo(FileRO fileRo) {
		FileDO fileDo = new FileDO();
		fileDo.setId(UUID.randomUUID());
		String fileName = new StringBuilder(fileDo.getId().toString()).append(PocConstants.UNDERSCORE).append(fileRo.getFile().getOriginalFilename()).toString();
		fileDo.setFileName(fileName);
		fileDo.setUserName(fileRo.getUserName());
		fileDo.setUploadedOn(LocalDateTime.now());
		return fileDo;
	}

	public void writeMultipartToFile(MultipartFile multipart, String dir, String fileName) throws IOException {
		Path filepath = Paths.get(dir, fileName);
		multipart.transferTo(filepath);
	}

	@Override
	public FileDO getFileById(UUID id) throws RequestedItemNotFoundException, IOException {
		Optional<FileDO> fileDOOpt = fileUploadRepository.findById(id);
		fileDOOpt.orElseThrow(() -> new RequestedItemNotFoundException("File details not available for the specified ID : "+id));
		FileDO fileDo = fetchFileContent(fileDOOpt.get());
		return fileDo;
	}

	private FileDO fetchFileContent(FileDO fileDO) throws IOException, RequestedItemNotFoundException {
		Path filepath = Paths.get(dirPath, fileDO.getFileName());
		
		if(Files.notExists(filepath)) {
			throw new RequestedItemNotFoundException("File not found.");
		}
		fileDO.setDocContent(Files.readString(filepath));
		return fileDO;
	}

	@Override
	public List<FileDO> getFilesByUserName(String userName) throws RequestedItemNotFoundException {
		List<FileDO> fileList = fileUploadRepository.findAllByUserName(userName);
		if(ObjectUtils.isEmpty(fileList)) {
			throw new RequestedItemNotFoundException("User details not found.");
		}
		populateFilecontent(fileList);
		return fileList;
	}

	private void populateFilecontent(List<FileDO> fileList) {
		fileList.stream().forEach(fileDo -> {
			try {
				fetchFileContent(fileDo);
			} catch (IOException | RequestedItemNotFoundException e) {
				LOGGER.error("Exception : ", e);
			}
		});
	}

	
	
	
}
