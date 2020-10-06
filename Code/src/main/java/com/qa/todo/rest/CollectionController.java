package com.qa.todo.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.todo.dto.CollectionDTO;
import com.qa.todo.persistance.domain.Collection;
import com.qa.todo.service.CollectionService;

@RestController
@CrossOrigin
@RequestMapping("/collection")
public class CollectionController {

	private CollectionService service;

	public CollectionController(CollectionService service) {
		super();
		this.service = service;
	}

	@PostMapping("/create")
	public ResponseEntity<CollectionDTO> create(@RequestBody Collection coll) {
		CollectionDTO created = this.service.create(coll);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@GetMapping("/readall")
	public ResponseEntity<List<CollectionDTO>> readAll() {
		return ResponseEntity.ok(this.service.read());
	}

	@GetMapping("/read/{id}")
	public ResponseEntity<CollectionDTO> readById(@PathVariable Long id) {
		return ResponseEntity.ok(this.service.read(id));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<CollectionDTO> update(@PathVariable Long id, @RequestBody CollectionDTO bandDto) {
		return new ResponseEntity<>(this.service.update(bandDto, id), HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<CollectionDTO> deleteById(@PathVariable Long id) {
		return this.service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
