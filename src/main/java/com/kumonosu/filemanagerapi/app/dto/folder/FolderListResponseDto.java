package com.kumonosu.filemanagerapi.app.dto.folder;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kumonosu.filemanagerapi.app.dto.file.FileInfoResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderListResponseDto {

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "parentFolder")
	private Long parentFolder;

	@JsonProperty(value = "path")
	private String path;

	@JsonProperty(value = "files")
	private List<FileInfoResponseDto> files;

}
