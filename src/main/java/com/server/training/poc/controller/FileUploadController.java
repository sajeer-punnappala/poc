package com.server.training.poc.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.training.poc.custom.annotation.ValidUuid;
import com.server.training.poc.exception.RequestedItemNotFoundException;
import com.server.training.poc.model.common.ResponseVo;
import com.server.training.poc.model.dos.FileDO;
import com.server.training.poc.model.ros.FileRO;
import com.server.training.poc.service.FileUploadService;

@RestController
@RequestMapping("/file")
@Validated
public class FileUploadController {
	
	Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@PostMapping("/upload")
	public ResponseEntity<ResponseVo<FileDO>> uploadFile(@Valid @ModelAttribute("fileRo") FileRO fileRo,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.info("uploadFile : IN");
		FileDO fileResponse = fileUploadService.uploadFile(fileRo);
		LOGGER.info("uploadFile : Exit");
		return new ResponseEntity<ResponseVo<FileDO>>(new ResponseVo<FileDO>(HttpStatus.CREATED,
				HttpStatus.CREATED.value(), fileResponse, null, HttpStatus.CREATED.getReasonPhrase()),
				HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseVo<FileDO>> getFileById(@PathVariable("id") @ValidUuid String id,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		LOGGER.info("getFileById : IN");
		FileDO fileResponse = fileUploadService.getFileById(UUID.fromString(id));
		LOGGER.info("getFileById : Exit");
		return new ResponseEntity<ResponseVo<FileDO>>(new ResponseVo<FileDO>(HttpStatus.OK, HttpStatus.OK.value(),
				fileResponse, null, HttpStatus.OK.getReasonPhrase()), HttpStatus.OK);
	}

	@GetMapping("/user/{userName}")
	public ResponseEntity<ResponseVo<List<FileDO>>> getFilesByUserName(@PathVariable("userName") String userName,
			HttpServletRequest request, HttpServletResponse response) throws RequestedItemNotFoundException {

		LOGGER.info("getFilesByUserName : IN");
		List<FileDO> fileResponse = fileUploadService.getFilesByUserName(userName);
		LOGGER.info("getFilesByUserName : Exit");
		return new ResponseEntity<ResponseVo<List<FileDO>>>(new ResponseVo<List<FileDO>>(HttpStatus.OK,
				HttpStatus.OK.value(), fileResponse, null, HttpStatus.OK.getReasonPhrase()), HttpStatus.OK);
	}
	
	
	
}
