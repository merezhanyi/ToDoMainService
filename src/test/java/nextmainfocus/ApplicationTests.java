package nextmainfocus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ApplicationTests {
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Test
	void healthcheckTest() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "guest", password = "password", roles = "user")
	void getAllTasksTest() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult result = mockMvc.perform(get("/api/v1/tasks/"))
				.andExpect(status().isOk())
				.andReturn();

		assertThat(result.getResponse().getContentAsString()).contains("{\"dateTime\":\"2023-01-28T08:40:54.597621\",\"description\":\"Drink morning");
	}
}
