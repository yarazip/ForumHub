package br.com.yarazip.forumhub.modules.user.controller.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserDetailsResponseDTO(UUID id, String name, String username, String email, List<OwnerOfDTO> ownerOf,
		List<ParticipatesInDTO> participatesIn, Long highs, Instant createdAt, Instant updatedAt) {
}
