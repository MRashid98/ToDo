package com.qa.todo.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.todo.dto.CollectionDTO;
import com.qa.todo.persistance.domain.Collection;
import com.qa.todo.persistance.repo.CollectionRepo;
import com.qa.todo.utils.SpringBeanUtils;

@Service
public class CollectionService {

	private CollectionRepo repo;
	private ModelMapper mapper;

	@Autowired
	public CollectionService(CollectionRepo repo, ModelMapper mapper) {
		super();
		this.repo = repo;
		this.mapper = mapper;
	}

	private CollectionDTO mapToDto(Collection coll) {
		return this.mapper.map(coll, CollectionDTO.class);
	}

	// Creating a Collection
	public CollectionDTO create(Collection coll) {
		Collection created = this.repo.save(coll);
		return this.mapToDto(created);
	}

	// Read all collections
	public List<CollectionDTO> read() {
		return this.repo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	// Read Collection by id
	public CollectionDTO read(Long id) {
		Collection found = this.repo.findById(id).orElseThrow(EntityNotFoundException::new);
		return this.mapToDto(found);
	}

	// Update Collection
	public CollectionDTO update(CollectionDTO dto, Long id) {
		Collection target = this.repo.findById(id).orElseThrow(EntityNotFoundException::new);
		SpringBeanUtils.mergeNotNullObject(dto, target);
		return this.mapToDto(this.repo.save(target));
	}

	// Delete Collection
	public boolean delete(Long id) {
		this.repo.deleteById(id);
		return !this.repo.existsById(id);
	}
}
