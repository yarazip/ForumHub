package br.com.yarazip.forumhub.modules.exception.usecase;

public class ForbiddenException extends RuntimeException {
	public ForbiddenException(String message) {
		super(message);
	}
}
