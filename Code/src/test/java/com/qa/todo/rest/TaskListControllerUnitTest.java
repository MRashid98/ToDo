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
import com.qa.todo.dto.TaskListDTO;
import com.qa.todo.persistance.domain.TaskList;
import com.qa.todo.service.TaskListService;

@SpringBootTest
public class TaskListControllerUnitTest {

	@Autowired
	private TaskListController controller;

	@Autowired
	private ModelMapper mapper;

	@MockBean
	private TaskListService service;

	private TaskListDTO mapToDto(TaskList taskList) {
		return this.mapper.map(taskList, TaskListDTO.class);
	}

	private List<TaskList> taskList;
	private TaskList testTaskList;
	private TaskList testTaskListPlusId;
	private TaskListDTO dto;

	private final Long id = 1L;
	private final String taskListName = "Collection Name";
	private List<TaskDTO> tasks = new ArrayList<>();

	@BeforeEach
	void init() {
		this.taskList = new ArrayList<>();
		this.testTaskList = new TaskList(this.taskListName);
		this.testTaskListPlusId = new TaskList(testTaskList.getCollName());
		this.testTaskListPlusId.setId(this.id);
		this.taskList.add(testTaskListPlusId);
		this.dto = this.mapToDto(testTaskListPlusId);
	}

	@Test
	void testCreate() {
		when(this.service.create(testTaskList)).thenReturn(this.dto);

		TaskListDTO created = this.dto;
		assertThat(new ResponseEntity<TaskListDTO>(created, HttpStatus.CREATED))
				.isEqualTo(this.controller.create(testTaskList));

		verify(this.service, times(1)).create(this.testTaskList);
	}

	@Test
	void testRead() {
		when(this.service.read(this.id)).thenReturn(this.dto);

		TaskListDTO readOne = this.dto;
		assertThat(new ResponseEntity<TaskListDTO>(readOne, HttpStatus.OK))
				.isEqualTo(this.controller.readById(this.id));

		verify(this.service, times(1)).read(this.id);
	}

	@Test
	void testReadAll() {
		when(this.service.read()).thenReturn(this.taskList.stream().map(this::mapToDto).collect(Collectors.toList()));

		assertThat(this.controller.readAll().getBody().isEmpty()).isFalse();

		verify(this.service, times(1)).read();
	}

	@Test
	void testUpdate() {
		TaskListDTO newTaskList = new TaskListDTO(null, "Updated Name", this.tasks);
		TaskListDTO newTaskListPlusId = new TaskListDTO(this.id, newTaskList.getCollName(), newTaskList.getTasks());

		when(this.service.update(newTaskList, this.id)).thenReturn(newTaskListPlusId);

		assertThat(new ResponseEntity<TaskListDTO>(newTaskListPlusId, HttpStatus.ACCEPTED))
				.isEqualTo(this.controller.update(this.id, newTaskList));
		verify(this.service, times(1)).update(newTaskList, this.id);
	}

	@Test
	void testDelete() {
		this.controller.deleteById(this.id);
		verify(this.service, times(1)).delete(this.id);
	}

}
