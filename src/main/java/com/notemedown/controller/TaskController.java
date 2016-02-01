package com.notemedown.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.notemedown.model.Status;
import com.notemedown.model.Task;
import com.notemedown.service.TaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {
	private TaskService taskService;

	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));		// true means convert empty String to null
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String addTask(Task task, @RequestParam("folderId") Long folderId, ModelMap model) {
		task.setStatus(Status.IN_PROGRESS);
		taskService.save(task, folderId);
		return "redirect:/" + "folders/" + task.getParentFolder().getId();
	}

	@RequestMapping(path = "/{id}/edit", method = RequestMethod.POST)
	public String editTask(HttpServletRequest request, @PathVariable("id") Long id, Task task,
			@RequestParam("folderId") Long folderId, ModelMap model) {
		task.setId(id);
		taskService.update(task, folderId);
		return "redirect:" + request.getHeader("Referer");
	}
	
	@RequestMapping(path = "/{id}/delete", method = RequestMethod.POST)
	public String deleteTask(@PathVariable("id") Long id, HttpServletRequest request) {
		taskService.delete(id);
		return "redirect:" + request.getHeader("Referer");
	}
	
	@RequestMapping(path = "/{id}/complete", method = RequestMethod.POST)
	public String complete(@PathVariable("id") Long id, HttpServletRequest request) {
		taskService.complete(id);
		return "redirect:" + request.getHeader("Referer");
	}
	
	@RequestMapping(path = "/{id}/continue", method = RequestMethod.POST)
	public String continue_(@PathVariable("id") Long id, @RequestParam("dueDate") Date dueDate, HttpServletRequest request) {
		taskService.continue_(id, dueDate);
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping(path = "/{id}/postpone", method = RequestMethod.POST)
	public String postpone(@PathVariable("id") Long id, HttpServletRequest request) {
		taskService.postpone(id);
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping(path = "/{id}/fail", method = RequestMethod.POST)
	public String fail(@PathVariable("id") Long id, HttpServletRequest request) {
		taskService.fail(id);
		return "redirect:" + request.getHeader("Referer");
	}
}