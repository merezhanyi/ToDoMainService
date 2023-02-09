package nextmainfocus.account;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account, UUID> {
	// @Query("SELECT s FROM Account s WHERE s.username =:username")
	// Account findByUsername(@Param("username") String username);
	Optional<Account> findByUsername(String username);
}
