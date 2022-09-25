package com.crg.todo;

import com.crg.todo.tasks_list.TasksController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest class ApplicationTests {

    @Autowired TasksController tasksController;

    @Test void getTasksList() {
//        ArrayList<Task>
//                body = (ArrayList<Task>) tasksController
//                        .findTasks("", "", "")
//                        .getBody();
//        Integer status = tasksController
//                            .findTasks("", "", "")
//                            .getStatusCodeValue();
//
//        assertThat(tasksController).isNotNull();
//        assertThat(status).isEqualTo(200);
    }
}
