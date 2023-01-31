package nextmainfocus.task.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import nextmainfocus.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
