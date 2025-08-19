package br.com.yarazip.forumhub.modules.exceptionn;

public class ErrorMessageDTO {

	private String message;
	private final String field;

	public ErrorMessageDTO(String message, String field) {
		this.message = message;
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getField() {
		return field;
	}

	@Override
	public String toString() {
		return "ErrorMessageDTO{" + "message='" + message + '\'' + ", field='" + field + '\'' + '}';
	}
}
