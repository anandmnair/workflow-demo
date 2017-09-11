package com.anand.wf.demo.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import com.anand.wf.demo.core.status.TaskStatus;

public class Task {

	private Map<TaskStatus, Map<String,Task>> parentTasks = new LinkedHashMap<>();

	private String code;

	private TaskStatus status;
	
	private Map<TaskStatus, Map<String,Task>> childTasks = new LinkedHashMap<>();

	public Task(String code) {
		this.code = code;
		this.status = TaskStatus.TODO;
	}
	
	public Task(String code, TaskStatus status) {
		this.code = code;
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Map<TaskStatus, Map<String, Task>> getParentTasks() {
		return parentTasks;
	}

	public void setParentTasks(Map<TaskStatus, Map<String, Task>> parentTasks) {
		this.parentTasks = parentTasks;
	}

	public Map<TaskStatus, Map<String, Task>> getChildTasks() {
		return childTasks;
	}

	public void setChildTasks(Map<TaskStatus, Map<String, Task>> childTasks) {
		this.childTasks = childTasks;
	}

	public Task find(String code) {

		return find(code, this);
	}

	private Task find(String code, Task root) {

		if (root.getCode().equals(code)) {
			return root;
		}

		if (MapUtils.isEmpty(root.getChildTasks())) {
			return null;
		}

		for (Map<String,Task> taskMap : root.getChildTasks().values()) {
			if(taskMap.containsKey(code)) {
				return taskMap.get(code);
			}
			for (Task task : taskMap.values()) {
				Task result = find(code, task);
				if (result != null) {
					return result;
				}
			}
		}
		
		return null;
	}

	public void addChild(String childCode, TaskStatus parentStatus) {
		Task childTask = createTask(childCode);
		if (!childTask.getParentTasks().containsKey(parentStatus)) {
			childTask.getParentTasks().put(parentStatus, new LinkedHashMap<>());
		}
		childTask.getParentTasks().get(parentStatus).put(getCode(), this);

		if (!getChildTasks().containsKey(parentStatus)) {
			getChildTasks().put(parentStatus, new LinkedHashMap<>());
		}
		getChildTasks().get(parentStatus).put(childTask.getCode(),childTask);
	}
	
	public boolean isActive() {
		if(MapUtils.isEmpty(getParentTasks())) {
			return true;
		}
		for(Map.Entry<TaskStatus, Map<String,Task>> entry : getParentTasks().entrySet()) {
			TaskStatus parentStatus = entry.getKey();
			for(Task parentTask : entry.getValue().values()) {
				if(parentTask.getStatus()!=parentStatus) {
					return false;
				}
			}
		}
		return true;
	}
	
	private Task createTask(String code) {
		Task task = find(code);
		if(task==null) {
			task=new Task(code);
		}
		return task;
	}

}
