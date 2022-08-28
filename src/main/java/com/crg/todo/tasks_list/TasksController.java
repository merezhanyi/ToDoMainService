package com.crg.todo.tasks_list;

import com.crg.todo.tasks_list.domain.Task;
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

    @GetMapping("/list")
    public ResponseEntity<?> findAllTasks() {
        List<Task> tasks = tasksService.findAllTasks();

        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        logger.info("Received request to create a new task: " + task.toString());
        Task newTask = tasksService.createTask(task);

        return ResponseEntity.ok("created new task with ID=" + newTask.getId());
    }
}
