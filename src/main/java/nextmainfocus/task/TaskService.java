package nextmainfocus.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;

	public Task createTask(Task task) {
		return taskRepository.save(task);
	}

	public List<Task> findAllTasks(String page, String sorting, String field) {
		Sort sort = null;
		if (sorting != null && field != null) {
			if ("asc".equals(sorting)) {
				sort = Sort.by(field).ascending();
			} else if ("desc".equals(sorting)) {
				sort = Sort.by(field).descending();
			}
		}

		if (page != null && sort != null) {
			Pageable pageable = PageRequest.of(Integer.valueOf(page), 10, sort);
			return taskRepository.findAll(pageable).getContent();
		} else if (page != null) {
			Pageable pageable = PageRequest.of(Integer.valueOf(page), 10);
			return taskRepository.findAll(pageable).getContent();
		}

		if (sort != null) {
			return taskRepository.findAll(sort);
		} else {
			return taskRepository.findAll();
		}
	}

	public Task findById(UUID id) {
		Optional<Task> possibleTask = taskRepository.findById(id);

		if (possibleTask.isPresent()) {
			return possibleTask.get();
		} else {
			return null;
		}
	}

	public Task updateTask(UUID id, Task task) {
		Task updatedTask = new Task();
		Optional<Task> possibleTask = taskRepository.findById(id);
		Task existingTask;

		if (possibleTask.isPresent()) {
			existingTask = possibleTask.get();
			updatedTask.setId(existingTask.getId());
		} else {
			return null;
		}

		if (task.getDateTime() != null) {
			updatedTask.setDateTime(task.getDateTime());
		} else {
			updatedTask.setDateTime(existingTask.getDateTime());
		}

		if (task.getDescription() != null) {
			updatedTask.setDescription(task.getDescription());
		} else {
			updatedTask.setDescription(existingTask.getDescription());
		}

		updatedTask.setDone(task.isDone());

		return taskRepository.save(updatedTask);
	}

	public void deleteTask(UUID id) {
		taskRepository.deleteById(id);
	}
}
