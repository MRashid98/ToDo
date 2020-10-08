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
import com.qa.todo.dto.TaskListDTO;
import com.qa.todo.persistance.domain.TaskList;
import com.qa.todo.persistance.repo.TaskListRepo;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskListControllerIntegrationTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	private TaskListRepo repo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ObjectMapper objMapper;

	private long id;
	private TaskList testCollection;
	private TaskList testCollPlusId;

	private TaskListDTO mapToDTO(TaskList taskList) {
		return this.mapper.map(taskList, TaskListDTO.class);
	}

	@BeforeEach
	void init() {
		this.repo.deleteAll();
		this.testCollection = new TaskList("Test Collection");
		this.testCollPlusId = this.repo.save(this.testCollection);
		this.id = this.testCollPlusId.getId();
	}

	@Test
	void testCreate() throws Exception {
		this.mock
				.perform(request(HttpMethod.POST, "/tasklist/create").contentType(MediaType.APPLICATION_JSON)
						.content(this.objMapper.writeValueAsString(testCollection)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().json(this.objMapper.writeValueAsString(testCollPlusId)));
	}

	@Test
	void testRead() throws Exception {
		this.mock.perform(request(HttpMethod.GET, "/tasklist/read/" + this.id).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(this.objMapper.writeValueAsString(this.testCollection)));
	}

	@Test
	void testReadAll() throws Exception {
		List<TaskList> collList = new ArrayList<>();
		collList.add(this.testCollPlusId);

		String content = this.mock
				.perform(request(HttpMethod.GET, "/tasklist/readall").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assertEquals(this.objMapper.writeValueAsString(collList), content);
	}

	@Test
	void testUpdate() throws Exception {
		TaskList newColl = new TaskList("New Collection");
		TaskList updatedColl = new TaskList(newColl.getCollName());
		updatedColl.setId(this.id);

		String result = this.mock
				.perform(request(HttpMethod.PUT, "/tasklist/update/" + this.id).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(this.objMapper.writeValueAsString(newColl)))
				.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

		assertEquals(this.objMapper.writeValueAsString(this.mapToDTO(updatedColl)), result);
	}

	@Test
	void testDelete() throws Exception {
		this.mock.perform(request(HttpMethod.DELETE, "/tasklist/delete/" + this.id))
				.andExpect(status().isNoContent());
	}

}
