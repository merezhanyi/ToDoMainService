package nextmainfocus.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
	@Query("SELECT s FROM Account s WHERE s.username =:username")
	Account findByUsername(@Param("username") String username);
}
