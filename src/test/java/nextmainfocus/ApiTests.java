package nextmainfocus;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextmainfocus.task.Task;
import nextmainfocus.task.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class ApiTests {
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private TaskService taskService;
	private Task testTask;

	@BeforeEach
	public void setUp() {
		Task task = new Task(null, "test task", false, LocalDateTime.now());
		testTask = taskService.createTask(task);
	}

	@Test
	void healthcheckTest() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

	@Test
		//	@WithMockUser(username = "guest", password = "password", roles = "user") //for integration with real db.
	void getAllTasksTest() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult response = mockMvc.perform(get("/api/v1/tasks/"))
				.andExpect(status().isOk())
				.andReturn();

		assertThat(response.getResponse().getContentAsString()).contains("test task");
	}

	@Test
	void getTaskByIdTest() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult response = mockMvc.perform(get("/api/v1/tasks/" + testTask.getId()))
				.andExpect(status().isOk())
				.andReturn();

		assertThat(response.getResponse().getContentAsString()).contains("test task");
	}

	@Test
	void createNewTaskTest() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		testTask.setDescription("test1 task");
		MvcResult response = mockMvc.perform(
						post("/api/v1/tasks/")
								.content(asJsonString(testTask))
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();

		assertThat(response.getResponse().getContentAsString()).contains("test1 task");
	}

	@Test
	void updateTaskTest() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		testTask.setDescription("test2 task");
		MvcResult response = mockMvc.perform(
						put("/api/v1/tasks/" + testTask.getId())
								.content(asJsonString(testTask))
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		assertThat(response.getResponse().getContentAsString()).contains("test2 task");
	}

	@Test
	void deleteTaskTest() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult response = mockMvc.perform(
						delete("/api/v1/tasks/" + testTask.getId())
								.content(asJsonString(testTask))
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		assertThat(response.getResponse().getContentAsString()).contains("Task was deleted");
	}


	private String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
