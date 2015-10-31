package com.notemedown.model.dao;

import static org.junit.Assert.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
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

import com.notemedown.model.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:data-access-config.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class TaskDAOTest {
	private TaskDAO dao;
	private FolderDAO folderDao;
	private GroupDAO groupDao;

	@Autowired @Required
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void setDao(TaskDAO dao) {
		this.dao = dao;
	}

	@Autowired @Required
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void setFolderDao(FolderDAO folderDao) {
		this.folderDao = folderDao;
	}

	@Autowired @Required
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void setGroupDao(GroupDAO groupDao) {
		this.groupDao = groupDao;
	}
	
	private Group parentGroup;
	private Folder parentFolder;
	
	@Before
	public void setUp() {
		parentGroup = new Group("Parent group");
		groupDao.save(parentGroup); 
		parentFolder = new Folder("Parent folder", parentGroup);
		folderDao.save(parentFolder);
	}
	
	@Test
	public void successWhenStoredAndRetrieved() {
		Task task = new Task("My task", parentFolder);
		long id = dao.save(task);

		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		Task retrievedTask = dao.get(id);
		assertEquals(task, retrievedTask);
		assertNotSame(task, retrievedTask);
	}
	
	@Test
	public void successWhenStoredUpdatedAndRetrieved() {
		Task task = new Task("Task name", parentFolder);
		long id = dao.save(task);
		
		String alteredName = "Altered task name";
		task.setName(alteredName);
		dao.update(task);
		
		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		Task retrievedTask = dao.get(id);
		assertEquals(task, retrievedTask);
		assertNotSame(task, retrievedTask);
	}
	
	@Test
	public void failWhenRetrievingDeletedTask() {
		Task task = new Task("Task to be deleted", parentFolder);
		long id = dao.save(task);
		
		Task retrievedTask = dao.get(id);
		assertNotNull(retrievedTask);
		
		dao.delete(task);
		retrievedTask = dao.get(id);
		assertNull(retrievedTask);
	}
	
	private Comparator<Task> idComparator =
			(a, b) -> a.getId() < b.getId() ? -1
					: a.getId() > b.getId() ? 1 : 0;
		
	@Test
	public void successWhenRetrievingMultipleTasksByFolderId() {
		Folder anotherParentFolder = new Folder("Another parent folder", parentGroup);
		folderDao.save(anotherParentFolder);
		
		List<Task> parentFolderTasks = new ArrayList<>(),
				anotherParentFolderTasks = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			Task task = new Task("Task " + i, parentFolder);
			parentFolderTasks.add(task);
			dao.save(task);
		}
		for (int i = 3; i < 5; ++i) {
			Task task = new Task("Task " + i, anotherParentFolder);
			anotherParentFolderTasks.add(task);
			dao.save(task);
		}
		
		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		List<Task> retrievedTasks = dao.getByFolder(parentFolder);
		retrievedTasks.sort(idComparator);
		assertArrayEquals(parentFolderTasks.toArray(), retrievedTasks.toArray());
		retrievedTasks = dao.getByFolder(anotherParentFolder);
		retrievedTasks.sort(idComparator);
		assertArrayEquals(anotherParentFolderTasks.toArray(), retrievedTasks.toArray());
	}
	
	@Test
	public void successWhenRetrievingAllTasks() {
		Folder anotherParentFolder = new Folder("Another parent folder", parentGroup);
		folderDao.save(anotherParentFolder);
		
		List<Task> parentFolderTasks = new ArrayList<>(),
				anotherParentFolderTasks = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			Task task = new Task("Task " + i, parentFolder);
			parentFolderTasks.add(task);
			dao.save(task);
		}
		for (int i = 3; i < 5; ++i) {
			Task task = new Task("Task " + i, anotherParentFolder);
			anotherParentFolderTasks.add(task);
			dao.save(task);
		}
		
		List<Task> allTasks = new ArrayList<>(parentFolderTasks);
		allTasks.addAll(anotherParentFolderTasks);
		
		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		List<Task> retrievedTasks = dao.getAll();
		retrievedTasks.sort(idComparator);
		assertArrayEquals(allTasks.toArray(), retrievedTasks.toArray());
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenStoringAnotherTaskWithSameNameIntoSameFolder() {
		Task task = new Task("Task name", parentFolder);
		dao.save(task);
		Task anotherTask = new Task("Task name", parentFolder);
		dao.save(anotherTask);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test
	public void successWhenStoringAnotherTaskWithSameNameIntoDifferentFolders() {
		Folder anotherParentFolder = new Folder("Another parent folder", parentGroup);
		folderDao.save(anotherParentFolder);
		
		Task task = new Task("Task name", parentFolder);
		dao.save(task);
		Task anotherTask = new Task("Task name", anotherParentFolder);
		dao.save(anotherTask);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenTaskInProgressDateFinishedIsNotNull() {
		Task task = new Task("Task name", parentFolder);
		task.setDateFinished(Date.valueOf(LocalDate.now().minus(2, ChronoUnit.DAYS)));
		dao.save(task);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenPostponedTaskDatePostponedIsNull() {
		Task task = new Task("Task name", parentFolder);
		task.postpone();
		task.setDatePostponed(null);
		dao.save(task);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenPostponedTaskDateFinishedIsNotNull() {
		Task task = new Task("Task name", parentFolder);
		task.postpone();
		task.setDateFinished(Date.valueOf(LocalDate.now().minus(2, ChronoUnit.DAYS)));
		dao.save(task);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenCompletedTaskDateFinishedIsNull() {
		Task task = new Task("Task name", parentFolder);
		task.complete();
		task.setDateFinished(null);
		dao.save(task);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenFailedTaskDateFinishedIsNull() {
		Task task = new Task("Task name", parentFolder);
		task.fail();
		task.setDateFinished(null);
		dao.save(task);
		
		dao.getHibernateTemplate().flush();
	}
}