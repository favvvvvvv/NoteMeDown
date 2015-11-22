package com.notemedown.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.notemedown.model.Folder;
import com.notemedown.model.Group;
import com.notemedown.model.dao.FolderDAO;
import com.notemedown.model.dao.GroupDAO;

public class FolderService {
	private FolderDAO dao;
	private GroupDAO groupDao;

	public FolderService(FolderDAO dao, GroupDAO groupDao) {
		this.dao = dao;
		this.groupDao = groupDao;
	}
	
	@Transactional
	public Long save(Folder folder, Long parentId) {
		if (folder.getIsRoot()) {
			folder.setParentGroup(groupDao.get(parentId));
		} else {
			folder.setParentFolder(dao.get(parentId));
		}
		return dao.save(folder);
	}

	@Transactional
	public void update(Folder folder, Long parentId) {
		if (folder.getIsRoot()) {
			folder.setParentGroup(groupDao.get(parentId));
		} else {
			folder.setParentFolder(dao.get(parentId));
		}
		dao.update(folder);
	}

	@Transactional
	public void delete(Long id) {
		dao.delete(dao.get(id));
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
	public List<Folder> getByGroup(Long id) {
		return dao.getByGroup(groupDao.get(id));
	}
}