package br.com.yarazip.forumhub.modules.topic.controller.dto;

import java.time.Instant;
import java.util.UUID;

public record TopicResponseDTO(UUID id, String title, String content, UUID forumId, UUID creatorId,
		String creatorUsername, Long highs, Long commentCount, Instant createdAt, Instant updatedAt) {
}
