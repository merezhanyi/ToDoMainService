package nextmainfocus.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nextmainfocus.security.entity.Account;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
	@Query("SELECT s FROM Account s WHERE s.username =:username")
	Account findByUsername(@Param("username") String username);
}
