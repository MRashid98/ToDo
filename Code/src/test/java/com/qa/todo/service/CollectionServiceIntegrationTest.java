package com.qa.todo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.todo.dto.CollectionDTO;
import com.qa.todo.dto.TaskDTO;
import com.qa.todo.persistance.domain.Collection;
import com.qa.todo.persistance.domain.Task;
import com.qa.todo.persistance.repo.CollectionRepo;

@SpringBootTest
public class CollectionServiceIntegrationTest {

	@Autowired
	private CollectionRepo repo;

	@Autowired
	private CollectionService service;

	@Autowired
	private ModelMapper mapper;

	private CollectionDTO mapToDto(Collection collection) {
		return this.mapper.map(collection, CollectionDTO.class);
	}

	private Collection testCollection;
	private Collection testCollectionPlusId;
	private CollectionDTO testDto;

	private Long id;
	private final String collName = "Work";
	private final Task task = new Task("Task Name", "Task Desc");
	private final List<TaskDTO> tasks = null;

	@BeforeEach
	void init() {
		this.repo.deleteAll();
		this.testCollection = new Collection(this.collName);
		this.testCollectionPlusId = this.repo.save(this.testCollection);
		this.testDto = this.mapToDto(testCollectionPlusId);
		this.id = this.testCollectionPlusId.getId();
	}

	@Test
	void testCreate() {
		assertThat(this.testDto).isEqualTo(this.service.create(testCollection));
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
		CollectionDTO newColl = new CollectionDTO(null, "Updated Name", null);
		CollectionDTO updatedColl = new CollectionDTO(this.id, newColl.getCollName(), null);
		updatedColl.setId(this.id);
		assertThat(updatedColl).isEqualTo(this.service.update(newColl, this.id));
	}

	@Test
	void testDelete() {
		assertThat(this.service.delete(this.id)).isTrue();
	}

}
