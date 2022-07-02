package com.kumonosu.filemanagerapi.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FileInfoResponseDto {

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "httpStatus")
	private String httpStatus;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "originalFilename")
	private String originalFilename;

	@JsonProperty(value = "path")
	private String path;

	@JsonProperty(value = "createdTime")
	private String createdTime;

	@JsonProperty(value = "error")
	private String error;

}
