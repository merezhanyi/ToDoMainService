package nextmainfocus.tasks_list.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nextmainfocus.tasks_list.entity.Task;

public interface TasksRepository extends JpaRepository<Task, Long> {
}
