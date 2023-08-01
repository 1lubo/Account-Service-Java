package account.security.errorMessage;

import java.time.LocalDateTime;

public class ErrorMessage {
	private final LocalDateTime timestamp;
	private final int status;
	private final String error;
	private final String message;
	private final String path;

	public ErrorMessage(LocalDateTime timeStamp, int status, String error, String message, String path){
		this.timestamp = timeStamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}
	public int getStatus() {
		return status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getError() {
		return error;
	}

	public String getPath() {
		return path;
	}
}
