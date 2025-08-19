package br.com.yarazip.forumhub.modules.forum.usecase;

import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumResponseDTO;
import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;
import br.com.yarazip.forumhub.modules.forum.mapper.ForumMapper;
import br.com.yarazip.forumhub.modules.forum.repository.ForumRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListForumsUseCase {

	private final ForumRepository forumRepository;
	private final ForumMapper forumMapper;

	public ListForumsUseCase(ForumRepository forumRepository, ForumMapper forumMapper) {
		this.forumRepository = forumRepository;
		this.forumMapper = forumMapper;
	}

	public List<ForumResponseDTO> execute(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<ForumEntity> entities = forumRepository.findAll(pageable);

		return entities.getContent().stream().map(forumMapper::toResponseDTO).toList();
	}
}
