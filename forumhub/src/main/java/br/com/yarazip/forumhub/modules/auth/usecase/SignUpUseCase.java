package br.com.yarazip.forumhub.modules.auth.usecase;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.yarazip.forumhub.modules.exception.usecase.ResourceAlreadyExistsException;
import br.com.yarazip.forumhub.modules.user.controller.dto.UserCreateRequestDTO;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.mapper.UserMapper;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import br.com.yarazip.forumhub.security.utils.CookieUtil;
import jakarta.servlet.http.Cookie;

@Service
public class SignUpUseCase {

	private static final Logger logger = LoggerFactory.getLogger(SignUpUseCase.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CookieUtil cookieUtil;

	public SignUpUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, CookieUtil cookieUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.cookieUtil = cookieUtil;
	}

	public Map<String, Object> execute(UserCreateRequestDTO requestDTO) {
		validateUsernameAvailability(requestDTO.username());

		UserEntity user = createUserEntity(requestDTO);
		userRepository.save(user);

		Cookie jwtCookie = cookieUtil.generateCookieWithToken(user);

		logger.info("User {} registered successfully", requestDTO.username());

		return Map.of("user", UserMapper.toDetailsResponseDTO(user), "cookie", jwtCookie);
	}

	private void validateUsernameAvailability(String username) {
		if (userRepository.existsByUsername(username)) {
			logger.warn("Attempt to register with existing username: {}", username);
			throw new ResourceAlreadyExistsException("Username already exists");
		}
	}

	private UserEntity createUserEntity(UserCreateRequestDTO requestDTO) {
		UserEntity user = new UserEntity();
		user.setUsername(requestDTO.username());
		user.setPassword(passwordEncoder.encode(requestDTO.password()));
		user.setName(requestDTO.name());
		user.setEmail(requestDTO.email());

		return user;
	}
}
