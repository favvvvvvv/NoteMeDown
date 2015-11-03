package com.notemedown.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.notemedown.model.Folder;
import com.notemedown.model.Status;
import com.notemedown.model.Task;
import com.notemedown.model.dao.TaskDAO;

public class TaskService {
	private TaskDAO dao;

	public TaskService(TaskDAO dao) {
		this.dao = dao;
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
	
	public Task get(Long id) {
		return dao.get(id);
	}

	public List<Task> getByStatus(Status status) {
		return dao.getByStatus(status);
	}

	public List<Task> getByStatusFromFolder(Status status, Folder folder) {
		return dao.getByStatusFromFolder(status, folder);
	}
}