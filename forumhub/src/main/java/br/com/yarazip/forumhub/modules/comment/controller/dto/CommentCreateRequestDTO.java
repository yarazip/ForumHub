package br.com.yarazip.forumhub.modules.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record CommentCreateRequestDTO(@NotBlank @Size(max = 500) String content, @UUID String topicId,
		String parentCommentId) {
	public CommentCreateRequestDTO {
		if (parentCommentId != null && parentCommentId.isEmpty()) {
			parentCommentId = null;
		}
	}
}
