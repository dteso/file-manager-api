package com.kumonosu.filemanagerapi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kumonosu.filemanagerapi.app.entity.FileInfo;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long> {

	public FileInfo findFileInfoByName(String name);

	public Integer deleteFileInfoByName(String name);

}
