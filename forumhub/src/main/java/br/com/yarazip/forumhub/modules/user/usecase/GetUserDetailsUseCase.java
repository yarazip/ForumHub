package br.com.yarazip.forumhub.modules.user.usecase;

import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.user.controller.dto.UserResponseDTO;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.mapper.UserMapper;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetUserDetailsUseCase {

	private static final Logger logger = LoggerFactory.getLogger(GetUserDetailsUseCase.class);

	private final UserRepository userRepository;

	public GetUserDetailsUseCase(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserResponseDTO execute(UUID id) {
		UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
			logger.warn("User with ID {} not found.", id);
			return new ResourceNotFoundException("User with ID " + id + " not found.");
		});

		logger.info("User with ID {} found.", id);
		return UserMapper.toResponseDTO(userEntity);
	}
}
