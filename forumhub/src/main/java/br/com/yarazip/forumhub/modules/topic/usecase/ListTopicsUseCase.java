package br.com.yarazip.forumhub.modules.topic.usecase;

import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicResponseDTO;
import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.topic.mapper.TopicMapper;
import br.com.yarazip.forumhub.modules.topic.repository.TopicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListTopicsUseCase {

	private final TopicRepository topicRepository;

	public ListTopicsUseCase(TopicRepository topicRepository) {
		this.topicRepository = topicRepository;
	}

	public List<TopicResponseDTO> execute(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<TopicEntity> entities = topicRepository.findAll(pageable);

		return entities.getContent().stream().map(TopicMapper::toResponseDTO).toList();
	}
}
