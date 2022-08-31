package com.crg.todo.tasks_list.repository;

import com.crg.todo.tasks_list.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Task, Long> {
}
