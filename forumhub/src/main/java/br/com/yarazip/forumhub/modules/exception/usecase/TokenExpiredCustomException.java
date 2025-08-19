package br.com.yarazip.forumhub.modules.exception.usecase;

public class TokenExpiredCustomException extends RuntimeException {
    public TokenExpiredCustomException(String message) {
        super(message);
    }
}