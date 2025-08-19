package br.com.yarazip.forumhub.modules.forum.usecase;

import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;
import br.com.yarazip.forumhub.modules.forum.entity.ForumHighsEntity;
import br.com.yarazip.forumhub.modules.forum.repository.ForumHighsRepository;
import br.com.yarazip.forumhub.modules.forum.repository.ForumRepository;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HighForumUseCase {

	private final ForumHighsRepository forumHighsRepository;
	private final ForumRepository forumRepository;
	private final UserRepository userRepository;

	public HighForumUseCase(ForumHighsRepository forumHighsRepository, ForumRepository forumRepository,
			UserRepository userRepository) {
		this.forumHighsRepository = forumHighsRepository;
		this.forumRepository = forumRepository;
		this.userRepository = userRepository;
	}

	public void execute(UUID forumId, UUID authenticatedUserId) {
		forumHighsRepository.findByForum_IdAndUser_Id(forumId, authenticatedUserId).ifPresent(userHighsEntity -> {
			throw new IllegalArgumentException("Forum already highed");
		});

		ForumEntity forumFound = forumRepository.findById(forumId)
				.orElseThrow(() -> new IllegalArgumentException("Forum not found"));

		UserEntity authenticatedUserEntity = userRepository.findById(authenticatedUserId)
				.orElseThrow(() -> new IllegalArgumentException("Forum not found"));

		forumFound.incrementHighs();
		forumHighsRepository.save(new ForumHighsEntity(forumFound, authenticatedUserEntity));
	}
}
