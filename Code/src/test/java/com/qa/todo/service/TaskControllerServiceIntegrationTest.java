package com.qa.todo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.todo.dto.TaskDTO;
import com.qa.todo.dto.TaskListDTO;
import com.qa.todo.persistance.domain.TaskList;
import com.qa.todo.persistance.repo.TaskListRepo;

@SpringBootTest
public class TaskControllerServiceIntegrationTest {

	@Autowired
	private TaskListRepo repo;

	@Autowired
	private TaskListService service;

	@Autowired
	private ModelMapper mapper;

	private TaskList testTaskList;
	private TaskList testTaskListPlusId;
	private TaskListDTO testDto;

	private List<TaskDTO> tasks = new ArrayList<>();

	private Long id;
	private final String collName = "Work";

	private TaskListDTO mapToDto(TaskList taskList) {
		return this.mapper.map(taskList, TaskListDTO.class);
	}

	@BeforeEach
	void init() {
		this.repo.deleteAll();
		this.testTaskList = new TaskList(this.collName);
		this.testTaskListPlusId = this.repo.save(this.testTaskList);
		this.testDto = this.mapToDto(this.testTaskListPlusId);
		this.id = this.testTaskListPlusId.getId();
	}

	@Test
	void testCreate() {
		assertThat(this.testDto).isEqualTo(this.service.create(testTaskList));
	}

	@Test
	void testReadOne() {
		assertThat(this.testDto).isEqualTo(this.service.read(this.id));
	}

	@Test
	void testReadAll() {
		assertThat(Stream.of(this.testDto).collect(Collectors.toList())).isEqualTo(this.service.read());

	}

	@Test
	void testUpdate() {
		TaskListDTO newColl = new TaskListDTO(null, "Updated Name", this.tasks);
		TaskListDTO updatedColl = new TaskListDTO(this.id, newColl.getCollName(), newColl.getTasks());
		assertThat(updatedColl).isEqualTo(this.service.update(newColl, this.id));
	}

	@Test
	void testDelete() {
		assertThat(this.service.delete(this.id)).isTrue();
	}

}
