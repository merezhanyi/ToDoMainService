package com.crg.todo.tasks_list.service;

import com.crg.todo.tasks_list.entity.Task;
import com.crg.todo.tasks_list.repository.TasksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksService {

    private static final Logger logger = LoggerFactory.getLogger(TasksService.class);

    @Autowired
    private TasksRepository tasksRepository;

    public List<Task> findAllTasks() {
        return tasksRepository.findAll();
    }

    public Task createTask(Task task) {
        return tasksRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        Task updatedTask = new Task();
        Task existingTask;
        if (tasksRepository.findById(id).isPresent()) {
            existingTask = tasksRepository.findById(id).get();
            updatedTask.setId(existingTask.getId());
        } else {
            logger.error("Task entity with ID=" + id + " was not found in database.");
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

        if (task.isDone() != null) {
            updatedTask.setDone(task.isDone());
        } else {
            updatedTask.setDone(existingTask.isDone());
        }

        return tasksRepository.save(updatedTask);
    }

    public void deleteTask(Long id) {
        tasksRepository.deleteById(id);
    }
}
