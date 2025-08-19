package br.com.yarazip.forumhub.modules.auth.usecase;

import br.com.yarazip.forumhub.modules.auth.controller.dto.LoginRequestDTO;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import br.com.yarazip.forumhub.security.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

	private static final Logger logger = LoggerFactory.getLogger(LoginUseCase.class);

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final CookieUtil cookieUtil;

	public LoginUseCase(UserRepository userRepository, AuthenticationManager authenticationManager,
			CookieUtil cookieUtil) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.cookieUtil = cookieUtil;
	}

    public Cookie execute(LoginRequestDTO requestDTO) {
		authenticateUser(requestDTO.username(), requestDTO.password());
		UserEntity user = getUserEntity(requestDTO.username());
		Cookie jwtCookie = cookieUtil.generateCookieWithToken(user);

		logger.info("User {} successfully authenticated and cookie generated.", requestDTO.username());
		return jwtCookie;
	}

	private void authenticateUser(String username, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			logger.debug("Authentication successful for username: {}", username);
		} catch (AuthenticationException ex) {
			logger.warn("Authentication failed for username: {}", username);
			throw new BadCredentialsException("Invalid username or password", ex);
		}
	}

	private UserEntity getUserEntity(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> {
			logger.error("User not found: {}", username);
			return new UsernameNotFoundException("User not found");
		});
	}
}
