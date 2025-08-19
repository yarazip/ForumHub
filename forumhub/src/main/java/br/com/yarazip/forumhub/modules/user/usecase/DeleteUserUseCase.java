package br.com.yarazip.forumhub.modules.user.usecase;

import br.com.yarazip.forumhub.modules.exception.usecase.ForbiddenException;
import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteUserUseCase {

	private static final Logger logger = LoggerFactory.getLogger(DeleteUserUseCase.class);

	private final UserRepository userRepository;

	public DeleteUserUseCase(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void execute(UUID id, UUID authenticatedUserId) {
		UserEntity userDB = userRepository.findById(id).orElseThrow(() -> {
			logger.error("User with ID {} not found.", id);
			return new ResourceNotFoundException("User not found.");
		});

		if (!userDB.getId().equals(authenticatedUserId)) {
			logger.error("User with ID {} is not allowed to delete user with ID {}.", authenticatedUserId, id);
			throw new ForbiddenException("You are not allowed to delete this user.");
		}

		logger.info("Deleting user with ID {}", id);
		userRepository.delete(userDB);
	}
}
