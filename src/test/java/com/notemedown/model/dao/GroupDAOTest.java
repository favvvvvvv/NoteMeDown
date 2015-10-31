package com.notemedown.model.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.notemedown.model.Group;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:data-access-config.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class GroupDAOTest {
	private GroupDAO dao;

	@Autowired @Required
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void setDao(GroupDAO dao) {
		this.dao = dao;
	}
	
	@Test
	public void successWhenStoredAndRetrieved() {
		Group group = new Group("My group");
		long id = dao.save(group);
		
		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		Group retrievedGroup = dao.get(id);
		assertEquals(group, retrievedGroup);
		assertNotSame(group, retrievedGroup);
	}
	
	@Test
	public void successWhenStoredUpdatedAndRetrieved() {
		Group group = new Group("Group name");
		long id = dao.save(group);
		
		String alteredName = "Altered group name";
		group.setName(alteredName);
		dao.update(group);

		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		Group retrievedGroup = dao.get(id);
		assertEquals(group, retrievedGroup);
		assertNotSame(group, retrievedGroup);
	}
	
	@Test
	public void failWhenRetrievingDeletedEntity() {
		Group group = new Group("Group to be deleted");
		long id = dao.save(group);
		
		Group retrievedGroup = dao.get(id);
		assertNotNull(retrievedGroup);
		
		dao.delete(group);
		retrievedGroup = dao.get(id);
		assertNull(retrievedGroup);
	}
	
	private Comparator<Group> idComparator =
			(a, b) -> a.getId() < b.getId() ? -1
					: a.getId() > b.getId() ? 1 : 0;
	
	@Test
	public void successWhenRetrievingAllGroups() {
		List<Group> groups = new ArrayList<>();
		for (int i = 0; i < 3; i++)
			groups.add(new Group("Group " + i));
		for (Group g: groups)
			dao.save(g);
		
		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		List<Group> retrievedGroups = dao.getAll();
		retrievedGroups.sort(idComparator);
		assertArrayEquals(groups.toArray(), retrievedGroups.toArray());
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenStoringAnotherGroupWithSameName() {
		Group group = new Group("My group");
		dao.save(group);
		
		Group anotherGroup = new Group("My group");
		dao.save(anotherGroup);

		dao.getHibernateTemplate().flush();
	}
}