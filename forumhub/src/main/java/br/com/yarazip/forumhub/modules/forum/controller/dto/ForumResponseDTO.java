package br.com.yarazip.forumhub.modules.forum.controller.dto;

import java.time.Instant;
import java.util.UUID;

public record ForumResponseDTO(UUID id, String name, String description, UUID owner, Long highs, int participants,
		Long topicCount, Instant createdAt, Instant updatedAt) {
}