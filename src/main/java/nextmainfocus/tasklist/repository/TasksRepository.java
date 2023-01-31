package nextmainfocus.tasklist.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import nextmainfocus.tasklist.entity.Task;

public interface TasksRepository extends JpaRepository<Task, UUID> {
}
