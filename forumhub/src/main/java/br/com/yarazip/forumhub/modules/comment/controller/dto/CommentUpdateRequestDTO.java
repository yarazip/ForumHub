package br.com.yarazip.forumhub.modules.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequestDTO(@NotBlank @Size(max = 500) String content) {
}