package br.com.yarazip.forumhub.modules.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(@Size(max = 50) String name,

		@Size(max = 50) @Pattern(regexp = "^[a-zA-Z0-9_]*$") String username,

		@Email String email,

		@Size(min = 8, max = 50) String password) {
}
