package com.qa.todo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.todo.dto.TaskListDTO;
import com.qa.todo.exceptions.TaskListNotFoundException;
import com.qa.todo.persistance.domain.TaskList;
import com.qa.todo.persistance.repo.TaskListRepo;
import com.qa.todo.utils.SpringBeanUtils;

@Service
public class TaskListService {

	private TaskListRepo repo;
	private ModelMapper mapper;

	@Autowired
	public TaskListService(TaskListRepo repo, ModelMapper mapper) {
		super();
		this.repo = repo;
		this.mapper = mapper;
	}

	private TaskListDTO mapToDto(TaskList coll) {
		return this.mapper.map(coll, TaskListDTO.class);
	}

	// Creating a Collection
	public TaskListDTO create(TaskList coll) {
		TaskList created = this.repo.save(coll);
		return this.mapToDto(created);
	}

	// Read all collections
	public List<TaskListDTO> read() {
		return this.repo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	// Read Collection by id
	public TaskListDTO read(Long id) {
		TaskList found = this.repo.findById(id).orElseThrow(TaskListNotFoundException::new);
		return this.mapToDto(found);
	}

	// Update Collection
	public TaskListDTO update(TaskListDTO dto, Long id) {
		TaskList target = this.repo.findById(id).orElseThrow(TaskListNotFoundException::new);
		target.setCollName(dto.getCollName());
		SpringBeanUtils.mergeNotNullObject(dto, target);
		return this.mapToDto(this.repo.save(target));
	}

	// Delete Collection
	public boolean delete(Long id) {
		if (!this.repo.existsById(id)) {
			throw new TaskListNotFoundException();
		}
		this.repo.deleteById(id);
		return !this.repo.existsById(id);
	}
}
