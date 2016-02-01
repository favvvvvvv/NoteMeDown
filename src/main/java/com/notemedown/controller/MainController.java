package com.notemedown.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.notemedown.model.Breadcrumbs;
import com.notemedown.model.Folder;
import com.notemedown.model.Group;
import com.notemedown.model.Status;
import com.notemedown.model.Task;
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
		return groupService.getAll(true);
	}
	
	@RequestMapping(path = "/", method=RequestMethod.GET)
	public String groups(ModelMap model) {
		model.put("breadcrumbs", new Breadcrumbs());
		model.put("group", new Group());
		return "root";
	}

	@RequestMapping(path = "/groups/{id}", method=RequestMethod.GET)
	public String groupFolders(@PathVariable("id") Long id, ModelMap model) {
		model.put("breadcrumbs", new Breadcrumbs(groupService.get(id)));
		model.put("groupId", id);
		model.put("folder", new Folder());
		model.put("folders", folderService.getByGroup(id));
		return "group";
	}
	
	@RequestMapping(path = "/folders/{id}", method = RequestMethod.GET)
	public String folderContents(@PathVariable("id") Long id, ModelMap model) {
		Folder folder = folderService.get(id);
		model.put("breadcrumbs", new Breadcrumbs(folder));
		model.put("folderId", id);
		model.put("task", new Task());
		model.put("folder", new Folder());
		model.put("subfolders", folderService.getByFolder(folder));
		model.put("tasks", taskService.getByStatusFromFolder(
				Status.IN_PROGRESS, folder));
		return "folder";
	}
	
	@RequestMapping(path = "/tasks/due", method=RequestMethod.GET)
	public String dueTasks(@RequestParam(name = "unit", defaultValue = "WEEKS") String unit,
			@RequestParam(name = "amount", defaultValue = "1") Integer amount, ModelMap model) {
		model.put("task", new Task());
		model.put("tasks", taskService.getByDaysLeft(unit, amount));
		return "dueTasks";
	}
	
	@RequestMapping(path = "/tasks/history", method=RequestMethod.GET)
	public String taskHistory(@RequestParam(name = "status") String status, ModelMap model) {
		List<Task> tasks = taskService.getByStatus(Status.valueOf(status));
		tasks.sort((o1, o2) -> {
			return o1.getParentFolder().absolutePath().compareToIgnoreCase(
						o2.getParentFolder().absolutePath());
		});
		model.put("tasks", tasks);
		
		return "history";
	}
}