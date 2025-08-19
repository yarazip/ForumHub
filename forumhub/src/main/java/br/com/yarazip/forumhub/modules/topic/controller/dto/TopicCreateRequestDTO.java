package br.com.yarazip.forumhub.modules.topic.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record TopicCreateRequestDTO(@NotBlank @Size(max = 50) String title,

		@NotBlank @Size(max = 500) String content,

		@UUID String forumId) {
}
