package br.com.yarazip.forumhub.modules.topic.usecase;

import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicDetailsResponseDTO;
import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.topic.mapper.TopicMapper;
import br.com.yarazip.forumhub.modules.topic.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetTopicDetailsUseCase {

	private final TopicRepository topicRepository;

	public GetTopicDetailsUseCase(TopicRepository topicRepository) {
		this.topicRepository = topicRepository;
	}

	public TopicDetailsResponseDTO execute(UUID id) {

		TopicEntity topicFound = topicRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Topic not found."));
		topicFound.getHighsCount();
		topicFound.getComments();
		topicFound.getCreator();

		return TopicMapper.toDetailsResponseDTO(topicFound);
	}
}
