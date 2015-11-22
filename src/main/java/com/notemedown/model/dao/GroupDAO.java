package com.notemedown.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
	
	@SuppressWarnings("unchecked")
	public List<Group> getAll() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Group.class)
				.addOrder(Order.asc("name"));
		return (List<Group>) getHibernateTemplate().findByCriteria(criteria);
	}
}