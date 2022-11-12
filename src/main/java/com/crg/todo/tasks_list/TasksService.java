package com.crg.todo.tasks_list;

import com.crg.todo.tasks_list.entity.Task;
import com.crg.todo.tasks_list.repository.TasksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TasksService {

    private static final Logger
            logger =
            LoggerFactory.getLogger(TasksService.class);

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
        } else if (page != null && sort == null) {
            Pageable pageable = PageRequest.of(Integer.valueOf(page), 10);
            return tasksRepository.findAll(pageable).getContent();
        }

        if (sort != null) {
            return tasksRepository.findAll(sort);
        } else {
            return tasksRepository.findAll();
        }
    }

    public Optional<Task> findById(Long id) {
        if (tasksRepository.existsById(id)) {
            return tasksRepository.findById(id);
        } else {
            return null;
        }
    }

    public Task updateTask(Long id, Task task) {
        Task updatedTask = new Task();
        Task existingTask;
        if (tasksRepository.findById(id).isPresent()) {
            existingTask = tasksRepository.findById(id).get();
            updatedTask.setId(existingTask.getId());
        } else {
            logger.error("Task entity with ID=" +
                    id +
                    " was not found in database.");
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