package br.com.yarazip.forumhub.modules.exceptionn;

import br.com.yarazip.forumhub.modules.exception.usecase.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerController {

	private final MessageSource messageSource;

	public ExceptionHandlerController(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<?> handleForbiddenException(ForbiddenException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadRequestException(BadCredentialsException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<?> handleIllegalStateException(IllegalStateException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

	@ExceptionHandler(RateLimitExceededException.class)
	public ResponseEntity<?> handleRateLimitExceededException(RateLimitExceededException e) {
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<?> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ErrorMessageDTO>> handlerMethodArgumentNotValidException(
			MethodArgumentNotValidException e) {

		List<ErrorMessageDTO> dto = new ArrayList<>();

		e.getBindingResult().getFieldErrors().forEach(err -> {
			String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());
			ErrorMessageDTO error = new ErrorMessageDTO(message, err.getField());
			dto.add(error);
		});

		return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
}
