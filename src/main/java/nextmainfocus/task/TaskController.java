package nextmainfocus.task;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.annotation.Nullable;
import nextmainfocus.task.entity.Task;

// Spring boot workflow:
// front            back
// httpRequest > controller > service (magic here) > repo (interface) > entities (db)
//             < controller < service (magic here) < repo (interface) < entities (db)

@CrossOrigin(origins = "${client.url}")
@RestController
@RequestMapping("api/v1/")
public class TaskController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	private static final String ID = "id";
	private static final String CODE_OK = "OK";
	private static final String CODE_NOT_FOUND = "NOT_FOUND";
	private static final String CODE_BAD_REQUEST = "BAD_REQUEST";
	private static final String CODE_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
	private static final String MESSAGE_FOUND = "Task was found";
	private static final String MESSAGE_SERVER_ERROR = "🤖 Something went wrong!";
	private static final String MESSAGE_INVALID_UUID = "😭 Invalid UUID!";
	private static final String MESSAGE_NOT_FOUND = "😱 No tasks found!";
	private static final String MESSAGE_CREATED = "Task was created";
	private static final String MESSAGE_CREATION_FAILED = "😱 Task creation failed!";
	private static final String MESSAGE_DELETED = "Task was deleted";
	private static final String MESSAGE_DELETION_FAILED = "😱 Deleting failed!";
	private static final String MESSAGE_UPDATED = "Task was updated";
	private static final String LOG_MESSAGE_GENERAL = "Cannot retrieve data from DB due to:";
	private static final String LOG_MESSAGE_NOT_VALID_UUID = "Provided tasks's UUID={} is not valid:";

	private static String fillBody(String code, String message, @Nullable List<Task> results) {
		JSONObject body = new JSONObject();
		body.put("code", code);
		body.put("message", message);
		body.put("results", results);
		return body.toString();
	}

	@Autowired
	TaskService taskService;

	@GetMapping(value = "tasks/", produces = "application/json")
	public ResponseEntity<String> findTasks(@RequestParam(required = false) String page,
			@RequestParam(required = false) String sort, @RequestParam(required = false) String field) {
		logger.info("Received request to retrieve tasks");

		String body;
		HttpStatus status;

		try {
			List<Task> tasks = taskService.findAllTasks(page, sort, field);

			if (!tasks.isEmpty()) {
				logger.info("{} tasks were found in the database", tasks.size());

				body = fillBody(CODE_OK, MESSAGE_FOUND, tasks);
				status = HttpStatus.OK;
			} else {
				logger.error("No tasks were found in the database");

				body = fillBody(CODE_NOT_FOUND, MESSAGE_NOT_FOUND, null);
				status = HttpStatus.NOT_FOUND;
			}
		} catch (Exception exception) {
			logger.error(LOG_MESSAGE_GENERAL);
			logger.error(exception.getMessage());

			body = fillBody(CODE_SERVER_ERROR, MESSAGE_SERVER_ERROR, null);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(body, status);
	}

	@GetMapping(value = "tasks/{id}", produces = "application/json")
	public ResponseEntity<String> findTask(@PathVariable(ID) String uuid) {
		logger.info("Received request to retrieve task with ID: {}", uuid);

		String body;
		HttpStatus status;

		try {
			UUID id = UUID.fromString(uuid);
			Task task = taskService.findById(id);

			if (task != null) {
				logger.info("A task found with ID: {}", id);

				body = fillBody(CODE_OK, MESSAGE_FOUND, Arrays.asList(task));
				status = HttpStatus.OK;
			} else {
				logger.error("A task was not found with ID={}", id);

				body = fillBody(CODE_NOT_FOUND, MESSAGE_NOT_FOUND + ": " + id, null);
				status = HttpStatus.NOT_FOUND;
			}
		} catch (IllegalArgumentException exception) {
			logger.error(LOG_MESSAGE_NOT_VALID_UUID, uuid);
			logger.error(exception.getMessage());

			body = fillBody(CODE_BAD_REQUEST, MESSAGE_INVALID_UUID, null);
			status = HttpStatus.BAD_REQUEST;
		} catch (Exception exception) {
			logger.error(LOG_MESSAGE_GENERAL);
			logger.error(exception.getMessage());

			body = fillBody(CODE_SERVER_ERROR, MESSAGE_SERVER_ERROR, null);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(body, status);
	}

	@PostMapping(value = "tasks/", produces = "application/json")
	public ResponseEntity<String> createTask(@RequestBody Task task) {
		logger.info("Received request to create a new task:");
		logger.info("description: {}", task.getDescription());
		logger.info("date/time: {}", task.getDateTime());
		logger.info("done: {}", task.isDone());

		HttpStatus status;
		String body;
		HttpHeaders responseHeaders = new HttpHeaders();

		try {
			Task newTask = taskService.createTask(task);
			logger.info("Task was created with description: {}", task.getDescription());

			body = fillBody(CODE_OK, MESSAGE_CREATED, Arrays.asList(newTask));
			status = HttpStatus.CREATED;
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{taskLocation}")
					.buildAndExpand(newTask.getId().toString()).toUri();
			responseHeaders.setLocation(location);
		} catch (Exception exception) {
			logger.info("Task was not created with description: {}", task.getDescription());
			logger.error("due to: {}", exception.getMessage());

			body = fillBody(CODE_SERVER_ERROR, MESSAGE_CREATION_FAILED, null);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(body, responseHeaders, status);
	}

	@PutMapping(value = "tasks/{id}", produces = "application/json")
	public ResponseEntity<String> updateTask(@PathVariable(ID) String uuid, @RequestBody Task task) {
		logger.info("Received request to update task with ID={}", uuid);

		String body;
		HttpStatus status;

		try {
			UUID id = UUID.fromString(uuid);
			Task updatedTask = taskService.updateTask(id, task);

			if (updatedTask != null) {
				logger.info("A task updated with ID={}", id);

				body = fillBody(CODE_OK, MESSAGE_UPDATED, Arrays.asList(updatedTask));
				status = HttpStatus.OK;
			} else {
				logger.error("A task was not updated with ID={}", id);

				body = fillBody(CODE_NOT_FOUND, MESSAGE_NOT_FOUND + ": " + id, null);
				status = HttpStatus.NOT_FOUND;
			}
		} catch (IllegalArgumentException exception) {
			logger.error(LOG_MESSAGE_NOT_VALID_UUID, uuid);
			logger.error(exception.getMessage());

			body = fillBody(CODE_BAD_REQUEST, MESSAGE_INVALID_UUID, null);
			status = HttpStatus.BAD_REQUEST;
		} catch (Exception exception) {
			logger.error("Cannot update data in DB due to:");
			logger.error(exception.getMessage());

			body = fillBody(CODE_SERVER_ERROR, MESSAGE_SERVER_ERROR, null);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(body, status);
	}

	@DeleteMapping(value = "tasks/{id}", produces = "application/json")
	public ResponseEntity<String> deleteTask(@PathVariable(ID) String uuid) {
		logger.info("Received request to delete task with ID={}", uuid);

		String body;
		HttpStatus status;

		try {
			UUID id = UUID.fromString(uuid);
			taskService.deleteTask(id);
			logger.info("A task deleted with ID={}", id);

			body = fillBody(CODE_OK, MessageFormat.format("{0}: {1}", MESSAGE_DELETED, id), null);
			status = HttpStatus.NO_CONTENT;
		} catch (IllegalArgumentException exception) {
			logger.error(LOG_MESSAGE_NOT_VALID_UUID, uuid);
			logger.error(exception.getMessage());

			body = fillBody(CODE_BAD_REQUEST, MESSAGE_INVALID_UUID, null);
			status = HttpStatus.BAD_REQUEST;
		} catch (EmptyResultDataAccessException exception) {
			logger.error("A task was not deleted with ID={} due to:", uuid);
			logger.error(exception.getMessage());

			body = fillBody(CODE_NOT_FOUND, MessageFormat.format("{}: {}", MESSAGE_NOT_FOUND, uuid), null);
			status = HttpStatus.NOT_FOUND;
		} catch (Exception exception) {
			logger.error("A task was not deleted with ID={} due to:", uuid);
			logger.error(exception.getMessage());

			body = fillBody(CODE_SERVER_ERROR, MessageFormat.format("{}: {}", MESSAGE_DELETION_FAILED, uuid), null);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(body, status);
	}
}
