package br.com.yarazip.forumhub.modules.user.mapper;

import br.com.yarazip.forumhub.modules.user.controller.dto.*;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;

public class UserMapper {

	public static UserEntity toEntity(UserCreateRequestDTO dto) {
		return new UserEntity(dto.name(), dto.username(), dto.email(), dto.password());
	}

	public static UserEntity toEntity(UserUpdateRequestDTO dto) {
		return new UserEntity(dto.name(), dto.username(), dto.email(), dto.password());
	}

	public static UserResponseDTO toResponseDTO(UserEntity entity) {
		return new UserResponseDTO(entity.getId(), entity.getName(), entity.getUsername(), entity.getHighsCount(),
				entity.getCreatedAt(), entity.getUpdatedAt());
	}

	public static UserDetailsResponseDTO toDetailsResponseDTO(UserEntity entity) {
		return new UserDetailsResponseDTO(entity.getId(), entity.getName(), entity.getUsername(), entity.getEmail(),
				entity.getOwnedForums().stream().map(OwnerOfDTO::from).toList(),
				entity.getParticipatingForums().stream().map(ParticipatesInDTO::from).toList(), entity.getHighsCount(),
				entity.getCreatedAt(), entity.getUpdatedAt());
	}
}


