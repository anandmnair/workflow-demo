package com.anand.wf.demo.core;

import java.util.Map;
import java.util.Stack;

import com.anand.wf.demo.core.status.TaskStatus;

public class WorkflowOld {
	
	private static final String ROOT="root";
	
	private Task root = new Task(ROOT, TaskStatus.APPROVED);
	
	private String lastTask = ROOT;
	
	private Stack<String> taskStack = new Stack<>();
	
	public WorkflowOld start() {
		taskStack.push(lastTask);
		return this;
	}
	
	public WorkflowOld end() {
		taskStack.pop();
		return this;
	}
	
	public WorkflowOld withTask(String code) {
		this.root.addChild(code, TaskStatus.APPROVED);
		lastTask=code;
		return this;
	}
	
	public WorkflowOld withTask(String childCode, TaskStatus parentStatus) {
		String parentCode = taskStack.isEmpty()?ROOT:taskStack.peek();
		Task parentTask = root.find(parentCode);
		if (parentTask == null) {
			throw new RuntimeException(String.format("Can not add task. No parent task found {%s}", parentCode));
		}
		parentTask.addChild(childCode, parentStatus);
		lastTask=childCode;
		return this;
	}
	
	public WorkflowOld nextTask(String childCode, TaskStatus parentStatus) {
		String parentCode = lastTask;
		Task parentTask = root.find(parentCode);
		if (parentTask == null) {
			throw new RuntimeException(String.format("Can not add task. No parent task found {%s}", parentCode));
		}
		parentTask.addChild(childCode, parentStatus);
		lastTask=childCode;
		return this;
	}
	
	public WorkflowOld updateTask(String code, TaskStatus status) {
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
	
}
