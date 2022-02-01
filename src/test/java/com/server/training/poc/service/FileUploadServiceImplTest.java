package com.server.training.poc.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.server.training.poc.exception.RequestedItemNotFoundException;
import com.server.training.poc.model.dos.FileDO;
import com.server.training.poc.model.ros.FileRO;
import com.server.training.poc.repository.FileUploadRepository;
import com.server.training.poc.service.impl.FileUploadServiceImpl;

@ExtendWith(MockitoExtension.class)
public class FileUploadServiceImplTest {

	@Mock
	private FileUploadRepository fileUploadRepository;

	@InjectMocks
	private FileUploadServiceImpl fileUploadServiceImpl;
	
	@BeforeAll
	public static void init() throws IOException {
		Path path = Paths.get("/poc/");
		Files.createDirectories(path);
	}
	
	@AfterAll
	public static void tearDown() throws IOException {
		Path pathToBeDeleted = Paths.get("/poc/");
	    Files.walk(pathToBeDeleted)
	      .sorted(Comparator.reverseOrder())
	      .map(Path::toFile)
	      .forEach(File::delete);
	}

	@BeforeEach
	public void setUp() {
		ReflectionTestUtils.setField(fileUploadServiceImpl, "dirPath", "/poc/");
	}

	@Test
	public void testUploadFile() throws IOException {
		
		// Given
		FileDO fileDo = Mockito.mock(FileDO.class);
		FileRO fileRo = Mockito.spy(new FileRO());
		MockMultipartFile file = new MockMultipartFile("pocFile", "pocFile.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());
		fileRo.setFile(file);

		// When
		when(fileUploadRepository.save(any(FileDO.class))).thenReturn(fileDo);

		// Then
		fileUploadServiceImpl.uploadFile(fileRo);
	}

	@Test
	public void testGetFileById() throws RequestedItemNotFoundException, IOException {

		// Given
		Path path = Paths.get("/poc/5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1_pocFile.txt");
		Files.writeString(path, "Hello");
		
		UUID id = UUID.fromString("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1");
		FileDO fileDo = new FileDO();
		
		fileDo.setFileName("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1_pocFile.txt");
		Optional<FileDO> fileDOOpt = Optional.of(fileDo);
		
		// When
		when(fileUploadRepository.findById(any(UUID.class))).thenReturn(fileDOOpt);
		
		// Then
		fileUploadServiceImpl.getFileById(id);
	}

	@Test
	public void testGetFileById_Throw_NoIdPresent() {

		// Given
		UUID id = UUID.fromString("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1");
		
		// When
		RequestedItemNotFoundException thrown = Assertions.assertThrows(RequestedItemNotFoundException.class,
				() -> fileUploadServiceImpl.getFileById(id));
		
		// Then
		String expectedMessage = "File details not available for the specified ID : 5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1";
		String actualMessage = thrown.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testGetFileById_Throw_FileNotFound() {

		// Given
		UUID id = UUID.fromString("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1");
		FileDO fileDo = new FileDO();
		fileDo.setFileName("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1_pocFile1.txt");
		Optional<FileDO> fileDOOpt = Optional.of(fileDo);
		
		// When
		when(fileUploadRepository.findById(any(UUID.class))).thenReturn(fileDOOpt);
		RequestedItemNotFoundException thrown = Assertions.assertThrows(RequestedItemNotFoundException.class,
				() -> fileUploadServiceImpl.getFileById(id));
		
		// Then
		String expectedMessage = "File not found.";
		String actualMessage = thrown.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testGetFilesByUserName() throws RequestedItemNotFoundException {

		// Given
		FileDO fileDo = new FileDO();
		fileDo.setFileName("5b3ba0bb-76c1-476b-b163-b1bc2e0fb0e1_pocFile.txt");
		List<FileDO> fileList = Mockito.spy(new ArrayList<>());
		fileList.add(fileDo);

		// When
		when(fileUploadRepository.findAllByUserName(anyString())).thenReturn(fileList);

		// Then
		fileUploadServiceImpl.getFilesByUserName(anyString());
	}

	@Test
	public void testGetFilesByUserName_Throw_UserNotFound() {

		// Given

		List<FileDO> fileList = Mockito.spy(new ArrayList<>());
		// When
		when(fileUploadRepository.findAllByUserName(anyString())).thenReturn(fileList);
		RequestedItemNotFoundException thrown = Assertions.assertThrows(RequestedItemNotFoundException.class,
				() -> fileUploadServiceImpl.getFilesByUserName(anyString()));
		
		// Then
		String expectedMessage = "User details not found.";
		String actualMessage = thrown.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

}
