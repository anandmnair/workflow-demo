package com.anand.wf.demo.core;

import static org.junit.Assert.*;

import org.junit.Test;

import com.anand.wf.demo.core.WorkflowOld;
import com.anand.wf.demo.core.status.TaskStatus;


public class WorkflowOldTest {

	@Test
	public void sequentialWorkflowTest() {
		
		WorkflowOld workflow = new WorkflowOld()
				.withTask("A")
				.nextTask("B", TaskStatus.APPROVED)
				.nextTask("C", TaskStatus.APPROVED)
				;
		
		assertTrue(workflow.isActive("A"));
		assertFalse(workflow.isActive("B"));
		assertFalse(workflow.isActive("C"));

		workflow.updateTask("A", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B"));
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("B", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("C"));
	}
	
	@Test
	public void parallelWorkflowTest() {
		
		WorkflowOld workflow = new WorkflowOld()
				.withTask("A")
				.withTask("B")
				.withTask("C")
				;
		assertTrue(workflow.isActive("A"));
		assertTrue(workflow.isActive("B"));
		assertTrue(workflow.isActive("C"));
	}
	
	@Test
	public void complexHybridTaskTest() {
		WorkflowOld workflow = new WorkflowOld()
				.withTask("A")
				.nextTask("B", TaskStatus.APPROVED)
				.start()
					.nextTask("B1", TaskStatus.APPROVED)
					.nextTask("B2", TaskStatus.APPROVED)
					.nextTask("B3", TaskStatus.APPROVED)
				.end()
				.nextTask("C",TaskStatus.APPROVED)
				;
		assertTrue(workflow.isActive("A"));
		assertFalse(workflow.isActive("B"));
		assertFalse(workflow.isActive("B1"));
		assertFalse(workflow.isActive("B2"));
		assertFalse(workflow.isActive("B3"));
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("A", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B"));
		assertFalse(workflow.isActive("B1"));
		assertFalse(workflow.isActive("B2"));
		assertFalse(workflow.isActive("B3"));
		assertFalse(workflow.isActive("C"));

		workflow.updateTask("B", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B1"));
		assertFalse(workflow.isActive("B2"));
		assertFalse(workflow.isActive("B3"));
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("B3", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B2"));
		assertFalse(workflow.isActive("B3"));
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("B1", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B3"));
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("B2", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("C"));
	}
	
	@Test
	public void complexWithParallelTaskTest() {
		WorkflowOld workflow = new WorkflowOld()
				.withTask("A")
				.nextTask("B", TaskStatus.APPROVED)
				.start()
					.withTask("B1", TaskStatus.APPROVED)
					.withTask("B2", TaskStatus.APPROVED)
					.withTask("B3", TaskStatus.APPROVED)
				.end()
				.nextTask("C",TaskStatus.APPROVED)
				;
		assertTrue(workflow.isActive("A"));
		assertFalse(workflow.isActive("B"));
		assertFalse(workflow.isActive("B1"));
		assertFalse(workflow.isActive("B2"));
		assertFalse(workflow.isActive("B3"));
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("A", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B"));
		assertFalse(workflow.isActive("B1"));
		assertFalse(workflow.isActive("B2"));
		assertFalse(workflow.isActive("B3"));
		assertFalse(workflow.isActive("C"));

		workflow.updateTask("B", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B1"));
		assertTrue(workflow.isActive("B2"));
		assertTrue(workflow.isActive("B3"));
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("B1", TaskStatus.APPROVED);
		assertFalse(workflow.isActive("C"));

		workflow.updateTask("B2", TaskStatus.APPROVED);
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("B3", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("C"));
	}
}
