package com.notemedown.model;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@SequenceGenerator(name = "task_gen", sequenceName = "task_ids",
		allocationSize = 1)
public class Task {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "task_gen", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name = "name", length = 30)
	private String name;
	
	@Column(name = "description", length = 255)
	private String description;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name = "due_date")
	private Date dueDate;
	
	@Column(name = "date_postponed")
	private Date datePostponed;
	
	@Column(name = "date_finished")
	private Date dateFinished;
	
	@ManyToOne
	@JoinColumn(name = "folder_id", referencedColumnName = "id")
	private Folder parentFolder;
	
	public Task() {}
	
	public Task(String name, Folder parentFolder) {
		this.name = name;
		this.status = Status.IN_PROGRESS;
		this.parentFolder = parentFolder;
	}
	
	public Task(String name, String description, Date dueDate, Folder parentFolder) {
		this.name = name;
		this.description = description;
		this.status = Status.IN_PROGRESS;
		this.dueDate = dueDate;
		this.parentFolder = parentFolder;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getDatePostponed() {
		return datePostponed;
	}

	public void setDatePostponed(Date datePostponed) {
		this.datePostponed = datePostponed;
	}

	public Date getDateFinished() {
		return dateFinished;
	}

	public void setDateFinished(Date dateFinished) {
		this.dateFinished = dateFinished;
	}

	public Folder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(Folder folder) {
		this.parentFolder = folder;
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
		Task other = (Task) obj;
		return id == null ? false : id.equals(other.id);
	}
	
	@Override
	public String toString() {
		return parentFolder.toString() + " : " + name;
	}
	
	public Task postpone() {
		setStatus(Status.POSTPONED);
		setDatePostponed(Date.valueOf(LocalDate.now()));
		return this;
	}
	
	public Task continue_(Date dueDate) {
		setStatus(Status.IN_PROGRESS);
		setDueDate(dueDate);
		return this;
	}
	
	public Task complete() {
		setStatus(Status.COMPLETED);
		setDateFinished(Date.valueOf(LocalDate.now()));
		return this;
	}
	
	public Task fail() {
		setStatus(Status.FAILED);
		setDateFinished(Date.valueOf(LocalDate.now()));
		return this;
	}
}