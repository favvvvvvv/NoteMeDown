package com.notemedown.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Breadcrumbs {
	private static final String ROOT = "Home";
	public final List<Crumb> crumbs;
	public final String currentLocation;
	
	public Breadcrumbs() {
		crumbs = Collections.emptyList();
		currentLocation = ROOT;
	}
	
	public Breadcrumbs(Group group) {
		crumbs = Collections.unmodifiableList(baseList());
		currentLocation = group.getName();
	}
	
	public Breadcrumbs(Folder folder) {
		ArrayList<Crumb> list = baseList();
		fillBackwards(list, folder);
		crumbs = Collections.unmodifiableList(list);
		currentLocation = folder.getName();
	}
	
	private static void fillBackwards(ArrayList<Crumb> list, Folder folder) {
		if (!folder.getIsRoot()) {
			fillBackwards(list, folder.getParentFolder());
			list.add(new Crumb(folder.getParentFolder().getName(),
					"/folders/" + folder.getParentFolder().getId()));
		} else {
			list.add(new Crumb(folder.getParentGroup().getName(),
					"/groups/" + folder.getParentGroup().getId()));
		}
	}
	
	private static ArrayList<Crumb> baseList() {
		ArrayList<Crumb> list = new ArrayList<>();
		list.add(new Crumb(ROOT, "/"));
		return list;
	}
	
	private static class Crumb {
		public final String name;
		public final String url;
		
		public Crumb(String name, String url) {
			this.name = name;
			this.url = url;
		}
	}
}