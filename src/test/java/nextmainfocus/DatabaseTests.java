package nextmainfocus;

import nextmainfocus.account.Account;
import nextmainfocus.account.AccountsRepository;
import nextmainfocus.task.Task;
import nextmainfocus.task.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")

public class DatabaseTests {
	@Autowired
	AccountsRepository accountsRepository;
	@Autowired
	TaskRepository taskRepository;

	@Test
	public void saveUserTest() {
		Account account = new Account(null, "test", "password", "user");
		Account savedAccount = accountsRepository.save(account);
		assertThat(savedAccount).usingRecursiveComparison().ignoringFields("userId").isEqualTo(account);
	}

	@Test
	public void saveTaskTest() {
		Task task = new Task(null, "test", false, LocalDateTime.now());
		Task savedTask = taskRepository.save(task);
		assertThat(savedTask).usingRecursiveComparison().ignoringFields("userId").isEqualTo(task);
	}
}
