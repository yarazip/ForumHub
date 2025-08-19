package br.com.yarazip.forumhub.modules.forum.usecase;

import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumResponseDTO;
import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;
import br.com.yarazip.forumhub.modules.forum.mapper.ForumMapper;
import br.com.yarazip.forumhub.modules.forum.repository.ForumRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetForumUseCase {

	private final ForumRepository forumRepository;
	private final ForumMapper forumMapper;

	public GetForumUseCase(ForumRepository forumRepository, ForumMapper forumMapper) {
		this.forumRepository = forumRepository;
		this.forumMapper = forumMapper;
	}

	public ForumResponseDTO execute(UUID id) {

		ForumEntity forumFound = forumRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Forum not found."));

		return forumMapper.toResponseDTO(forumFound);
	}
}
