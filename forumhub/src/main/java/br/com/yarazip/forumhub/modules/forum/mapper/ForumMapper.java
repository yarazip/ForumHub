package br.com.yarazip.forumhub.modules.forum.mapper;

import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumCreateRequestDTO;
import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumResponseDTO;
import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumUpdateRequestDTO;
import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ForumMapper {

	public ForumEntity toEntity(ForumCreateRequestDTO dto, UserEntity owner) {
		if (owner == null) {
			throw new IllegalArgumentException("Owner cannot be null");
		}
		if ((dto.name() == null || dto.name().isEmpty() || dto.name().isBlank())
				|| (dto.description() == null || dto.description().isEmpty() || dto.description().isBlank())) {
			throw new IllegalArgumentException("Forum name and description cannot be empty");
		}
		return new ForumEntity(dto.name(), dto.description(), owner);
	}

	public ForumEntity toEntity(ForumUpdateRequestDTO dto, UserEntity owner) {
		return new ForumEntity(dto.name(), dto.description(), owner);
	}

	public ForumResponseDTO toResponseDTO(ForumEntity entity) {
		if (entity == null || entity.getOwner() == null) {
			throw new IllegalArgumentException("Forum owner cannot be null");
		}
		return new ForumResponseDTO(entity.getId(), entity.getName(), entity.getDescription(),
				entity.getOwner().getId(), entity.getHighsCount(), entity.getParticipants().size(),
				entity.getTopicsCount(), entity.getCreatedAt(), entity.getUpdatedAt());
	}
}
