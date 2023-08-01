package account.controller;

import account.account.Account;
import account.account.UserDetailsImpl;
import account.account.UserDetailsServiceImpl;
import account.payroll.Payroll;
import account.payroll.PayrollDTO;
import account.payroll.PayrollServiceImplementation;
import account.payroll.date.DateValidatorLocalDate;
import account.security.exceptions.AccountValidationException;
import account.security.exceptions.DateValidationException;
import account.security.exceptions.PayrollUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static account.payroll.date.SalaryPeriodToISODate.salaryPeriodToISODate;

@RestController
public class PayrollController {

	@Autowired
	UserDetailsServiceImpl accountDetailsService;
	@Autowired
	PayrollServiceImplementation payrollService;

	DateValidatorLocalDate validatorLocalDate = new DateValidatorLocalDate(DateTimeFormatter.BASIC_ISO_DATE);


	@GetMapping("api/empl/payment")
	public ResponseEntity<Object> getEmployeePayroll(@RequestParam(required = false) String period, Authentication auth) {
		UserDetailsImpl details = (UserDetailsImpl) auth.getPrincipal();
		Account currentUser = accountDetailsService.findByUsername(details.getEmail());
		List<PayrollDTO> userPayroll = payrollService.findByAccount(currentUser.getEmail());
		List<Payroll> userPayrollNoDTO = payrollService.findByAccountPayrolls(currentUser.getEmail());

		if(period == null) {
			return new ResponseEntity<>(userPayroll, HttpStatus.OK);
		}
		if (validatorLocalDate.isNotValid(salaryPeriodToISODate(period))){
				throw new DateValidationException("Period is not a valid date");
		}
		Predicate<Payroll> streamPredicate = item -> item.getPeriod().matches(period);
		List<Payroll> filteredPayroll = userPayrollNoDTO.stream().filter(streamPredicate).collect(Collectors.toList());

		if(filteredPayroll.size() > 0){
			return new ResponseEntity<>(filteredPayroll.get(0).convertToDTO(), HttpStatus.OK);
		}

		return new ResponseEntity<>(filteredPayroll, HttpStatus.OK);
	}

	@PostMapping("api/acct/payments")
	public ResponseEntity<Object> uploadEmployeePayroll(@RequestBody List<Payroll> employeePayrolls){
		payrollService.doTransaction(employeePayrolls);
		final Map<String, Object> body = new HashMap<>();
		body.put("status", "Added successfully!");
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	@PutMapping("api/acct/payments")
	public ResponseEntity<Object> updateEmployeePayroll(@RequestBody Payroll employeePayroll) {
		if (accountDetailsService.doesNotExistByEmail(employeePayroll.getEmployee().toLowerCase())) {
			throw new AccountValidationException(String.format("Employee '%s' does not exist", employeePayroll.getEmployee()));
		}
		if (validatorLocalDate.isNotValid(employeePayroll.getPeriodAsISO())) {
			throw new DateValidationException(String.format("Period '%s' is not a valid date", employeePayroll.getPeriod()));
		}
		if (employeePayroll.getSalary() < 0) {
			throw new PayrollUploadException(String.format("Salary '%s' must not be less than 0", employeePayroll.getSalary()));
		}

		List<Payroll> userPayrollNoDTO = payrollService.findByAccountPayrolls(employeePayroll.getEmployee().toLowerCase());
		Predicate<Payroll> streamPredicate = item -> item.getPeriod().matches(employeePayroll.getPeriod());
		List<Payroll> filteredPayroll = userPayrollNoDTO.stream().filter(streamPredicate).toList();
		if(filteredPayroll.size() < 1){
			throw new PayrollUploadException("Employee does not have salary for the specified period: " + employeePayroll.getPeriod());
		}
		Payroll newPayroll = filteredPayroll.get(0);
		newPayroll.setSalary(employeePayroll.getSalary());
		payrollService.savePayroll(newPayroll);
		final Map<String, Object> body = new HashMap<>();
		body.put("status", "Updated successfully!");
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
}
