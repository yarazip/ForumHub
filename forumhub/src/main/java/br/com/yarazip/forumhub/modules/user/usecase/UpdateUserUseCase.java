package br.com.yarazip.forumhub.modules.user.usecase;

import br.com.yarazip.forumhub.modules.exception.usecase.ForbiddenException;
import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.user.controller.dto.UserResponseDTO;
import br.com.yarazip.forumhub.modules.user.controller.dto.UserUpdateRequestDTO;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.mapper.UserMapper;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateUserUseCase {

	private static final Logger logger = LoggerFactory.getLogger(UpdateUserUseCase.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UpdateUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserResponseDTO execute(UUID id, UserUpdateRequestDTO requestDTO, UUID authenticatedUserId) {
		UserEntity userDB = userRepository.findById(id).orElseThrow(() -> {
			logger.warn("User with ID {} not found", id);
			return new ResourceNotFoundException("User not found.");
		});

		if (!userDB.getId().equals(authenticatedUserId)) {
			logger.warn("User with ID {} is not allowed to update user with ID {}", authenticatedUserId, id);
			throw new ForbiddenException("You are not allowed to update this user.");
		}

		if (requestDTO == null || (requestDTO.name() == null && requestDTO.username() == null
				&& requestDTO.email() == null && requestDTO.password() == null)) {
			logger.error("No fields to update provided for user with ID {}", id);
			throw new IllegalArgumentException("""
					You must provide at least one field to update:
					- name
					- username
					- email
					- password
					""");
		}

		userDB.setName(requestDTO.name() != null ? requestDTO.name() : userDB.getName());
		userDB.setUsername(requestDTO.username() != null ? requestDTO.username() : userDB.getUsername());
		userDB.setEmail(requestDTO.email() != null ? requestDTO.email() : userDB.getEmail());
		userDB.setPassword(
				requestDTO.password() != null ? passwordEncoder.encode(requestDTO.password()) : userDB.getPassword());
		userDB.setUpdatedAt(Instant.now());

		logger.info("User with ID {} updated successfully", id);
		return UserMapper.toResponseDTO(userRepository.save(userDB));
	}
}
