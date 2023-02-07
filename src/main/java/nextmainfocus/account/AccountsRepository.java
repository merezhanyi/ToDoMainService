package nextmainfocus.account;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountsRepository extends JpaRepository<Account, UUID> {
	@Query("SELECT s FROM Account s WHERE s.username =:username")
	Account findByUsername(@Param("username") String username);
}
