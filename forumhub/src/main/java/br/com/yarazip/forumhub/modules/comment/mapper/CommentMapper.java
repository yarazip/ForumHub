package br.com.yarazip.forumhub.modules.comment.mapper;

import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentCreateRequestDTO;
import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentResponseDTO;
import br.com.yarazip.forumhub.modules.comment.entity.CommentEntity;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicResponseDTO;
import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.topic.mapper.TopicMapper;
import br.com.yarazip.forumhub.modules.user.controller.dto.UserResponseDTO;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CommentMapper {

	private CommentMapper() {
	}

	public CommentEntity toEntity(CommentCreateRequestDTO dto, UserEntity userEntity, TopicEntity topicEntity) {
		return new CommentEntity(dto.content(), userEntity, topicEntity);
	}

	public CommentEntity toEntity(CommentCreateRequestDTO dto, UserEntity userEntity, TopicEntity topicEntity,
			CommentEntity parentComment) {
		if (parentComment == null) {
			return toEntity(dto, userEntity, topicEntity);
		}
		return new CommentEntity(dto.content(), userEntity, topicEntity, parentComment);
	}

	public CommentResponseDTO toResponseDTO(CommentEntity commentEntity) {

		UserResponseDTO user = UserMapper.toResponseDTO(commentEntity.getUser());
		TopicResponseDTO topic = TopicMapper.toResponseDTO(commentEntity.getTopic());
		UUID parentComment = commentEntity.getParentComment() != null ? commentEntity.getParentComment().getId() : null;

		List<CommentResponseDTO> replies = commentEntity.getReplies().stream().map(this::toResponseDTO).toList();

		return new CommentResponseDTO(commentEntity.getId(), commentEntity.getContent(), user.id(), topic.id(),
				commentEntity.getHighsCount(), parentComment, replies, commentEntity.getCreatedAt(),
				commentEntity.getUpdatedAt());
	}
}