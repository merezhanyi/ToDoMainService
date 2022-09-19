package com.crg.todo.tasks_list;

import com.crg.todo.tasks_list.entity.Task;
import com.crg.todo.tasks_list.service.TasksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Spring boot workflow:
// front            back
// httpRequest > controller > service (magic here) > repo (interface) > entities (db)
//             < controller < service (magic here) < repo (interface) < entities (db)

@RestController @RequestMapping("api/v1/") public class TasksController {

    private static final Logger
            logger =
            LoggerFactory.getLogger(TasksController.class);

    @Autowired TasksService tasksService;

    @PostMapping("tasks/")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        logger.info("Received request to create a new task: " +
                task.toString());
        try {
            Task newTask = tasksService.createTask(task);

            return new ResponseEntity<>("Created new task with ID=" +
                    newTask.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Task creation failed",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("tasks/")
    public ResponseEntity<?> findTasks(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String field) {
        logger.info("Received request to retrieve tasks");
        List<Task> tasks = tasksService.findAllTasks(page, sort, field);

        return tasks.size() > 0 ?
                new ResponseEntity<>(tasks, HttpStatus.OK) :
                new ResponseEntity<>("No tasks found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("tasks/{id}")
    public ResponseEntity<?> findTask(@PathVariable("id") Long id) {
        logger.info("Received request to retrieve task with ID=" + id);
        Optional<Task> task = tasksService.findById(id);

        return task != null ?
                new ResponseEntity<>(task, HttpStatus.OK) :
                new ResponseEntity<>("No task found", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("tasks/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") Long id,
                                        @RequestBody Task task) {
        logger.info("Received request to update task with ID=" + id);
        Task updatedTask = tasksService.updateTask(id, task);

        return updatedTask != null ?
                new ResponseEntity<>(updatedTask, HttpStatus.OK) :
                new ResponseEntity<>("No task found", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        logger.info("Received request to delete task with ID=" + id);
        try {
            tasksService.deleteTask(id);
            return new ResponseEntity<>("Deleted task with ID=" + id,
                    HttpStatus.OK);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ResponseEntity<>("Task deleting failed",
                    HttpStatus.BAD_REQUEST);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("No task found",
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Task deleting failed",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
