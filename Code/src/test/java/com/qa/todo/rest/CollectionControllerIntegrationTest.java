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
import com.qa.todo.dto.CollectionDTO;
import com.qa.todo.persistance.domain.Collection;
import com.qa.todo.persistance.repo.CollectionRepo;

@SpringBootTest
@AutoConfigureMockMvc
public class CollectionControllerIntegrationTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	private CollectionRepo repo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ObjectMapper objMapper;

	private long id;
	private Collection testCollection;
	private Collection testCollPlusId;

	@BeforeEach
	void init() {
		this.repo.deleteAll();
		this.testCollection = new Collection("Test Collection");
		this.testCollPlusId = this.repo.save(this.testCollection);
		this.id = this.testCollPlusId.getId();
	}

	private CollectionDTO mapToDTO(Collection collection) {
		return this.mapper.map(collection, CollectionDTO.class);
	}

	@Test
	void testCreate() throws Exception {
		this.mock
				.perform(request(HttpMethod.POST, "/collection/create").contentType(MediaType.APPLICATION_JSON)
						.content(this.objMapper.writeValueAsString(testCollection)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().json(this.objMapper.writeValueAsString(testCollPlusId)));
	}

	@Test
	void testRead() throws Exception {
		this.mock.perform(request(HttpMethod.GET, "/collection/read/" + this.id).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(this.objMapper.writeValueAsString(this.testCollection)));
	}

	@Test
	void testReadAll() throws Exception {
		List<Collection> collList = new ArrayList<>();
		collList.add(this.testCollPlusId);

		String content = this.mock
				.perform(request(HttpMethod.GET, "/collection/readall").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assertEquals(this.objMapper.writeValueAsString(collList), content);
	}

	@Test
	void testUpdate() throws Exception {
		Collection newColl = new Collection("New Collection");
		Collection updatedColl = new Collection(newColl.getCollName());
		updatedColl.setId(this.id);

		String result = this.mock
				.perform(request(HttpMethod.PUT, "/controller/update/" + this.id).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(this.objMapper.writeValueAsString(newColl)))
				.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

		assertEquals(this.objMapper.writeValueAsString(this.mapToDTO(updatedColl)), result);
	}

	@Test
	void testDelete() throws Exception {
		this.mock.perform(request(HttpMethod.DELETE, "/collection/delete/" + this.id))
				.andExpect(status().isNoContent());
	}

}
