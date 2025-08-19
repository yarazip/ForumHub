package br.com.yarazip.forumhub.modules.user.usecase;

import br.com.yarazip.forumhub.modules.user.controller.dto.UserResponseDTO;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import br.com.yarazip.forumhub.modules.user.mapper.UserMapper;
import br.com.yarazip.forumhub.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListUsersUseCase {

	private static final Logger logger = LoggerFactory.getLogger(ListUsersUseCase.class);

	private final UserRepository userRepository;

	public ListUsersUseCase(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserResponseDTO> execute(int page, int size) {
		if (page < 0 || size <= 0) {
			logger.error("Invalid page or size parameters: page={}, size={}", page, size);
			throw new IllegalArgumentException("Page and size must be positive numbers.");
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<UserEntity> entities = userRepository.findAll(pageable);

		logger.info("Retrieved {} users from page {} with size {}", entities.getNumberOfElements(), page, size);
		return entities.getContent().stream().map(UserMapper::toResponseDTO).collect(Collectors.toList());
	}
}
