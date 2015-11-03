package com.notemedown.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.notemedown.model.Folder;
import com.notemedown.model.Status;
import com.notemedown.model.Task;

public class TaskDAO extends HibernateDaoSupport {
	public Long save(Task task) {
		return (Long) getHibernateTemplate().save(task);
	}
	
	public void update(Task task) {
		getHibernateTemplate().update(task);
	}
	
	public void delete(Task task) {
		getHibernateTemplate().delete(task);
	}
	
	public Task get(Long id) {
		return getHibernateTemplate().get(Task.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Task> getByStatus(Status status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Task.class)
				.add(Restrictions.eq("status", status));
		return (List<Task>) getHibernateTemplate().findByCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<Task> getByStatusFromFolder(Status status, Folder folder) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Task.class)
				.add(Restrictions.eq("status", status))
				.add(Restrictions.eq("parentFolder", folder));
		return (List<Task>) getHibernateTemplate().findByCriteria(criteria);
	}
}