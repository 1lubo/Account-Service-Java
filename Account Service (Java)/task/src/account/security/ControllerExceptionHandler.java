package account.security;

import account.security.errorMessage.ErrorMessage;
import account.security.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(AccountLockException.class)
	public ResponseEntity<ErrorMessage> AccountLockException(AccountLockException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			400,
			(HttpStatus.valueOf( 400)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccountRoleUpdateException.class)
	public ResponseEntity<ErrorMessage> AccountDoesNotHaveRoleException(AccountRoleUpdateException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			400,
			(HttpStatus.valueOf( 400)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RoleNotFoundException.class)
	public ResponseEntity<ErrorMessage> AccountRoleUpdateException(RoleNotFoundException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			404,
			(HttpStatus.valueOf( 404)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AccountDeletionException.class)
	public ResponseEntity<ErrorMessage> AccountDeletionException(AccountDeletionException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			400,
			(HttpStatus.valueOf( 400)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NewPasswordValidationException.class)
	public ResponseEntity<ErrorMessage> PasswordBreachedException(NewPasswordValidationException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			400,
			(HttpStatus.valueOf( 400)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccountValidationException.class)
	public ResponseEntity<ErrorMessage> AccountValidationException(AccountValidationException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			400,
			(HttpStatus.valueOf( 400)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorMessage> userAlreadyExistsException(UserAlreadyExistsException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			400,
			(HttpStatus.valueOf( 400)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PayrollUploadException.class)
	public ResponseEntity<ErrorMessage> payrollUploadException(PayrollUploadException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			400,
			(HttpStatus.valueOf( 400)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DateValidationException.class)
	public ResponseEntity<ErrorMessage> DateValidationException(DateValidationException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			400,
			(HttpStatus.valueOf( 400)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorMessage> badCredentialsException(WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			401,
			(HttpStatus.valueOf( 401)).getReasonPhrase(),
			"",
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ErrorMessage> validationException(AccountNotFoundException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			404,
			(HttpStatus.valueOf( 404)).getReasonPhrase(),
			exception.getMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> validationException(MethodArgumentNotValidException exception, WebRequest request) {
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			400,
			(HttpStatus.valueOf( 400)).getReasonPhrase(),
			Objects.requireNonNull(exception.getFieldError()).getDefaultMessage(),
			path);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> globalExceptionHandler (ResponseStatusException exception, WebRequest request) {
		int exceptionStatusCode = exception.getStatusCode().value();
		String path = request.getDescription(false).substring(request.getDescription(false).indexOf('=') + 1);
		ErrorMessage errorMessage = new ErrorMessage(
			LocalDateTime.now(),
			exceptionStatusCode,
			((HttpStatus.valueOf(exceptionStatusCode))).getReasonPhrase(),
			"",
			path);
		return new ResponseEntity<>(errorMessage, HttpStatusCode.valueOf(exceptionStatusCode));
	}
}
