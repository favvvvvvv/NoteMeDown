package com.notemedown.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.notemedown.model.Folder;
import com.notemedown.model.Status;
import com.notemedown.model.Task;
import com.notemedown.model.dao.FolderDAO;
import com.notemedown.model.dao.TaskDAO;

public class TaskService {
	private TaskDAO dao;
	private FolderDAO folderDao;

	public TaskService(TaskDAO dao, FolderDAO folderDao) {
		this.dao = dao;
		this.folderDao = folderDao;
	}
	
	@Transactional
	public Long save(Task task) {
		return dao.save(task);
	}
	
	@Transactional
	public void update(Task task) {
		dao.update(task);
	}
	
	@Transactional
	public void delete(Task task) {
		dao.delete(task);
	}

	@Transactional(readOnly = true)
	public Task get(Long id) {
		return dao.get(id);
	}

	@Transactional(readOnly = true)
	public List<Task> getByStatus(Status status) {
		return dao.getByStatus(status);
	}

	@Transactional(readOnly = true)
	public List<Task> getByStatusFromFolder(Status status, Folder folder) {
		return dao.getByStatusFromFolder(status, folder);
	}
	
	@Transactional
	public void postpone(Long id) {
		Task task = dao.get(id);
		task.postpone();
	}

	@Transactional
	public void postponeAll(Long folderId) {
		Folder folder = folderDao.get(folderId);
		List<Task> tasks = getByStatusFromFolder(Status.IN_PROGRESS, folder);
		
		for (Task task: tasks) {
			task.postpone();
		}
	}
	
	@Transactional
	public void continue_(Long id) {
		Task task = dao.get(id);
		task.continue_(null);
	}
	
	@Transactional
	public void continue_(Long id, LocalDate dueDate) {
		if (dueDate == null)
			throw new NullPointerException("New due date is null");
		Task task = dao.get(id);
		task.continue_(Date.valueOf(dueDate));
	}
	
	@Transactional
	public void complete(Long id) {
		Task task = dao.get(id);
		task.complete();
	}

	@Transactional
	public void completeAll(Long folderId) {
		Folder folder = folderDao.get(folderId);
		List<Task> tasks = getByStatusFromFolder(Status.IN_PROGRESS, folder);
		
		for (Task t: tasks) {
			t.complete();
		}
	}
	
	@Transactional
	public void fail(Long id) {
		Task task = dao.get(id);
		task.fail();
	}

	@Transactional
	public void failAll(Long folderId) {
		Folder folder = folderDao.get(folderId);
		List<Task> tasks = getByStatusFromFolder(Status.IN_PROGRESS, folder);
		
		for (Task t: tasks) {
			t.fail();
		}
	}
}