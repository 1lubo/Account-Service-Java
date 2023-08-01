package account.payroll;

import account.account.Account;

public class PayrollDTO {
	private String name;
	private String lastname;
	private String period;
	private String salary;

	public PayrollDTO(){}
	public PayrollDTO(Account account, String period,String salaryInDollars){
		this.name = account.getName();
		this.lastname = account.getLastname();
		this.period = period;
		this.salary = salaryInDollars;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public String getName() {
		return name;
	}

	public String getPeriod() {
		return period;
	}

	public String getSalary() {
		return salary;
	}
}
