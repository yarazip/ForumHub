package br.com.yarazip.forumhub.modules.topic.mapper;

import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicCreateRequestDTO;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicDetailsResponseDTO;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicResponseDTO;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicUpdateRequestDTO;
import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;

import java.util.List;

public class TopicMapper {

	public static TopicEntity toEntity(TopicCreateRequestDTO dto, ForumEntity forum, UserEntity creator) {
		return new TopicEntity(dto.title(), dto.content(), creator, forum);
	}

	public static TopicEntity toEntity(TopicUpdateRequestDTO dto) {
		return new TopicEntity(dto.title(), dto.content());
	}

	public static TopicResponseDTO toResponseDTO(TopicEntity topic) {
		return new TopicResponseDTO(topic.getId(), topic.getTitle(), topic.getContent(), topic.getForum().getId(),
				topic.getCreator().getId(), topic.getCreator().getUsername(), topic.getHighsCount(),
				topic.getCommentsCount(), topic.getCreatedAt(), topic.getUpdatedAt());
	}

	public static TopicDetailsResponseDTO toDetailsResponseDTO(TopicEntity topic) {
		return new TopicDetailsResponseDTO(topic.getId(), topic.getTitle(), topic.getContent(),
				topic.getForum().getId(), topic.getCreator().getId(), topic.getCreator().getUsername(),
				topic.getHighsCount(), topic.getCommentsCount(), List.of(), topic.getCreatedAt(), topic.getUpdatedAt());
	}
}
