package account.payroll;

import account.account.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import static account.payroll.date.SalaryPeriodToISODate.salaryPeriodToISODate;
import static account.payroll.date.SalaryPeriodToISODate.salaryPeriodWithMonth;

@Entity

public class Payroll {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Email
	@NotBlank
	private String employee;
	@NotBlank
	@NotNull
	private String period;

	@NotNull
	private long salary;

	@ManyToOne
	@JoinColumn(name="account_id", nullable=false)
	private Account account;

	public Payroll(){}

	public Payroll(long id, String employee, String period, long salary){
		this.id = id;
		this.employee = employee;
		this.period = period;
		this.salary = salary;
	}

	public long getId() {
		return id;
	}
	public String getPeriod() {
		return period;
	}

	public long getSalary() {
		return salary;
	}

	public String getEmployee() {
		return employee;
	}

	public Account getAccount() {
		return account;
	}
	public String getPeriodAsISO() {return salaryPeriodToISODate(period); }

	public void setId(long id) {
		this.id = id;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public void setSalary(long salary) {
		this.salary = salary;
	}

	public String salaryInDollars(){
		return this.salary / 100 + " dollar(s) " + this.salary % 100 + " cent(s)";
	}

	public String getPeriodWithMonth(){
		return salaryPeriodWithMonth(period);
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public PayrollDTO convertToDTO(){
		return new PayrollDTO(this.account, getPeriodWithMonth(), this.salaryInDollars());
	}
}
