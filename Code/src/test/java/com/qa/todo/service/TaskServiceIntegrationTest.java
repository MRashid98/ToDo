package com.qa.todo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.todo.dto.TaskDTO;
import com.qa.todo.persistance.domain.Task;
import com.qa.todo.persistance.repo.TaskRepo;

@SpringBootTest
public class TaskServiceIntegrationTest {

	@Autowired
	private TaskRepo repo;

	@Autowired
	private TaskService service;

	@Autowired
	private ModelMapper mapper;

	private TaskDTO mapToDto(Task task) {
		return this.mapper.map(task, TaskDTO.class);
	}

	private Task testTask;
	private Task testTaskPlusId;
	private TaskDTO testDto;

	private Long id;
	private final String taskName = "TDL Tests";
	private final String taskDesc = "To Create Test Classes For project";

	@BeforeEach
	void init() {
		this.repo.deleteAll();
		this.testTask = new Task(this.taskName, this.taskDesc);
		this.testTaskPlusId = this.repo.save(this.testTask);
		this.testDto = this.mapToDto(testTaskPlusId);
		this.id = this.testTaskPlusId.getId();
	}

	@Test
	void testCreate() {
		assertThat(this.testDto).isEqualTo(this.service.create(testTask));
	}

	@Test
	void testReadOne() {
		assertThat(this.testDto).isEqualTo(this.service.read(this.id));
	}

	@Test
	void testReadAll() {
		assertThat(this.service.read()).isEqualTo(Stream.of(this.testDto).collect(Collectors.toList()));
	}

	@Test
	void testUpdate() {
		TaskDTO newTask = new TaskDTO(null, "Updated Name", "Updated and Recent Description");
		TaskDTO updatedTask = new TaskDTO(this.id, newTask.getTaskName(), newTask.getTaskDesc());

		assertThat(updatedTask).isEqualTo(this.service.update(newTask, this.id));
	}

	@Test
	void testDelete() {
		assertThat(this.service.delete(this.id)).isTrue();
	}

}
