package com.crg.todo.tasks_list;

import com.crg.todo.tasks_list.entity.Task;
import com.crg.todo.tasks_list.service.TasksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Spring boot workflow:
// front            back
// httpRequest > controller > service (magic here) > repo (interface) > entities (db)
//             < controller < service (magic here) < repo (interface) < entities (db)

@RestController
@RequestMapping("/v1/tasks")
public class TasksController {

    private static final Logger logger = LoggerFactory.getLogger(TasksController.class);

    @Autowired
    TasksService tasksService;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        logger.info("Received request to create a new task: " + task.toString());
        Task newTask = tasksService.createTask(task);

        return ResponseEntity.ok("created new task with ID=" + newTask.getId());
    }

    @GetMapping("/read")
    public ResponseEntity<?> findAllTasks() {
        List<Task> tasks = tasksService.findAllTasks();

        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTask(@RequestParam Long id, @RequestBody Task task) {
        logger.info("Received request to update task with ID=" + id);
        Task updatedTask = tasksService.updateTask(id, task);

        if (updatedTask != null) {
            return ResponseEntity.ok("updated task with ID=" + updatedTask.getId());
        } else {
            return ResponseEntity.ok("Task entity with ID=" + id + " was not found in database.");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> updateTask(@RequestParam Long id) {
        logger.info("Received request to delete task with ID=" + id);
        tasksService.deleteTask(id);

        return ResponseEntity.ok("deleted task with ID=" + id);
    }
}
