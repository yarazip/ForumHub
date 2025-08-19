package br.com.yarazip.forumhub.modules.topic.controlle;

import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumResponseDTO;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicCreateRequestDTO;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicDetailsResponseDTO;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicResponseDTO;
import br.com.yarazip.forumhub.modules.topic.controller.dto.TopicUpdateRequestDTO;
import br.com.yarazip.forumhub.modules.topic.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/topics")
@Tag(name = "Topic", description = "Operations related to topics")
public class TopicController {

	private final CreateTopicUseCase createTopicUseCase;
	private final ListTopicsUseCase listTopicsUseCase;
	private final GetTopicDetailsUseCase getTopicDetailsUseCase;
	private final UpdateTopicUseCase updateTopicUseCase;
	private final DeleteTopicUseCase deleteTopicUseCase;
	private final HighTopicUseCase highTopicUseCase;
	private final UnHighTopicUseCase unHighTopicUseCase;

	public TopicController(CreateTopicUseCase createTopicUseCase, ListTopicsUseCase listTopicsUseCase,
			GetTopicDetailsUseCase getTopicDetailsUseCase, UpdateTopicUseCase updateTopicUseCase,
			DeleteTopicUseCase deleteTopicUseCase, HighTopicUseCase highTopicUseCase,
			UnHighTopicUseCase unHighTopicUseCase) {
		this.createTopicUseCase = createTopicUseCase;
		this.getTopicDetailsUseCase = getTopicDetailsUseCase;
		this.listTopicsUseCase = listTopicsUseCase;
		this.updateTopicUseCase = updateTopicUseCase;
		this.deleteTopicUseCase = deleteTopicUseCase;
		this.highTopicUseCase = highTopicUseCase;
		this.unHighTopicUseCase = unHighTopicUseCase;
	}

	@PostMapping
	@Operation(summary = "Create a new topic")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Topic created"),
			@ApiResponse(responseCode = "400", description = "Invalid request data"),
			@ApiResponse(responseCode = "409", description = "Topic already exists") })
	public ResponseEntity<TopicResponseDTO> createTopic(@Valid @RequestBody TopicCreateRequestDTO requestDTO) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		TopicResponseDTO responseDTO = createTopicUseCase.execute(requestDTO, authenticatedUserId);

		URI location = URI.create("/topics/" + responseDTO.id());
		return ResponseEntity.created(location).body(responseDTO);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get topic details by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Topic found"),
			@ApiResponse(responseCode = "404", description = "Topic not found") })
	public ResponseEntity<TopicDetailsResponseDTO> getTopicDetails(
			@Valid @PathVariable @org.hibernate.validator.constraints.UUID String id) {
		return ResponseEntity.ok(getTopicDetailsUseCase.execute(UUID.fromString(id)));
	}

	@GetMapping("/all")
	@Operation(summary = "List all topics with pagination support")
	@ApiResponse(responseCode = "200", description = "Topics found")
	public ResponseEntity<List<TopicResponseDTO>> listForumsPageable(
			@Valid @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page number must be greater than or equal to 0") int page,
			@Valid @RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(listTopicsUseCase.execute(page, size));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a topic by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Topic updated"),
			@ApiResponse(responseCode = "400", description = "Invalid request data"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Topic not found") })
	public ResponseEntity<TopicResponseDTO> updateTopic(
			@Valid @PathVariable @org.hibernate.validator.constraints.UUID String id,
			@Valid @RequestBody TopicUpdateRequestDTO requestDTO) {
		UUID authenticatedUserId = getAuthenticatedUserId();

		return ResponseEntity.ok(updateTopicUseCase.execute(UUID.fromString(id), requestDTO, authenticatedUserId));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a topic by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Topic deleted"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Topic not found") })
	public ResponseEntity<Void> deleteTopic(@Valid @PathVariable @org.hibernate.validator.constraints.UUID String id) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		deleteTopicUseCase.execute(UUID.fromString(id), authenticatedUserId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "High a topic by ID", description = "High a topic by its unique identifier")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Topic high"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Topic not found") })
	@PostMapping("/high/{id}")
	public ResponseEntity<Void> highTopic(
			@Valid @PathVariable("id") @org.hibernate.validator.constraints.UUID String topicId) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		highTopicUseCase.execute(UUID.fromString(topicId), authenticatedUserId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "UnHigh a topic by ID", description = "UnHigh a topic by its unique identifier")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Topic unHigh"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Topic not found") })
	@DeleteMapping("/unhigh/{id}")
	public ResponseEntity<Void> unHighTopic(
			@Valid @PathVariable("id") @org.hibernate.validator.constraints.UUID String topicId) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		unHighTopicUseCase.execute(UUID.fromString(topicId), authenticatedUserId);
		return ResponseEntity.noContent().build();
	}

	private UUID getAuthenticatedUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UUID) {
			return (UUID) principal;
		} else if (principal instanceof String) {
			return UUID.fromString((String) principal);
		}

		throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
	}
}
