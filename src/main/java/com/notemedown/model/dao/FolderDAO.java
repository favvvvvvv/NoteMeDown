package com.notemedown.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.notemedown.model.Folder;
import com.notemedown.model.Group;

public class FolderDAO extends HibernateDaoSupport {
	public Long save(Folder folder) {
		return (Long) getHibernateTemplate().save(folder);
	}
	
	public void update(Folder folder) {
		getHibernateTemplate().update(folder);
	}
	
	public void delete(Folder folder) {
		getHibernateTemplate().delete(folder);
	}
	
	public Folder get(Long id) {
		return getHibernateTemplate().get(Folder.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Folder> getByFolder(Folder folder) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Folder.class)
				.add(Restrictions.eq("parentFolder", folder))
				.addOrder(Order.asc("name"));
		return (List<Folder>) getHibernateTemplate().findByCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<Folder> getByGroup(Group group) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Folder.class)
				.add(Restrictions.eq("parentGroup", group))
				.addOrder(Order.asc("name"));
		return (List<Folder>) getHibernateTemplate().findByCriteria(criteria);
	}
}