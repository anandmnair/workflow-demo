package com.anand.wf.demo.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

import com.anand.wf.demo.core.Task;
import com.anand.wf.demo.core.status.TaskStatus;

public class TeskTest {

	@Test
	public void test() {
		Task root = new Task("root");
		root.addChild("A", TaskStatus.APPROVED);
		
		Task taskA = root.find("A");
		taskA.addChild("B", TaskStatus.APPROVED);
		taskA.addChild("C", TaskStatus.REJECTED);
		
		Task result = root.find("root");
		assertThat(result.getCode(), equalTo("root"));
		assertTrue(result.getParentTasks().isEmpty());
		
		result = root.find("A");
		assertThat(result.getCode(), equalTo("A"));
		assertThat(result.getParentTasks().get(TaskStatus.APPROVED).size(),equalTo(1));
		assertTrue(result.getParentTasks().get(TaskStatus.APPROVED).containsKey("root"));

		result = root.find("B");
		assertThat(result.getCode(), equalTo("B"));
		assertThat(result.getParentTasks().get(TaskStatus.APPROVED).size(),equalTo(1));
		assertTrue(result.getParentTasks().get(TaskStatus.APPROVED).containsKey("A"));
		
		result = root.find("C");
		assertThat(result.getCode(), equalTo("C"));
		assertThat(result.getParentTasks().size(),equalTo(1));
		assertTrue(result.getParentTasks().get(TaskStatus.REJECTED).containsKey("A"));
	}

}
