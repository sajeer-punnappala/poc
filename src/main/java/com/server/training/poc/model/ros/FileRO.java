package com.server.training.poc.model.ros;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.server.training.poc.custom.annotation.ValidFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"file"})
@NoArgsConstructor
public class FileRO implements Serializable{

	private static final long serialVersionUID = 1L;

	private UUID id;
	
	@NotBlank(message = "User Name is mandatory!")
	private String userName;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime uploadedOn;
	
	@NotNull
	@ValidFile
	private MultipartFile file;
	
	private String fileName;
	
}
