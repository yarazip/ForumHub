package br.com.yarazip.forumhub.security.utils;

import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.security.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

	private final CustomUserDetailsService userDetailsService;

	public CookieUtil(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public Cookie generateCookieWithToken(UserEntity user) {
		if (user == null) {
			throw new NullPointerException("User cannot be null");
		}

		String jwt = userDetailsService.generateToken(user);

		Cookie cookie = new Cookie("JWT_TOKEN", jwt);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(60 * 60 * 24 * 7);
		cookie.setPath("/");

		return cookie;
	}
}
