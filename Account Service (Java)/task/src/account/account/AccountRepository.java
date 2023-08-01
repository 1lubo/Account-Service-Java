package account.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Override
	List<Account> findAll();

	Account findById(long id);
	Account findByEmail(String email);

	Account save(Account user);
	boolean existsByEmail(String email);

}
