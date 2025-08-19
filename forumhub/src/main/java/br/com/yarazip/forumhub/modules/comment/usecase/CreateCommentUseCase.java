package br.com.yarazip.forumhub.modules.comment.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentCreateRequestDTO;
import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentResponseDTO;
import br.com.yarazip.forumhub.modules.comment.entity.CommentEntity;
import br.com.yarazip.forumhub.modules.comment.mapper.CommentMapper;
import br.com.yarazip.forumhub.modules.comment.repository.CommentRepository;
import br.com.yarazip.forumhub.modules.exception.usecase.ForbiddenException;
import br.com.yarazip.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.topic.repository.TopicRepository;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;

@Service
public class CreateCommentUseCase {

	private final CommentMapper commentMapper;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final TopicRepository topicRepository;

	public CreateCommentUseCase(CommentMapper commentMapper, CommentRepository commentRepository,
			UserRepository userRepository, TopicRepository topicRepository) {
		this.commentMapper = commentMapper;
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.topicRepository = topicRepository;
	}

	public CommentResponseDTO execute(CommentCreateRequestDTO requestDTO, UUID authenticatedUserId) {

		UserEntity user = userRepository.findById(authenticatedUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));

		TopicEntity topic = topicRepository.findById(UUID.fromString(requestDTO.topicId()))
				.orElseThrow(() -> new ResourceNotFoundException("Topic not found."));

		if (!user.participatingInForum(topic.getForum())) {
			throw new ForbiddenException("You are not allowed to create a comment for this topic.");
		}

		CommentEntity parentComment = null;
		if (requestDTO.parentCommentId() != null) {
			parentComment = commentRepository.findById(UUID.fromString(requestDTO.parentCommentId()))
					.orElseThrow(() -> new ResourceNotFoundException("Parent comment not found."));

			if (!parentComment.getTopic().equals(topic)) {
				throw new IllegalArgumentException("Parent comment does not belong to the specified topic.");
			}
		}

		CommentEntity newComment = commentMapper.toEntity(requestDTO, user, topic, parentComment);
		commentRepository.save(newComment);

		topic.incrementComments();
		topicRepository.save(topic);

		return commentMapper.toResponseDTO(newComment);
	}
}
