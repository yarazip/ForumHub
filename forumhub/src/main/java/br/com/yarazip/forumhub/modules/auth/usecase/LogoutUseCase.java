package br.com.yarazip.forumhub.modules.auth.usecase;

import jakarta.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogoutUseCase {

	private static final Logger logger = LoggerFactory.getLogger(LogoutUseCase.class);

	public Cookie execute() {
		Cookie cookie = new Cookie("JWT_TOKEN", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(0);
		cookie.setPath("/");

		logger.info("User logged out successfully. JWT token invalidated.");
		return cookie;
	}
}
