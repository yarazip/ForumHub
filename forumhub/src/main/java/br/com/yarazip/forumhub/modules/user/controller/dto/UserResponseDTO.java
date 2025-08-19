package br.com.yarazip.forumhub.modules.user.controller.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(UUID id, String name, String username, Long highs, Instant createdAt, Instant updatedAt) {
}
