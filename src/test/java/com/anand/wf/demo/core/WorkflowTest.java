package com.anand.wf.demo.core;

import static org.junit.Assert.*;

import org.junit.Test;

import com.anand.wf.demo.core.Workflow;
import com.anand.wf.demo.core.status.TaskStatus;


public class WorkflowTest {

	@Test
	public void sequentialWorkflowTest() {
		
		Workflow workflow = new Workflow()
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
		
		Workflow workflow = new Workflow()
				.withTask("A")
				.withTask("B")
				.withTask("C")
				;
		assertTrue(workflow.isActive("A"));
		assertTrue(workflow.isActive("B"));
		assertTrue(workflow.isActive("C"));
	}
	
	@Test
	public void parallelSubTaskTest() {
		
		Workflow workflow = new Workflow()
				.withTask("A")
				.nextTask("B", TaskStatus.APPROVED)
				.start()
					.nextTask("B1", TaskStatus.APPROVED)
					.nextTask("B2", TaskStatus.APPROVED)
					.nextTask("B3", TaskStatus.APPROVED)
				.end()
				.withTask("C")
				;
		assertTrue(workflow.isActive("A"));
		assertTrue(workflow.isActive("B"));
		assertTrue(workflow.isActive("C"));
	}
}
