package account.log;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity

public class SecurityLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String action;
	private Date eventDate;
	private String subject;
	private String object;
	private String path;

	public SecurityLog(){}
	public SecurityLog(Date date, String action, String subject, String object, String path){
		this.action = action;
		this.eventDate = date;
		this.subject = subject;
		this.object = object;
		this.path = path;
	}

	public Date getEventDate() { return eventDate; }
	public String getSubject() {	return subject;	}
	public String getObject(){	return object;	}
	public String getPath(){ return path; }
	public String getAction() {	return action;	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public SecurityLogDTO convertToDTO() {
		return new SecurityLogDTO(eventDate, action, subject, object, path);
	}
}
