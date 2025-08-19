package br.com.yarazip.forumhub.modules.exception.usecase;

public class RateLimitExceededException extends RuntimeException {

	public RateLimitExceededException(String message) {
		super(message);
	}

	public RateLimitExceededException() {
		super("Rate limit exceeded. Try again later.");
	}
}
