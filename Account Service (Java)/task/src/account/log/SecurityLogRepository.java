package account.log;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface SecurityLogRepository extends JpaRepository<SecurityLog, Long> {
	@Override
	List<SecurityLog> findAll();
	List<SecurityLog> findBySubject(String email);
	List<SecurityLog> findByEventDate(Date date);

}
