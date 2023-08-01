package account.payroll.date;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class SalaryPeriodToISODate {

	public static String salaryPeriodToISODate(String salaryPeriod){
		String [] splitPeriod = salaryPeriod.split("-");
		return splitPeriod[1] + splitPeriod[0] + "01";
	}

	public static String salaryPeriodWithMonth(String salaryPeriod){
		String [] splitPeriod = salaryPeriod.split("-");
		String month = Month.of(Integer.parseInt(splitPeriod[0])).getDisplayName(TextStyle.FULL_STANDALONE, Locale.US);
		return month + "-" + splitPeriod[1];
	}
}
