package br.com.yarazip.forumhub.modules.exception.usecase;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
