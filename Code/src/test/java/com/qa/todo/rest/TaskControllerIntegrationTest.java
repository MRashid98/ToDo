package com.qa.todo.rest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.todo.dto.TaskDTO;
import com.qa.todo.persistance.domain.Task;
import com.qa.todo.persistance.repo.TaskRepo;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	private TaskRepo repo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ObjectMapper objMapper;

	private Task testTask;
	private Task testTaskPlusId;
	private TaskDTO dto;

	private Long id;

	private TaskDTO mapToDto(Task task) {
		return this.mapper.map(task, TaskDTO.class);
	}

	@BeforeEach
	void init() {
		this.repo.deleteAll();
		this.testTask = new Task("Test Task", "Test Task Description");
		this.testTaskPlusId = this.repo.save(this.testTask);
		this.dto = this.mapToDto(this.testTaskPlusId);
		this.id = this.testTaskPlusId.getId();
	}

	@Test
	void testCreate() throws Exception {
		this.mock
				.perform(request(HttpMethod.POST, "/task/create").contentType(MediaType.APPLICATION_JSON)
						.content(this.objMapper.writeValueAsString(this.testTask)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(content().json(this.objMapper.writeValueAsString(this.dto)));
	}

	@Test
	void testRead() throws Exception {
		this.mock.perform(request(HttpMethod.GET, "/task/read/" + this.id).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(this.objMapper.writeValueAsString(this.dto)));
	}

	@Test
	void testReadAll() throws Exception {
		List<TaskDTO> taskList = new ArrayList<>();
		taskList.add(this.dto);

		String outcome = this.mock.perform(request(HttpMethod.GET, "/task/readall").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(this.objMapper.writeValueAsString(taskList), outcome);
	}

	@Test
	void testUpdate() throws Exception {
		TaskDTO newTask = new TaskDTO(null, "Updated Name", "Updated Description");
		Task updatedTask = new Task(newTask.getTaskName(), newTask.getTaskDesc());
		updatedTask.setId(this.id);

		String outcome = this.mock
				.perform(request(HttpMethod.PUT, "/task/update/" + this.id).contentType(MediaType.APPLICATION_JSON)
						.content(this.objMapper.writeValueAsString(newTask)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

		assertEquals(this.objMapper.writeValueAsString(this.mapToDto(updatedTask)), outcome);
	}

	@Test
	void testDelete() throws Exception {
		this.mock.perform(request(HttpMethod.DELETE, "/task/delete/" + this.id)).andExpect(status().isNoContent());
	}

}
