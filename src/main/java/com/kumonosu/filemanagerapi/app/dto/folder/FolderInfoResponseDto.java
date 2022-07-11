package com.kumonosu.filemanagerapi.app.dto.folder;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kumonosu.filemanagerapi.app.dto.file.FileInfoResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FolderInfoResponseDto {

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "parentFolderId")
	private Long parentFolderId;

	@JsonProperty(value = "files")
	private List<FileInfoResponseDto> files;

	@JsonProperty(value = "path")
	private String path;

}
