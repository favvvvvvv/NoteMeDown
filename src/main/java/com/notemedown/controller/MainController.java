package com.notemedown.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.notemedown.model.Folder;
import com.notemedown.model.Group;
import com.notemedown.model.Status;
import com.notemedown.service.FolderService;
import com.notemedown.service.GroupService;
import com.notemedown.service.TaskService;

@Controller
@RequestMapping("/")
public class MainController {
	private GroupService groupService;
	private FolderService folderService;
	private TaskService taskService;
	
	@Autowired
	public MainController(GroupService groupService,
			FolderService folderService, TaskService taskService) {
		this.groupService = groupService;
		this.folderService = folderService;
		this.taskService = taskService;
	}
	
	@ModelAttribute("groups")
	public List<Group> prepareGroups() {
		return groupService.getAll();
	}
	
	@RequestMapping(path = "/", method=RequestMethod.GET)
	public String layout(ModelMap model) {
		prepareGroups();
		return "root";
	}

	@RequestMapping(path = "/groups/{id}", method=RequestMethod.GET)
	public String groupContent(ModelMap model, @PathVariable("id") Long id) {
		prepareGroups();
		model.put("folders", groupService.get(id).getFolders());
		return "group";
	}
	
	@RequestMapping(path = "/folders/{id}", method = RequestMethod.GET)
	public String folderContent(ModelMap model, @PathVariable("id") Long id) {
		prepareGroups();
		Folder folder = folderService.get(id);
		model.put("subfolders", folderService.getByFolder(folder));
		model.put("tasks", taskService.getByStatusFromFolder(
				Status.IN_PROGRESS, folder));
		return "folder";
	}
}