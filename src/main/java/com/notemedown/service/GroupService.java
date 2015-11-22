package com.notemedown.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import com.notemedown.model.Group;
import com.notemedown.model.dao.GroupDAO;

public class GroupService {
	private GroupDAO dao;
	
	public GroupService(GroupDAO dao) {
		this.dao = dao;
	}
	
	@Transactional
	public Long save(Group group) {
		return dao.save(group);
	}
	
	@Transactional
	public void update(Group group) {
		dao.update(group);
	}
	
	@Transactional
	public void delete(Long id) {
		dao.delete(dao.get(id));
	}
	
	@Transactional(readOnly = true)
	public Group get(Long id) {
		return dao.get(id);
	}
	
	public List<Group> getAll() {
		return getAll(false);
	}

	@Transactional(readOnly = true)
	public List<Group> getAll(boolean initialize) {
		List<Group> groups = dao.getAll();
		if (initialize) {
			for (Group g: groups) {
				Hibernate.initialize(g.getFolders());
			}
		}
		return groups;
	}
}