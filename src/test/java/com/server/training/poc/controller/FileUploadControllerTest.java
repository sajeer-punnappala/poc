package com.server.training.poc.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.server.training.poc.model.dos.FileDO;
import com.server.training.poc.model.ros.FileRO;
import com.server.training.poc.service.FileUploadService;

@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FileUploadService fileUploadService;
	
	@Test
	public void testUploadFile_ReturnStatusCreated() throws Exception {
		
		//Given
		FileDO fileResponse = new FileDO(UUID.fromString("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1"), "Test_User", "5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1_pocFile.txt", LocalDateTime.now(), null);
		FileRO fileRo =new FileRO();
		MockMultipartFile file = new MockMultipartFile("pocFile", "pocFile.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());
		fileRo.setFile(file);
		fileRo.setUserName("Test_User");
		
		//When
		when(fileUploadService.uploadFile(any(FileRO.class))).thenReturn(fileResponse);
		
		//Then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/file/upload")
				.flashAttr("fileRo", fileRo))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void testUploadFile_ReturnStatusBadRequest_IfUserNameEmpty() throws Exception {
		
		//Given
		FileRO fileRo =new FileRO();
		MockMultipartFile file = new MockMultipartFile("pocFile", "pocFile.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());
		fileRo.setFile(file);
		
		//When
		
		//Then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/file/upload")
				.flashAttr("fileRo", fileRo))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorVo.fieldErrors[0].errorMessage").value("User Name is mandatory!"));
	}
	
	@Test
	public void testUploadFile_ReturnStatusBadRequest_IfFileMissing() throws Exception {
		
		//Given
		FileRO fileRo = Mockito.spy(FileRO.class);
		MockMultipartFile file = new MockMultipartFile("pocFile", "pocFile.txt", null,
				"Hello, World!".getBytes());
		fileRo.setFile(file);
		fileRo.setUserName("Test_User");
		//When
		
		//Then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/file/upload")
				.flashAttr("fileRo", fileRo))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorVo.fieldErrors[0].errorMessage").value("File is mandatory"));
	}
	
	@Test
	public void testUploadFile_ReturnStatusBadRequest_IfContentTypeNotTXT() throws Exception {
		
		//Given
		FileRO fileRo = Mockito.spy(FileRO.class);
		MockMultipartFile file = new MockMultipartFile("pocFile", "pocFile.txt", MediaType.TEXT_HTML_VALUE,
				"Hello, World!".getBytes());
		fileRo.setFile(file);
		fileRo.setUserName("Test_User");
		//When
		
		//Then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/file/upload")
				.flashAttr("fileRo", fileRo))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorVo.fieldErrors[0].errorMessage").value("Only .txt files are allowed."));
	}
	
	@Test
	public void testGetFileById_ReturnStatusOk() throws Exception {
		
		//Given
		FileDO fileResponse = new FileDO(UUID.fromString("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1"), "Test_User", "5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1_pocFile.txt", LocalDateTime.now(), "Hello, World!");
		
		//When
		when(fileUploadService.getFileById(any(UUID.class))).thenReturn(fileResponse);
		//Then
		mockMvc.perform(MockMvcRequestBuilders.get("/file/{id}","5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1"));
	}
	
	@Test
	public void testGetFileById_ReturnStatusBadRequest() throws Exception {
		
		//Given
		//When
		//Then
		mockMvc.perform(MockMvcRequestBuilders.get("/file/{id}","5b3ba0bb-77cb1-476b-b163-b1bc2e0fb0e1"))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorVo.errorDescription").value("getFileById.id: Invalid UUID"));
	}
	
	@Test
	public void testGetFilesByUserName_ReturnStatusOk() throws Exception {
		
		//Given
		FileDO fileDo = new FileDO(UUID.fromString("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1"), "Test_User", "5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1_pocFile.txt", LocalDateTime.now(), "Hello, World!");
		FileDO fileDo1 = new FileDO(UUID.fromString("94478e86-9530-4142-ba89-6dc17bf4899a"), "Test_User", "94478e86-9530-4142-ba89-6dc17bf4899a_pocFile.txt", LocalDateTime.now(), "Hello, World!");
		
		
		//When
		when(fileUploadService.getFilesByUserName(anyString())).thenReturn(Arrays.asList(fileDo,fileDo1));
		//Then
		mockMvc.perform(MockMvcRequestBuilders.get("/file/user/{userName}","Test_User"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data",hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1"));
	}
}
