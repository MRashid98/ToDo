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
public class TaskListServiceUnitTest {

	@Autowired
	private TaskListRepo repo;

	@Autowired
	private TaskListService service;

	@Autowired
	private ModelMapper mapper;

	private TaskListDTO mapToDto(TaskList taskList) {
		return this.mapper.map(taskList, TaskListDTO.class);
	}

	private TaskList testTaskList;
	private TaskList testTaskListPlusId;
	private TaskListDTO testDto;

	private Long id;
	private final String taskListName = "Collection Tests";
	private List<TaskDTO> tasks = new ArrayList<>();

	@BeforeEach
	void init() {
		this.repo.deleteAll();
		this.testTaskList = new TaskList(this.taskListName);
		this.testTaskListPlusId = this.repo.save(this.testTaskList);
		this.testDto = this.mapToDto(testTaskListPlusId);
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
		assertThat(this.service.read()).isEqualTo(Stream.of(this.testDto).collect(Collectors.toList()));
	}

	@Test
	void testUpdate() {
		TaskListDTO newTaskList = new TaskListDTO(null, "Updated Name", this.tasks);
		TaskListDTO updatedTaskList = new TaskListDTO(this.id, newTaskList.getCollName(), newTaskList.getTasks());

		assertThat(updatedTaskList).isEqualTo(this.service.update(newTaskList, this.id));
	}

	@Test
	void testDelete() {
		assertThat(this.service.delete(this.id)).isTrue();
	}
}
