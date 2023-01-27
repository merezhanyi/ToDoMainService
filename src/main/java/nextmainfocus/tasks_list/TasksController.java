package nextmainfocus.tasks_list;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nextmainfocus.tasks_list.entity.Task;

// Spring boot workflow:
// front            back
// httpRequest > controller > service (magic here) > repo (interface) > entities (db)
//             < controller < service (magic here) < repo (interface) < entities (db)

@CrossOrigin(origins = "${client.url}")
@RestController
@RequestMapping("api/v1/")
public class TasksController {

	private static final Logger logger = LoggerFactory.getLogger(TasksController.class);

	@Autowired
	TasksService tasksService;

	@PostMapping(value = "tasks/", produces = "application/json")
	public ResponseEntity<?> createTask(@RequestBody Task task) {
		logger.info("Received request to create a new task:");
		logger.info("description: " + task.getDescription());
		logger.info("date/time: " + task.getDateTime());
		logger.info("done: " + task.isDone());

		HttpStatus status;

		try {
			Task newTask = tasksService.createTask(task);
			JSONObject body = new JSONObject();
			body.put("code", "OK");
			body.put("message", "Task was created.");
			body.put("id: ", newTask.getId());
			body.put("description: ", newTask.getDescription());
			body.put("dateTime: ", newTask.getDateTime());
			body.put("isDone: ", newTask.isDone());
			status = HttpStatus.OK;

			logger.info("Task was created with description: " + task.getDescription());
			return new ResponseEntity<>(body.toString(), status);
		} catch (Exception exception) {
			JSONObject body = new JSONObject();
			body.put("code", "INTERNAL_SERVER_ERROR");
			body.put("message", "Oops! Task creation failed.");
			status = HttpStatus.INTERNAL_SERVER_ERROR;

			logger.info("Task was not created with description: " + task.getDescription());
			logger.error("due to: " + exception.getMessage());
			return new ResponseEntity<>(body.toString(), status);
		}
	}

	@GetMapping(value = "tasks/", produces = "application/json")
	public ResponseEntity<?> findTasks(@RequestParam(required = false) String page,
			@RequestParam(required = false) String sort, @RequestParam(required = false) String field)
			throws JsonProcessingException {
		logger.info("Received request to retrieve tasks");

		HttpStatus status;
		List<Task> tasks = tasksService.findAllTasks(page, sort, field);

		if (tasks.size() > 0) {
			ObjectMapper mapper = new ObjectMapper();
			String body = mapper.writeValueAsString(tasks.size() + " tasks were found in the database");
			status = HttpStatus.OK;

			logger.info(tasks.size() + " tasks were found in the database");
			return new ResponseEntity<>(tasks, status);
		} else {
			JSONObject body = new JSONObject();
			body.put("code", "NOT_FOUND");
			body.put("message", "Oops! No tasks found");
			status = HttpStatus.NOT_FOUND;

			logger.error("No tasks were found in the database");
			return new ResponseEntity<>(body.toString(), status);
		}
	}

	@GetMapping(value = "tasks/{id}", produces = "application/json")
	public ResponseEntity<?> findTask(@PathVariable("id") Long id) throws JsonProcessingException {
		logger.info("Received request to retrieve task with ID: " + id);

		HttpStatus status;
		Optional<Task> task = tasksService.findById(id);

		if (task != null) {
			JSONObject body = new JSONObject();
			body.put("code", "OK");
			body.put("message", "Task was found.");
			body.put("id: ", task.get().getId());
			body.put("description: ", task.get().getDescription());
			body.put("dateTime: ", task.get().getDateTime());
			body.put("isDone: ", task.get().isDone());
			status = HttpStatus.OK;

			logger.info("A task found with ID: " + id);
			return new ResponseEntity<>(body.toString(), status);
		} else {
			JSONObject body = new JSONObject();
			body.put("code", "NOT_FOUND");
			body.put("message", "Oops! Task was not found.");
			body.put("id", id);
			status = HttpStatus.NOT_FOUND;

			logger.error("A task was not found with ID=" + id);
			return new ResponseEntity<>(body.toString(), status);
		}
	}

	@PutMapping(value = "tasks/{id}", produces = "application/json")
	public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody Task task)
			throws JsonProcessingException {
		logger.info("Received request to update task with ID=" + id);

		HttpStatus status;
		Task updatedTask = tasksService.updateTask(id, task);

		if (updatedTask != null) {
			JSONObject body = new JSONObject();
			body.put("code", "OK");
			body.put("message", "Task was updated.");
			body.put("id: ", updatedTask.getId());
			body.put("description: ", updatedTask.getDescription());
			body.put("dateTime: ", updatedTask.getDateTime());
			body.put("isDone: ", updatedTask.isDone());
			status = HttpStatus.OK;

			logger.info("A task updated with ID=" + id);
			return new ResponseEntity<>(body, status);
		} else {
			JSONObject body = new JSONObject();
			body.put("code", "NOT_FOUND");
			body.put("message", "Oops! Task was not found.");
			body.put("id", id);
			status = HttpStatus.NOT_FOUND;

			logger.error("A task was not updated with ID=" + id);
			return new ResponseEntity<>(body.toString(), status);
		}
	}

	@DeleteMapping(value = "tasks/{id}", produces = "application/json")
	public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
		logger.info("Received request to delete task with ID=" + id);

		JSONObject body = new JSONObject();
		HttpStatus status;

		try {
			tasksService.deleteTask(id);
			body.put("code", "OK");
			body.put("message", "Task was deleted.");
			body.put("id", id);
			status = HttpStatus.OK;

			logger.info("A task deleted with ID=" + id);
			return new ResponseEntity<>(body.toString(), status);
		} catch (IllegalArgumentException | EmptyResultDataAccessException exception) {
			body.put("code", "NOT_FOUND");
			body.put("message", "Oops! No task found.");
			body.put("id", id);
			status = HttpStatus.NOT_FOUND;

			logger.error("A task was not deleted with ID=" + id + " due to:");
			logger.error(exception.getMessage());
			return new ResponseEntity<>(body.toString(), status);
		} catch (Exception exception) {
			body.put("code", "NOT_FOUND");
			body.put("message", "Oops! Deleting failed.");
			body.put("id", id);
			status = HttpStatus.INTERNAL_SERVER_ERROR;

			logger.error("A task was not deleted with ID=" + id + " due to:");
			logger.error(exception.getMessage());
			return new ResponseEntity<>(body.toString(), status);
		}
	}
}
