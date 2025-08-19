package br.com.yarazip.forumhub.modules.topic.usecase;

import br.com.yarazip.forumhub.modules.topic.repository.TopicHighsRepository;
import br.com.yarazip.forumhub.modules.topic.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UnHighTopicUseCase {

	private final TopicHighsRepository topicHighsRepository;
	private final TopicRepository topicRepository;

	public UnHighTopicUseCase(TopicHighsRepository topicHighsRepository, TopicRepository topicRepository) {
		this.topicHighsRepository = topicHighsRepository;
		this.topicRepository = topicRepository;
	}

	public void execute(UUID topicId, UUID authenticatedUserId) {
		topicHighsRepository.findByTopic_IdAndUser_Id(topicId, authenticatedUserId)
				.ifPresentOrElse(topicHighsRepository::delete, () -> {
					throw new IllegalArgumentException("Topic not highed");
				});
		topicRepository.findById(topicId).ifPresent((topicEntity) -> {
			topicEntity.decrementHighs();
			topicRepository.save(topicEntity);
		});
	}
}
