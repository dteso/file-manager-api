package com.kumonosu.filemanagerapi.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "filename")
	private String filename;

	@Column(name = "url")
	private String url;

	@Column(name = "type")
	private String type;

	@Column(name = "extension")
	private String extension;

	@Column(name = "created_time")
	private String createdTime;

	@Column(name = "updated_time")
	private String updatedTime;

	@ManyToOne
	private FolderInfo folder;
}