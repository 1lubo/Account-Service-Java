package account.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long>{
	List<Payroll> findPayrollByAccount_EmailOrderByPeriodDesc(String email);
}
