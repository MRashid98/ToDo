package com.qa.todo.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.todo.dto.TaskDTO;
import com.qa.todo.persistance.domain.Task;
import com.qa.todo.service.TaskService;

@SpringBootTest
public class TaskControllerUnitTest {

	@Autowired
	private TaskController controller;

	@Autowired
	private ModelMapper mapper;

	@MockBean
	private TaskService service;

	private TaskDTO mapToDto(Task task) {
		return this.mapper.map(task, TaskDTO.class);
	}

	private List<Task> taskList;
	private Task testTask;
	private Task testTaskPlusId;
	private TaskDTO dto;

	private final Long id = 1L;
	private final String taskName = "Task Name";
	private final String taskDesc = "Task Desc";

	@BeforeEach
	void init() {
		this.taskList = new ArrayList<>();
		this.testTask = new Task(this.taskName, this.taskDesc);
		this.testTaskPlusId = new Task(testTask.getTaskName(), testTask.getTaskDesc());
		this.testTaskPlusId.setId(this.id);
		this.taskList.add(testTaskPlusId);
		this.dto = this.mapToDto(testTaskPlusId);
	}

	@Test
	void testCreate() {
		when(this.service.create(testTask)).thenReturn(this.dto);

		TaskDTO created = this.dto;
		assertThat(new ResponseEntity<TaskDTO>(created, HttpStatus.CREATED))
				.isEqualTo(this.controller.create(testTask));

		verify(this.service, times(1)).create(this.testTask);
	}

	@Test
	void testRead() {
		when(this.service.read(this.id)).thenReturn(this.dto);

		TaskDTO readOne = this.dto;
		assertThat(new ResponseEntity<TaskDTO>(readOne, HttpStatus.OK)).isEqualTo(this.controller.read(this.id));

		verify(this.service, times(1)).read(this.id);
	}

	@Test
	void testReadAll() {
		when(this.service.read()).thenReturn(this.taskList.stream().map(this::mapToDto).collect(Collectors.toList()));

		assertThat(this.controller.read().getBody().isEmpty()).isFalse();

		verify(this.service, times(1)).read();
	}

	@Test
	void testUpdate() {
		TaskDTO newTask = new TaskDTO(null, "Updated Name", "Updated Description");
		TaskDTO newTaskPlusId = new TaskDTO(this.id, newTask.getTaskName(), newTask.getTaskDesc());

		when(this.service.update(newTask, this.id)).thenReturn(newTaskPlusId);

		assertThat(new ResponseEntity<TaskDTO>(newTaskPlusId, HttpStatus.ACCEPTED))
				.isEqualTo(this.controller.update(this.id, newTask));
		verify(this.service, times(1)).update(newTask, this.id);
	}

	@Test
	void testDelete() {
		this.controller.delete(this.id);
		verify(this.service, times(1)).delete(this.id);
	}

}
