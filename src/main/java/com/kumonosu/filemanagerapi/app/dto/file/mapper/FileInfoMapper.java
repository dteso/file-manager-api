package com.kumonosu.filemanagerapi.app.dto.file.mapper;

import org.springframework.stereotype.Service;

import com.kumonosu.filemanagerapi.app.dto.file.FileInfoDto;
import com.kumonosu.filemanagerapi.app.dto.file.FileInfoResponseDto;
import com.kumonosu.filemanagerapi.app.entity.FileInfo;

@Service
public final class FileInfoMapper {

	public static FileInfo fromDtoToEntity(FileInfoDto dto) {

		if (dto == null) {
			return null;
		}

		FileInfo fileInfo = new FileInfo();

		fileInfo.setName(dto.getName());
		fileInfo.setUrl(dto.getUrl());
		fileInfo.setType(dto.getType());
		fileInfo.setExtension(dto.getExtension());
		fileInfo.setFilename(dto.getFilaname());
		fileInfo.setCreatedTime(dto.getCreatedTime());
		fileInfo.setUpdatedTime(dto.getUpdatedTime());
		fileInfo.setFolder(dto.getFolder());

		return fileInfo;
	}

	public static FileInfoResponseDto fromEntityToResponse(FileInfo file) {

		if (file == null) {
			return null;
		}

		FileInfoResponseDto fileInfo = new FileInfoResponseDto();

		fileInfo.setName(file.getName());
		fileInfo.setUrl(file.getUrl());
		fileInfo.setId(file.getId());
		fileInfo.setOriginalFilename(file.getFilename());
		fileInfo.setCreatedTime(file.getCreatedTime());
		fileInfo.setPath(file.getFolder().getPath());

		return fileInfo;
	}

	public static FileInfoDto fromEntityToDto(FileInfo dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
