package nextmainfocus.task;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

import nextmainfocus.error.ErrorMessenger;
import nextmainfocus.util.Utility;

@CrossOrigin(origins = "${client.url}")
@RestController
@RequestMapping("api/v1/")
public class TaskController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	private static final String ID = "id";

	private static final String responseInvalidUuid(String uuid, IllegalArgumentException exception) {
		logger.error("Provided tasks's UUID={} is not valid:", uuid);
		logger.error(exception.getMessage());

		return Utility.fillResponseBody(HttpStatus.BAD_REQUEST, ErrorMessenger.ERROR_INVALID_UUID,
				null);
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

				status = HttpStatus.OK;
				body = Utility.fillResponseBody(status, TaskMessenger.TASK_FOUND, tasks);
			} else {
				logger.error("No tasks were found in the database");

				status = HttpStatus.NOT_FOUND;
				body = Utility.fillResponseBody(status, TaskMessenger.TASK_NOT_FOUND, null);
			}
		} catch (Exception exception) {
			logger.error("Cannot retrieve data from DB due to:");
			logger.error(exception.getMessage());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body = Utility.fillResponseBody(status, ErrorMessenger.ERROR_SERVER, null);
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

				status = HttpStatus.OK;
				body = Utility.fillResponseBody(status, TaskMessenger.TASK_FOUND,
						Arrays.asList(task));
			} else {
				logger.error("A task was not found with ID={}", id);

				status = HttpStatus.NOT_FOUND;
				body = Utility.fillResponseBody(status, TaskMessenger.TASK_NOT_FOUND + ": " + id,
						null);
			}
		} catch (IllegalArgumentException exception) {
			status = HttpStatus.BAD_REQUEST;
			body = responseInvalidUuid(uuid, exception);
		} catch (Exception exception) {
			logger.error("Cannot retrieve data from DB due to:");
			logger.error(exception.getMessage());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body = Utility.fillResponseBody(status, ErrorMessenger.ERROR_SERVER, null);
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

			status = HttpStatus.CREATED;
			body = Utility.fillResponseBody(status,TaskMessenger.TASK_CREATED,
					Arrays.asList(newTask));
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{taskLocation}")
					.buildAndExpand(newTask.getId().toString()).toUri();
			responseHeaders.setLocation(location);
		} catch (Exception exception) {
			logger.info("Task was not created with description: {}", task.getDescription());
			logger.error("due to: {}", exception.getMessage());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body = Utility.fillResponseBody(status, TaskMessenger.TASK_NOT_CREATED, null);
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

				status = HttpStatus.OK;
				body = Utility.fillResponseBody(status, TaskMessenger.TASK_UPDATED,
						Arrays.asList(updatedTask));
			} else {
				logger.error("A task was not updated with ID={}", id);

				status = HttpStatus.NOT_FOUND;
				body = Utility.fillResponseBody(status, TaskMessenger.TASK_NOT_FOUND + ": " + id,
						null);
			}
		} catch (IllegalArgumentException exception) {
			status = HttpStatus.BAD_REQUEST;
			body = responseInvalidUuid(uuid, exception);
		} catch (Exception exception) {
			logger.error("Cannot update data in DB due to:");
			logger.error(exception.getMessage());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body = Utility.fillResponseBody(status, ErrorMessenger.ERROR_SERVER, null);
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

			status = HttpStatus.OK;
			body = Utility.fillResponseBody(status, MessageFormat.format("{0}: {1}",
					TaskMessenger.TASK_DELETED, id), null);
		} catch (IllegalArgumentException exception) {
			status = HttpStatus.BAD_REQUEST;
			body = responseInvalidUuid(uuid, exception);
		} catch (EmptyResultDataAccessException exception) {
			logger.error("A task was not deleted with ID={} due to:", uuid);
			logger.error(exception.getMessage());

			status = HttpStatus.NOT_FOUND;
			body = Utility.fillResponseBody(status, MessageFormat.format("{}: {}",
					TaskMessenger.TASK_NOT_FOUND, uuid), null);
		} catch (Exception exception) {
			logger.error("A task was not deleted with ID={} due to:", uuid);
			logger.error(exception.getMessage());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body = Utility.fillResponseBody(status, MessageFormat.format("{}: {}",
					TaskMessenger.TASK_NOT_DELETED, uuid), null);
		}
		return new ResponseEntity<>(body, status);
	}
}
