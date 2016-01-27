package com.notemedown.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "groups")
@SequenceGenerator(name = "group_gen", sequenceName = "group_ids",
		allocationSize = 1)
public class Group {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "group_gen", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name = "name", length = 30)
	private String name;
	
	@OrderBy("name ASC")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentGroup",
			orphanRemoval = true)
	private Set<Folder> folders = new HashSet<>();
	
	public Group() {}
	
	public Group(String name) {
		this.name = name;
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
	
	public Set<Folder> getFolders() {
		return folders;
	}
	
	public void setFolders(Set<Folder> folders) {
		this.folders = folders;
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
		Group other = (Group) obj;
		return id == null ? false : id.equals(other.id);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + id;
	}
}