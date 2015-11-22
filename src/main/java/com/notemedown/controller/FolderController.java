package com.notemedown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.notemedown.model.Folder;
import com.notemedown.service.FolderService;

@Controller
@RequestMapping("/folders")
public class FolderController {
	private FolderService folderService;
	
	@Autowired
	public FolderController(FolderService folderService) {
		this.folderService = folderService;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String addFolder(Folder folder, @RequestParam("parentId") Long parentId, ModelMap model) {		
		folderService.save(folder, parentId);
		return "redirect:/" + (folder.getIsRoot()
				? "groups/" + folder.getParentGroup().getId()
				: "folders/" + folder.getParentFolder().getId());
	}

	@RequestMapping(path = "/{id}/edit", method = RequestMethod.POST)
	public String editFolder(@PathVariable("id") Long id, Folder folder,
			@RequestParam("parentId") Long parentId, ModelMap model) {
		folder.setId(id);
		folderService.update(folder, parentId);
		return "redirect:/" + (folder.getIsRoot()
				? "groups/" + folder.getParentGroup().getId()
				: "folders/" + folder.getParentFolder().getId());
	}
	
	@RequestMapping(path = "/{id}/delete", method = RequestMethod.POST)
	public String deleteFolder(@PathVariable("id") Long id, ModelMap model) {
		Folder folder = folderService.get(id);
		String parentUrl = folder.getIsRoot()
				? "groups/" + folder.getParentGroup().getId()
				: "folders/" + folder.getParentFolder().getId();
		folderService.delete(folder);
		return "redirect:/" + parentUrl;
	}
}