package com.qa.todo.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.todo.persistance.domain.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

}
