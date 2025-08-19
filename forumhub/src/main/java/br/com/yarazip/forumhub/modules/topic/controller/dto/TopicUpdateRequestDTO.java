package br.com.yarazip.forumhub.modules.topic.controller.dto;

import jakarta.validation.constraints.Size;

public record TopicUpdateRequestDTO(@Size(max = 50) String title, @Size(max = 500) String content) {
}
