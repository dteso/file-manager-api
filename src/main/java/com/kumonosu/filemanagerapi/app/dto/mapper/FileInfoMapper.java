package com.kumonosu.filemanagerapi.app.dto.mapper;

import org.springframework.stereotype.Service;

import com.kumonosu.filemanagerapi.app.dto.FileInfoDto;
import com.kumonosu.filemanagerapi.app.entity.FileInfo;

@Service
public final class FileInfoMapper {

	public static FileInfo fromDtoToEntity(FileInfoDto dto) {

		if (dto == null) {
			return null;
		}

		FileInfo fileInfo = new FileInfo();

		fileInfo.setName(dto.getName());
		fileInfo.setPath(dto.getPath());
		fileInfo.setType(dto.getType());
		fileInfo.setExtension(dto.getExtension());
		fileInfo.setFilename(dto.getFilaname());
		fileInfo.setCreatedTime(dto.getCreatedTime());
		fileInfo.setUpdatedTime(dto.getUpdatedTime());

		return fileInfo;
	}

	public static FileInfoDto fromEntityToDto(FileInfo dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
