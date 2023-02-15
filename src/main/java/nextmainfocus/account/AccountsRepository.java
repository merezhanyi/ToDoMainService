package nextmainfocus.account;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account, UUID> {
	Account findByUsername(String username);
}
