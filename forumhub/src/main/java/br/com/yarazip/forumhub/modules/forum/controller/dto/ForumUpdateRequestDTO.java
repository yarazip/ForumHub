package br.com.yarazip.forumhub.modules.forum.controller.dto;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record ForumUpdateRequestDTO(@Size(max = 50) String name,

		@Size(max = 50) String description) {
}