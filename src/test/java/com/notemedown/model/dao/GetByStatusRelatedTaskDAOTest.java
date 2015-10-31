package com.notemedown.model.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.notemedown.model.Folder;
import com.notemedown.model.Group;
import com.notemedown.model.Status;
import com.notemedown.model.Task;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:data-access-config.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class GetByStatusRelatedTaskDAOTest {
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
	private Folder anotherParentFolder;
	
	private List<Task> tasksInProgress,
			postponedTasks,
			completedTasks,
			failedTasks;
	
	private void addTaskTo(Task task, List<Task> destinationList) {
		destinationList.add(task);
		dao.save(task);
	}
	
	@Before
	public void setUp() {
		parentGroup = new Group("Parent group");
		groupDao.save(parentGroup);
		
		parentFolder = new Folder("Parent folder", parentGroup);
		folderDao.save(parentFolder);
		anotherParentFolder = new Folder("Another parent folder", parentGroup);
		folderDao.save(anotherParentFolder);
		
		tasksInProgress = new ArrayList<>();
		addTaskTo(new Task("Task 1", parentFolder), tasksInProgress);
		addTaskTo(new Task("Task 2", parentFolder), tasksInProgress);
		addTaskTo(new Task("Task 3", anotherParentFolder), tasksInProgress);
		
		postponedTasks = new ArrayList<>();
		addTaskTo((new Task("Task 4", parentFolder)).postpone(), postponedTasks);
		addTaskTo((new Task("Task 5", parentFolder)).postpone(), postponedTasks);
		addTaskTo((new Task("Task 6", anotherParentFolder)).postpone(), postponedTasks);
		addTaskTo((new Task("Task 7", anotherParentFolder)).postpone(), postponedTasks);
		
		completedTasks = new ArrayList<>();
		addTaskTo((new Task("Task 8", parentFolder)).complete(), completedTasks);
		addTaskTo((new Task("Task 9", parentFolder)).complete(), completedTasks);
		addTaskTo((new Task("Task 10", parentFolder)).complete(), completedTasks);
		addTaskTo((new Task("Task 11", anotherParentFolder)).complete(), completedTasks);
		
		failedTasks = new ArrayList<>();
		addTaskTo((new Task("Task 12", parentFolder)).fail(), failedTasks);
		addTaskTo((new Task("Task 13", parentFolder)).fail(), failedTasks);
		addTaskTo((new Task("Task 14", parentFolder)).fail(), failedTasks);
		addTaskTo((new Task("Task 15", anotherParentFolder)).fail(), failedTasks);
		addTaskTo((new Task("Task 16", anotherParentFolder)).fail(), failedTasks);
		
		dao.getHibernateTemplate().flush();
	}
	
	private Comparator<Task> idComparator =
			(a, b) -> a.getId() < b.getId() ? -1
					: a.getId() > b.getId() ? 1 : 0;
	
	@Test
	public void successWhenRetrievingTasksInProgress() {
		List<Task> retrievedTasks = dao.getByStatus(Status.IN_PROGRESS);
		retrievedTasks.sort(idComparator);
		assertArrayEquals(tasksInProgress.toArray(), retrievedTasks.toArray());
	}
	
	@Test
	public void successWhenRetrievingPostponedTasks() {
		List<Task> retrievedTasks = dao.getByStatus(Status.POSTPONED);
		retrievedTasks.sort(idComparator);
		assertArrayEquals(postponedTasks.toArray(), retrievedTasks.toArray());
	}
	
	@Test
	public void successWhenRetrievingCompletedTasks() {
		List<Task> retrievedTasks = dao.getByStatus(Status.COMPLETED);
		retrievedTasks.sort(idComparator);
		assertArrayEquals(completedTasks.toArray(), retrievedTasks.toArray());
	}
	
	@Test
	public void successWhenRetrievingFailedTasks() {
		List<Task> retrievedTasks = dao.getByStatus(Status.FAILED);
		retrievedTasks.sort(idComparator);
		assertArrayEquals(failedTasks.toArray(), retrievedTasks.toArray());
	}
}