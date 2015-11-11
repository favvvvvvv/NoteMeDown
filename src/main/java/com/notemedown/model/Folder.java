package com.notemedown.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "folders")
@SequenceGenerator(name = "folder_gen", sequenceName = "folder_ids",
		allocationSize = 1)
public class Folder {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "folder_gen", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name = "name", length = 30)
	private String name;
	
	@Column(name = "is_root")
	private Boolean isRoot;
	
	@ManyToOne
	@JoinColumn(name = "folder_id", referencedColumnName = "id")
	private Folder parentFolder;
	
	@ManyToOne
	@JoinColumn(name = "group_id", referencedColumnName = "id")
	private Group parentGroup;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder",
			orphanRemoval = true)
	private Set<Folder> subfolders = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder",
			orphanRemoval = true)
	private Set<Task> tasks = new HashSet<>();
	
	public Folder() {}
	
	public Folder(String name, Folder parentFolder) {
		this.name = name;
		this.isRoot = false;
		this.parentFolder = parentFolder;
	}
	
	public Folder(String name, Group parentGroup) {
		this.name = name;
		this.isRoot = true;
		this.parentGroup = parentGroup;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getIsRoot() {
		return isRoot;
	}
	
	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}
	
	public Folder getParentFolder() {
		return parentFolder;
	}
	
	public void setParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
	}
	
	public Group getParentGroup() {
		return parentGroup;
	}
	
	public void setParentGroup(Group parentGroup) {
		this.parentGroup = parentGroup;
	}
	
	public Set<Folder> getSubfolders() {
		return subfolders;
	}
	
	public void setSubfolders(Set<Folder> subfolders) {
		this.subfolders = subfolders;
	}
	
	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public int hashCode() {
		return id == null ? super.hashCode() : id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		return id == null ? false : id.equals(other.id);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + id;
	}
	
	public String absolutePath() {
		return (isRoot ? (parentGroup.absolutePath() + " : ")
				: (parentFolder.absolutePath() + "/")) + name;
	}
}