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

import com.qa.todo.dto.TaskListDTO;
import com.qa.todo.persistance.domain.TaskList;
import com.qa.todo.service.TaskListService;

@RestController
@CrossOrigin
@RequestMapping("/tasklist")
public class TaskListController {

	private TaskListService service;

	public TaskListController(TaskListService service) {
		super();
		this.service = service;
	}

	@PostMapping("/create")
	public ResponseEntity<TaskListDTO> create(@RequestBody TaskList coll) {
		TaskListDTO created = this.service.create(coll);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@GetMapping("/readall")
	public ResponseEntity<List<TaskListDTO>> readAll() {
		return ResponseEntity.ok(this.service.read());
	}

	@GetMapping("/read/{id}")
	public ResponseEntity<TaskListDTO> readById(@PathVariable Long id) {
		return ResponseEntity.ok(this.service.read(id));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<TaskListDTO> update(@PathVariable Long id, @RequestBody TaskListDTO bandDto) {
		return new ResponseEntity<>(this.service.update(bandDto, id), HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<TaskListDTO> deleteById(@PathVariable Long id) {
		return this.service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
