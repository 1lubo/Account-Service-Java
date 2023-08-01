package account.payroll.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidatorLocalDate implements DateValidator{
	private final DateTimeFormatter dateTimeFormatter;

	public DateValidatorLocalDate(DateTimeFormatter dateTimeFormatter){
		this.dateTimeFormatter = dateTimeFormatter;
	}

	@Override
	public boolean isNotValid(String dateString){
		try {
			LocalDate.parse(dateString, this.dateTimeFormatter);
		} catch (DateTimeParseException ex) {
			return true;
		}
		return false;
	}
}
