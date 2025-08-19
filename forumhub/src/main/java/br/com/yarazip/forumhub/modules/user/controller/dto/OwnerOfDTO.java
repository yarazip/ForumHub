package br.com.yarazip.forumhub.modules.user.controller.dto;

import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;

import java.time.Instant;
import java.util.UUID;

public record OwnerOfDTO(UUID id, String name, Instant createdAt) {
	public static OwnerOfDTO from(ForumEntity forum) {
		return new OwnerOfDTO(forum.getOwner().getId(), forum.getOwner().getName(), forum.getOwner().getCreatedAt());
	}
}