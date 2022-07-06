package com.kumonosu.filemanagerapi.app.dto.file;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kumonosu.filemanagerapi.app.entity.FolderInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Validated
@NoArgsConstructor
@Data
public class FileInfoDto {

	@NotNull
	@JsonProperty(value = "name")
	private String name;

	@NotNull
	@JsonProperty(value = "filename")
	private String filaname;

	@NotNull
	@JsonProperty(value = "path")
	private String path;

	@JsonProperty(value = "type")
	private String type;

	@NotNull
	@JsonProperty(value = "extension")
	private String extension;

	private FolderInfo folder;

	private String createdTime;

	private String updatedTime;

}
