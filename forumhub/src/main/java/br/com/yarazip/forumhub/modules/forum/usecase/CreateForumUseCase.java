package br.com.yarazip.forumhub.modules.forum.usecase;

import br.com.yarazip.forumhub.modules.exception.usecase.ResourceAlreadyExistsException;
import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumCreateRequestDTO;
import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumResponseDTO;
import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;
import br.com.yarazip.forumhub.modules.forum.mapper.ForumMapper;
import br.com.yarazip.forumhub.modules.forum.repository.ForumRepository;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateForumUseCase {

	private final ForumRepository forumRepository;
	private final UserRepository userRepository;
	private final ForumMapper forumMapper;

	public CreateForumUseCase(ForumRepository forumRepository, UserRepository userRepository, ForumMapper forumMapper) {
		this.forumRepository = forumRepository;
		this.userRepository = userRepository;
		this.forumMapper = forumMapper;
	}

	@Transactional
	public ForumResponseDTO execute(ForumCreateRequestDTO requestDTO, UUID authenticatedUserId) {

		UserEntity user = userRepository.findById(authenticatedUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));

		if (forumRepository.existsByName(requestDTO.name()).equals(true)) {
			throw new ResourceAlreadyExistsException("Forum already exists.");
		}

		ForumEntity forum = forumMapper.toEntity(requestDTO, user);
		forum.addParticipant(user);
		user.addOwnedForum(forum);

		userRepository.save(user);
		return forumMapper.toResponseDTO(forum);
	}
}
