package br.com.yarazip.forumhub.modules.user.usecase;

import br.com.yarazip.forumhub.modules.exception.usecase.ResourceAlreadyExistsException;
import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.exception.usecase.UnauthorizedException;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.entity.UserHighsEntity;
import br.com.yarazip.forumhub.modules.user.repository.UserHighsRepository;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HighUserUseCase {

	private static final Logger logger = LoggerFactory.getLogger(HighUserUseCase.class);

	private final UserHighsRepository userHighsRepository;
	private final UserRepository userRepository;

	public HighUserUseCase(UserHighsRepository userHighsRepository, UserRepository userRepository) {
		this.userHighsRepository = userHighsRepository;
		this.userRepository = userRepository;
	}

	public void execute(UUID highedUser, UUID authenticatedUserId) {
		if (authenticatedUserId == null) {
			logger.error("User not authenticated");
			throw new UnauthorizedException("User not authenticated");
		}

		if (highedUser.equals(authenticatedUserId)) {
			logger.warn("User with ID {} cannot high himself", authenticatedUserId);
			throw new IllegalArgumentException("User cannot high himself");
		}

		userHighsRepository.findByHighedUser_IdAndHighingUser_Id(highedUser, authenticatedUserId)
				.ifPresent(userHighsEntity -> {
					logger.warn("User with ID {} already highed", authenticatedUserId);
					throw new ResourceAlreadyExistsException("User already highed");
				});

		UserEntity highedUserEntity = userRepository.findById(highedUser).orElseThrow(() -> {
			logger.error("User to be highed with ID {} not found", highedUser);
			return new ResourceNotFoundException("User not found");
		});

		UserEntity authenticatedUserEntity = userRepository.findById(authenticatedUserId).orElseThrow(() -> {
			logger.error("User with ID {} not found", authenticatedUserId);
			return new ResourceNotFoundException("User not found");
		});

		highedUserEntity.incrementHighs();
		logger.info("User with ID {} highed user with ID {}", authenticatedUserId, highedUser);
		userHighsRepository.save(new UserHighsEntity(highedUserEntity, authenticatedUserEntity));
	}
}
