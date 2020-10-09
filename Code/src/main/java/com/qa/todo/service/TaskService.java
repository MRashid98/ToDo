package com.qa.todo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.todo.dto.TaskDTO;
import com.qa.todo.exceptions.TaskNotFoundException;
import com.qa.todo.persistance.domain.Task;
import com.qa.todo.persistance.repo.TaskRepo;
import com.qa.todo.utils.SpringBeanUtils;

@Service
public class TaskService {

	private TaskRepo repo;
	private ModelMapper mapper;

	@Autowired
	private TaskService(TaskRepo repo, ModelMapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	private TaskDTO mapToDto(Task task) {
		return this.mapper.map(task, TaskDTO.class);
	}

	public TaskDTO create(Task task) {

		Task created = this.repo.save(task);
		return this.mapToDto(created);
	}

	// read
	public List<TaskDTO> read() {
		List<Task> found = this.repo.findAll();
		List<TaskDTO> stream = found.stream().map(this::mapToDto).collect(Collectors.toList());
		return stream;
	}

	// read
	public TaskDTO read(Long id) {
		Task found = this.repo.findById(id).orElseThrow(TaskNotFoundException::new);
		return this.mapToDto(found);
	}

	// update
	public TaskDTO update(TaskDTO dto, Long id) {
		Task target = this.repo.findById(id).orElseThrow(TaskNotFoundException::new);
		SpringBeanUtils.mergeNotNullObject(dto, target);
		return this.mapToDto(this.repo.save(target));
	}

	// delete {id}
	public boolean delete(Long id) {
		if (!this.repo.existsById(id)) {
			throw new TaskNotFoundException();
		}
		this.repo.deleteById(id);
		return !this.repo.existsById(id);
	}

}
