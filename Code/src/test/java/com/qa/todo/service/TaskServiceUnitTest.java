package com.qa.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.todo.dto.TaskDTO;
import com.qa.todo.persistance.domain.Task;
import com.qa.todo.persistance.repo.TaskRepo;

@SpringBootTest
public class TaskServiceUnitTest {

	@Autowired
	private TaskService service;

	@MockBean
	private TaskRepo repo;

	@MockBean
	private ModelMapper mapper;

	private List<Task> taskList;
	private Task testTask;
	private Task testTaskPlusId;
	private TaskDTO dto;

	final Long id = 1L;
	final String taskName = "Unit Test";
	final String taskDesc = "Unit test class for Task";

	@BeforeEach
	void init() {
		this.taskList = new ArrayList<>();
		this.testTask = new Task(taskName, taskDesc);
		this.taskList.add(testTask);
		this.testTaskPlusId = new Task(testTask.getTaskName(), testTask.getTaskDesc());
		this.testTaskPlusId.setId(id);
		this.dto = mapper.map(testTaskPlusId, TaskDTO.class);
	}

	@Test
	void testCreate() {
		when(this.repo.save(this.testTask)).thenReturn(this.testTaskPlusId);

		when(this.mapper.map(this.testTaskPlusId, TaskDTO.class)).thenReturn(this.dto);

		TaskDTO expected = this.dto;
		TaskDTO actual = this.service.create(this.testTask);
		assertThat(expected).isEqualTo(actual);

		verify(this.repo, times(1)).save(this.testTask);
	}

	@Test
	void readTest() {

		when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testTaskPlusId));

		when(this.mapper.map(testTaskPlusId, TaskDTO.class)).thenReturn(dto);

		assertThat(this.dto).isEqualTo(this.service.read(this.id));

		verify(this.repo, times(1)).findById(this.id);
	}

	@Test
	void readAllTest() {
		
		when(this.repo.findAll()).thenReturn(this.taskList);

		when(this.mapper.map(this.testTaskPlusId, TaskDTO.class)).thenReturn(dto);

		assertThat(this.service.read().isEmpty()).isFalse();

		verify(this.repo, times(1)).findAll();
	}

	@Test
	void updateTest() {
		Task task = new Task("Updated", "Testing Updating Function");
		task.setId(this.id);

		TaskDTO dto = new TaskDTO(null, "Updated", "Testing Updating Function");

		Task updatedTask = new Task(dto.getTaskName(), dto.getTaskDesc());
		updatedTask.setId(this.id);

		TaskDTO updatedDto = new TaskDTO(this.id, updatedTask.getTaskName(), updatedTask.getTaskDesc());

		when(this.repo.findById(this.id)).thenReturn(Optional.of(task));
		when(this.repo.save(task)).thenReturn(updatedTask);
		when(this.mapper.map(updatedTask, TaskDTO.class)).thenReturn(updatedDto);

		assertThat(updatedDto).isEqualTo(this.service.update(dto, this.id));

		verify(this.repo, times(1)).findById(1L);
		verify(this.repo, times(1)).save(updatedTask);
	}

	@Test
	void deleteTest() {
		when(this.repo.existsById(id)).thenReturn(true, false);
		assertThat(this.service.delete(id)).isTrue();
		verify(this.repo, times(1)).deleteById(id);
		verify(this.repo, times(2)).existsById(id);
	}

}
