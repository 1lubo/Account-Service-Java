package account.payroll;

import java.util.List;

public interface PayrollService {
	void savePayroll(Payroll payroll);
	List<PayrollDTO> findByAccount(String email);
}
