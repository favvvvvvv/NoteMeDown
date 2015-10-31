package com.notemedown.model.dao;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import java.util.List;

import com.notemedown.model.Group;

public class GroupDAO extends HibernateDaoSupport {
	public Long save(Group group) {
		return (Long) getHibernateTemplate().save(group);
	}
	
	public void update(Group group) {
		getHibernateTemplate().update(group);
	}
	
	public void delete(Group group) {
		getHibernateTemplate().delete(group);
	}
	
	public Group get(Long id) {
		return getHibernateTemplate().get(Group.class, id);
	}
	
	public List<Group> getAll() {
		return getHibernateTemplate().loadAll(Group.class);
	}
}