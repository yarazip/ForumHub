package br.com.yarazip.forumhub.modules.forum.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;

public record ForumCreateRequestDTO(@NotBlank @Length(min = 3, max = 50) String name,

		@NotBlank @Length(min = 3, max = 50) String description) {
}
