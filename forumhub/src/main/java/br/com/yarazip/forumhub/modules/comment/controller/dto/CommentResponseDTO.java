package br.com.yarazip.forumhub.modules.comment.controller.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CommentResponseDTO(UUID id, String content, UUID user, UUID topic, Long highs, UUID parentCommentId,
		List<CommentResponseDTO> replies, Instant createdAt, Instant updatedAt) {
}
