package br.com.yarazip.forumhub.modules.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(@NotBlank @Size(max = 50) String name,

		@NotBlank @Size(max = 50) @Pattern(regexp = "^[a-zA-Z0-9_]*$") String username,

		@NotBlank @Email String email,

		@NotBlank @Size(min = 8, max = 50) String password) {
}