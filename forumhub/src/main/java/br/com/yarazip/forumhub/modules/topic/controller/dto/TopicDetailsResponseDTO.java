package br.com.yarazip.forumhub.modules.topic.controller.dto;

import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentResponseDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TopicDetailsResponseDTO(UUID id, String title, String content, UUID forumId, UUID creatorId,
		String creatorUsername, Long highs, Long commentCount, List<CommentResponseDTO> comments, Instant createdAt,
		Instant updatedAt) {
}
