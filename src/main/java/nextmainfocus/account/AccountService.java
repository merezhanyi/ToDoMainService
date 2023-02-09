package nextmainfocus.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	@Autowired
	private AccountsRepository accountsRepository;

	public Account createUser(Account user) {
		List<Account> usersFromDatabase = accountsRepository.findAll();

		for (Account accountFromDatabase : usersFromDatabase) {
			if (accountFromDatabase.getUsername().equals(user.getUsername())) {
				return null;
			}
		}

		return accountsRepository.save(user);
	}

	public List<Account> findAllUsers() {
		return accountsRepository.findAll();
	}

	public Account findByUsername(String username) {
		return accountsRepository.findByUsername(username).orElse(null);
	}
}
