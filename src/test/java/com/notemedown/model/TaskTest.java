package com.notemedown.model;

import static org.junit.Assert.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

public class TaskTest {
	private Task task;
	
	@Before
	public void setUp() {
		task = new Task("Task under test", null);
	}
	
	@Test
	public void successWhenPostponingTask() {
		task.postpone();
		
		assertEquals(Status.POSTPONED, task.getStatus());
		assertEquals(Date.valueOf(LocalDate.now()), task.getDatePostponed());
	}
	
	@Test(expected = IllegalStateException.class)
	public void failWhenPostponingCompletedTask() {
		task.complete();
		task.postpone();
	}
	
	@Test
	public void successWhenContinuingPostponedTask() {
		task.postpone();
		Date newDueDate = Date.valueOf(LocalDate.now().plus(2, ChronoUnit.DAYS));
		task.continue_(newDueDate);
		
		assertEquals(Status.IN_PROGRESS, task.getStatus());
		assertEquals(newDueDate, task.getDueDate());
	}
	
	@Test(expected = IllegalStateException.class)
	public void failWhenContinuingTaskInProgress() {
		task.continue_(null);
	}
	
	@Test
	public void successWhenCompletingTask() {
		task.complete();
		
		assertEquals(Status.COMPLETED, task.getStatus());
		assertEquals(Date.valueOf(LocalDate.now()), task.getDateFinished());
	}
	
	@Test(expected = IllegalStateException.class)
	public void failWhenCompletingPostponedTask() {
		task.postpone();
		task.complete();
	}
	
	@Test
	public void successWhenFailingTask() {
		task.fail();
		
		assertEquals(Status.FAILED, task.getStatus());
		assertEquals(Date.valueOf(LocalDate.now()), task.getDateFinished());
	}
	
	@Test(expected = IllegalStateException.class)
	public void failWhenFailingPostponedTask() {
		task.postpone();
		task.fail();
	}
}