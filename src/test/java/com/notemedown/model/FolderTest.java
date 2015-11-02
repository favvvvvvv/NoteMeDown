package com.notemedown.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FolderTest {
	@Test
	public void successWhenGettingAbsolutePathOfFolder() {
		Folder folder = new Folder("Subfolder",
				new Folder("Parent folder", new Group("Parent group")));
		
		assertEquals("Parent group : Parent folder/Subfolder", folder.absolutePath());
	}
}