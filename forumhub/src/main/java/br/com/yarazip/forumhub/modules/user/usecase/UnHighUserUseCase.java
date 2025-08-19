package br.com.yarazip.forumhub.modules.user.usecase;

import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.exception.usecase.UnauthorizedException;
import br.com.yarazip.forumhub.modules.user.repository.UserHighsRepository;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UnHighUserUseCase {

	private static final Logger logger = LoggerFactory.getLogger(UnHighUserUseCase.class);

	private final UserHighsRepository userHighsRepository;
	private final UserRepository userRepository;

	public UnHighUserUseCase(UserHighsRepository userHighsRepository, UserRepository userRepository) {
		this.userHighsRepository = userHighsRepository;
		this.userRepository = userRepository;
	}

	public void execute(UUID unHighedUser, UUID authenticatedUserId) {
		if (authenticatedUserId == null) {
			logger.error("User not authenticated");
			throw new UnauthorizedException("User not authenticated");
		}

		if (unHighedUser.equals(authenticatedUserId)) {
			logger.warn("User with ID {} cannot unhigh himself", authenticatedUserId);
			throw new IllegalArgumentException("User cannot unhigh himself");
		}

		userHighsRepository.findByHighedUser_IdAndHighingUser_Id(unHighedUser, authenticatedUserId)
				.ifPresentOrElse((userHighsEntity) -> {
					logger.info("User with ID {} unhighed", unHighedUser);
					userHighsRepository.delete(userHighsEntity);
				}, () -> {
					logger.warn("User with ID {} not highed", unHighedUser);
					throw new ResourceNotFoundException("User not highed");
				});
		userRepository.findById(unHighedUser).ifPresent(userEntity -> {
			userEntity.decrementHighs();
			userRepository.save(userEntity);
		});
	}
}
