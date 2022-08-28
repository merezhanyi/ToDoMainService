package com.crg.todo.tasks_list.repository;

import com.crg.todo.tasks_list.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Task, Long> {
}
