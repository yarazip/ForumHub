package br.com.yarazip.forumhub.modules.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginRequestDTO(

		@NotBlank @NotNull String username,

		@NotBlank @NotNull @Length(min = 8, max = 50) String password) {
}