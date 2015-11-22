package com.notemedown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.notemedown.model.Group;
import com.notemedown.service.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupController {
	private GroupService groupService;
	
	@Autowired
	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String addGroup(Group group, ModelMap model) {
		groupService.save(group);
		return "redirect:/";
	}

	@RequestMapping(path = "/{id}/edit", method = RequestMethod.POST)
	public String editGroup(@PathVariable("id") Long id, Group group, ModelMap model) {
		group.setId(id);
		groupService.update(group);
		return "redirect:/";
	}
	
	@RequestMapping(path = "/{id}/delete", method = RequestMethod.POST)
	public String deleteGroup(@PathVariable("id") Long id, ModelMap model) {
		groupService.delete(id);
		return "redirect:/";
	}
}
