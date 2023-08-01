package account.payroll;

import account.account.UserDetailsServiceImpl;
import account.payroll.date.DateValidatorLocalDate;
import account.security.exceptions.AccountValidationException;
import account.security.exceptions.DateValidationException;
import account.security.exceptions.PayrollUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
 public class PayrollServiceImplementation implements PayrollService{

	private final PlatformTransactionManager trManager;
	@Autowired
	UserDetailsServiceImpl accountDetailsService;
	@Autowired
	PayrollRepository payrollRepository;
	@Autowired PayrollServiceImplementation(PlatformTransactionManager trManager){
		this.trManager = trManager;
	}

	DateValidatorLocalDate validatorLocalDate = new DateValidatorLocalDate(DateTimeFormatter.BASIC_ISO_DATE);

	@Override
	public void savePayroll(Payroll payroll) {
		payrollRepository.save(payroll);
	}

	@Override
	public List<PayrollDTO> findByAccount(String email) {
		List<Payroll> allPayrolls = payrollRepository.findPayrollByAccount_EmailOrderByPeriodDesc(email);
		List<PayrollDTO> outputPayrolls = new ArrayList<>();
		for(Payroll payroll :  allPayrolls) {
			outputPayrolls.add(payroll.convertToDTO());
		}
		return outputPayrolls;
	}

	public List<Payroll> findByAccountPayrolls(String email) {
		return payrollRepository.findPayrollByAccount_EmailOrderByPeriodDesc(email);
	}

	public void doTransaction(List<Payroll> employeePayrolls) {
		TransactionDefinition trDefinition = new DefaultTransactionDefinition();
		TransactionStatus trStatus = trManager.getTransaction(trDefinition);
		HashMap<String, ArrayList<String>> accountPeriods = new HashMap<>();
		try{
			for(Payroll empPayroll : employeePayrolls){
				if(accountDetailsService.doesNotExistByEmail(empPayroll.getEmployee().toLowerCase())){
					throw new AccountValidationException(String.format("Employee '%s' does not exist", empPayroll.getEmployee()));
				}
				if(validatorLocalDate.isNotValid(empPayroll.getPeriodAsISO())){
					throw new DateValidationException(String.format("Period '%s' is not a valid date", empPayroll.getPeriod()));
				}
				if(empPayroll.getSalary() < 0) {
					throw new PayrollUploadException(String.format("Salary '%s' must not be less than 0", empPayroll.getSalary()));
				}
				if(accountPeriods.containsKey(empPayroll.getEmployee())){
					ArrayList<String > previousPeriods = accountPeriods.get(empPayroll.getEmployee());
					if(previousPeriods.contains(empPayroll.getPeriod())){
						throw new PayrollUploadException("Salary for period already entered");
					} else {
						previousPeriods.add(empPayroll.getPeriod());
						accountPeriods.put(empPayroll.getEmployee(), previousPeriods);
					}
				} else {
					accountPeriods.put(empPayroll.getEmployee(), new ArrayList<>(Collections.singletonList(empPayroll.getPeriod())));
				}

				if(empPayroll.getAccount() == null){
					empPayroll.setAccount(accountDetailsService.findByUsername(empPayroll.getEmployee().toLowerCase()));
				}
				payrollRepository.save(empPayroll);
			}
			trManager.commit(trStatus);
		} catch (Exception ex){
			trManager.rollback(trStatus);
			throw ex;
		}
	}
}
