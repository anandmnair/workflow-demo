package com.anand.wf.demo.core;

import java.util.Map;
import java.util.Stack;

import com.anand.wf.demo.core.status.TaskStatus;

public class Workflow {
	
	private static final String ROOT="root";
	
	private Task root = new Task(ROOT, TaskStatus.APPROVED);
	
	private String lastTask = ROOT;
	
	private Stack<String> taskStack = new Stack();
	
	public Workflow withTask(String code) {
		this.root.addChild(code, TaskStatus.APPROVED);
		lastTask=code;
		this.taskStack.push(lastTask);
		return this;
	}
	
	public Workflow start() {
		taskStack.push(lastTask);
		return this;
	}
	
	public Workflow end() {
		taskStack.push(lastTask);
		return this;
	}
	
	public Workflow withTask(String childCode, TaskStatus parentStatus) {
		String parentCode = taskStack.isEmpty()?ROOT:taskStack.peek();
		Task parentTask = root.find(parentCode);
		if (parentTask == null) {
			throw new RuntimeException(String.format("Can not add task. No parent task found {%s}", parentCode));
		}
		parentTask.addChild(childCode, parentStatus);
		lastTask=childCode;
		return this;
	}
	
	public Workflow nextTask(String childCode, TaskStatus parentStatus) {
		String parentCode = lastTask;
		Task parentTask = root.find(parentCode);
		if (parentTask == null) {
			throw new RuntimeException(String.format("Can not add task. No parent task found {%s}", parentCode));
		}
		parentTask.addChild(childCode, parentStatus);
		lastTask=childCode;
		return this;
	}
	
//	public Workflow withTask(String childCode, String parentCode, TaskStatus parentStatus) {
//		Task parentTask = root.find(parentCode);
//		if (parentTask == null) {
//			throw new RuntimeException(String.format("Can not add task. No parent task found {%s}", parentCode));
//		}
//		parentTask.addChild(childCode, parentStatus);
//		return this;
//	}
	
	public Workflow updateTask(String code, TaskStatus status) {
		Task task = root.find(code);
		if(task==null) {
			throw new RuntimeException(String.format("Task not found for the given code {%s}", code));
		}
		if(!task.isActive()) {
			throw new RuntimeException(String.format("Can not update the task. Task is not active {%s}", code));
		}
		task.setStatus(status);		
		return this;
	}
	
	public Map<String,Task> nextTaskOn(String code, TaskStatus taskStatus) {
		Task task = this.root.find(code);
		return task.getChildTasks().get(taskStatus);
	}
	
	public boolean isActive(String code) {
		Task task = this.root.find(code);
		return task.isActive();
	}
	
	public DependsOn dependsOn(){
		return null;
	}
	
	
	private static class DependsOn {
		private String code;
		private TaskStatus status;
	}
	
}
