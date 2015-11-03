package com.notemedown.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.notemedown.model.Folder;
import com.notemedown.model.Group;
import com.notemedown.model.dao.FolderDAO;

public class FolderService {
	private FolderDAO dao;

	public FolderService(FolderDAO dao) {
		this.dao = dao;
	}
	
	@Transactional
	public Long save(Folder folder) {
		return dao.save(folder);
	}

	@Transactional
	public void update(Folder folder) {
		dao.update(folder);
	}

	@Transactional
	public void delete(Folder folder) {
		dao.delete(folder);
	}

	@Transactional(readOnly = true)
	public Folder get(Long id) {
		return dao.get(id);
	}

	@Transactional(readOnly = true)
	public List<Folder> getByFolder(Folder folder) {
		return dao.getByFolder(folder);
	}

	@Transactional(readOnly = true)
	public List<Folder> getByGroup(Group group) {
		return dao.getByGroup(group);
	}

	@Transactional(readOnly = true)
	public List<Folder> getAll() {
		return dao.getAll();
	}
}