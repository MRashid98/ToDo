package com.qa.todo.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.todo.persistance.domain.TaskList;

@Repository
public interface TaskListRepo extends JpaRepository<TaskList, Long> {

}
