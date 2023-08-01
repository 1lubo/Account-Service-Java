package account.log;

import java.util.Date;

public class SecurityLogDTO {
	private Date date;
	private String action;
	private String subject;
	private String object;
	private String path;

	public SecurityLogDTO(){}
	public SecurityLogDTO(Date date, String action, String subject, String object, String path){
		this.date = date;
		this.action = action;
		this.subject = subject;
		this.object = object;
		this.path = path;
	}

	public Date getDate() {	return date;	}
	public String getAction() {	return action;	}
	public String getSubject() {	return subject;	}
	public String getObject() {	return object;	}
	public String getPath() {	return path;	}

	public void setDate(Date date) { this.date = date;	}
	public void setAction(String action) { this.action = action;	}
	public void setSubject(String subject) { this.subject = subject;	}
	public void setObject(String object) { this.object = object;	}
	public void setPath(String path) { this.path = path;	}
}
