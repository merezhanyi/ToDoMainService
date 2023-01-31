package nextmainfocus.tasklist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nextmainfocus.tasklist.entity.Task;
import nextmainfocus.tasklist.repository.TasksRepository;

@Service
public class TasksService {
	@Autowired
	private TasksRepository tasksRepository;

	public Task createTask(Task task) {
		return tasksRepository.save(task);
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
			return tasksRepository.findAll(pageable).getContent();
		} else if (page != null) {
			Pageable pageable = PageRequest.of(Integer.valueOf(page), 10);
			return tasksRepository.findAll(pageable).getContent();
		}

		if (sort != null) {
			return tasksRepository.findAll(sort);
		} else {
			return tasksRepository.findAll();
		}
	}

	public Task findById(UUID id) {
		Optional<Task> possibleTask = tasksRepository.findById(id);

		if (possibleTask.isPresent()) {
			return possibleTask.get();
		} else {
			return null;
		}
	}

	public Task updateTask(UUID id, Task task) {
		Task updatedTask = new Task();
		Optional<Task> possibleTask = tasksRepository.findById(id);
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

		return tasksRepository.save(updatedTask);
	}

	public void deleteTask(UUID id) {
		tasksRepository.deleteById(id);
	}
}
