package com.crg.todo.tasks_list.service;

import com.crg.todo.tasks_list.domain.Task;
import com.crg.todo.tasks_list.repository.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksService {

    @Autowired
    private TasksRepository tasksRepository;

    public List<Task> findAllTasks() {
        return tasksRepository.findAll();
    }

    public Task createTask(Task task) {
        return tasksRepository.save(task);
    }
}
