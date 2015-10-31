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
		int result = 31;
		result = 17 * result + name.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		return name.equals(other.name);
	}
	
	@Override
	public String toString() {
		return name;
	}
}