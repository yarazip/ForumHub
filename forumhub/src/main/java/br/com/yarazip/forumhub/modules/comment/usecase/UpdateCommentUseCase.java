package br.com.yarazip.forumhub.modules.comment.usecase;

import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentResponseDTO;
import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentUpdateRequestDTO;
import br.com.yarazip.forumhub.modules.comment.entity.CommentEntity;
import br.com.yarazip.forumhub.modules.comment.mapper.CommentMapper;
import br.com.yarazip.forumhub.modules.comment.repository.CommentRepository;
import br.com.yarazip.forumhub.modules.exception.usecase.ForbiddenException;
import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.topic.repository.TopicRepository;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateCommentUseCase {

	private final CommentMapper commentMapper;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final TopicRepository topicRepository;

	public UpdateCommentUseCase(CommentMapper commentMapper, CommentRepository commentRepository,
			UserRepository userRepository, TopicRepository topicRepository) {
		this.commentMapper = commentMapper;
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.topicRepository = topicRepository;
	}

	public CommentResponseDTO execute(UUID id, CommentUpdateRequestDTO requestDTO, UUID authenticatedUserId) {

		CommentEntity commentFound = commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment not found."));

		if (requestDTO.content().equals(commentFound.getContent())) {
			throw new IllegalArgumentException("New content must be different from the old content");
		}
		if (requestDTO.content().isEmpty()) {
			throw new IllegalArgumentException("Comment content cannot be empty");
		}

		if (!commentFound.getUser().getId().equals(authenticatedUserId)) {
			throw new ForbiddenException("You are not allowed to update a comment for another user");
		}

		UserEntity user = userRepository.findById(authenticatedUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));

		TopicEntity topic = topicRepository.findById(commentFound.getTopic().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Topic not found."));
		if (!user.participatingInForum(topic.getForum())) {
			throw new ForbiddenException("You are not allowed to update a comment for this topic");
		}

		commentFound.setContent(requestDTO.content());
		commentFound.setUpdatedAt(Instant.now());

		commentRepository.save(commentFound);
		return commentMapper.toResponseDTO(commentFound);
	}
}