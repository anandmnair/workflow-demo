package com.anand.wf.demo.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.anand.wf.demo.core.status.TaskStatus;


public class WorkflowTest {

	@Test
	public void sequentialWorkflowTest() {
		
		Workflow workflow = new Workflow()
				.addTaskToRoot("A")
				.addChildTask("B", TaskStatus.APPROVED, "A")
				.addChildTask("C", TaskStatus.APPROVED, "B")
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
	public void conditionalWorkflowOnApproveTest() {
		
		Workflow workflow = new Workflow()
				.addTaskToRoot("A")
				.addChildTask("B", TaskStatus.APPROVED, "A")
				.addChildTask("C", TaskStatus.REJECTED, "A")
				;
		
		assertTrue(workflow.isActive("A"));
		assertFalse(workflow.isActive("B"));
		assertFalse(workflow.isActive("C"));

		workflow.updateTask("A", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B"));
		assertFalse(workflow.isActive("C"));
	}
	
	@Test
	public void conditionalWorkflowOnRejectTest() {
		
		Workflow workflow = new Workflow()
				.addTaskToRoot("A")
				.addChildTask("B", TaskStatus.APPROVED, "A")
				.addChildTask("C", TaskStatus.REJECTED, "A")
				;
		
		assertTrue(workflow.isActive("A"));
		assertFalse(workflow.isActive("B"));
		assertFalse(workflow.isActive("C"));

		workflow.updateTask("A", TaskStatus.REJECTED);
		assertFalse(workflow.isActive("B"));
		assertTrue(workflow.isActive("C"));	
	}
	
	@Test
	public void simpleParallelWorkflowTest() {
		
		Workflow workflow = new Workflow()
				.addTaskToRoot("A")
				.addChildTask("B", TaskStatus.APPROVED, "A")
				.addChildTask("C", TaskStatus.APPROVED, "A")
				;
		
		assertTrue(workflow.isActive("A"));
		assertFalse(workflow.isActive("B"));
		assertFalse(workflow.isActive("C"));

		workflow.updateTask("A", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B"));
		assertTrue(workflow.isActive("C"));	
	}
	
	@Test
	public void multiParentsWorkflowTest() {
		
		Workflow workflow = new Workflow()
				.addTaskToRoot("A")
				.addChildTask("B1", TaskStatus.APPROVED, "A")
				.addChildTask("B2", TaskStatus.APPROVED, "A")
				.addChildTask("C", TaskStatus.APPROVED, "B1", "B2")
				;
		
		assertTrue(workflow.isActive("A"));
		assertFalse(workflow.isActive("B1"));
		assertFalse(workflow.isActive("B2"));
		assertFalse(workflow.isActive("C"));


		workflow.updateTask("A", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B1"));
		assertTrue(workflow.isActive("B2"));
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("B2", TaskStatus.APPROVED);
		assertFalse(workflow.isActive("C"));
		
		workflow.updateTask("B1", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("C"));

	}
	
	@Test
	public void conditionalMultiParentsWorkflowOnApproveTest() {
		
		Workflow workflow = new Workflow()
				.addTaskToRoot("A")
				.addChildTask("B1", TaskStatus.APPROVED, "A")
				.addChildTask("B2", TaskStatus.APPROVED, "A")
				.addChildTask("C", TaskStatus.APPROVED, "B1", "B2")
				.addChildTask("D", TaskStatus.REJECTED, "B1", "B2")
				;
		
		assertTrue(workflow.isActive("A"));
		assertFalse(workflow.isActive("B1"));
		assertFalse(workflow.isActive("B2"));
		assertFalse(workflow.isActive("C"));
		assertFalse(workflow.isActive("D"));

		workflow.updateTask("A", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B1"));
		assertTrue(workflow.isActive("B2"));
		assertFalse(workflow.isActive("C"));
		assertFalse(workflow.isActive("D"));
		
		workflow.updateTask("B2", TaskStatus.APPROVED);
		assertFalse(workflow.isActive("C"));
		assertFalse(workflow.isActive("D"));
		
		workflow.updateTask("B1", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("C"));
		assertFalse(workflow.isActive("D"));

	}
	
	@Test
	public void conditionalMultiParentsWorkflowOnRejectTest() {
		
		Workflow workflow = new Workflow()
				.addTaskToRoot("A")
				.addChildTask("B1", TaskStatus.APPROVED, "A")
				.addChildTask("B2", TaskStatus.APPROVED, "A")
				.addChildTask("C", TaskStatus.APPROVED, "B1", "B2")
				.addChildTask("D", TaskStatus.REJECTED, "B1", "B2")
				;
		
		assertTrue(workflow.isActive("A"));
		assertFalse(workflow.isActive("B1"));
		assertFalse(workflow.isActive("B2"));
		assertFalse(workflow.isActive("C"));
		assertFalse(workflow.isActive("D"));

		workflow.updateTask("A", TaskStatus.APPROVED);
		assertTrue(workflow.isActive("B1"));
		assertTrue(workflow.isActive("B2"));
		assertFalse(workflow.isActive("C"));
		assertFalse(workflow.isActive("D"));
		
		workflow.updateTask("B2", TaskStatus.REJECTED);
		assertFalse(workflow.isActive("C"));
		assertFalse(workflow.isActive("D"));

		workflow.updateTask("B1", TaskStatus.REJECTED);
		assertFalse(workflow.isActive("C"));
		assertTrue(workflow.isActive("D"));
	}
	
}
