package br.com.yarazip.forumhub.modules.forum.controlle;

import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumCreateRequestDTO;
import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumResponseDTO;
import br.com.yarazip.forumhub.modules.forum.controller.dto.ForumUpdateRequestDTO;
import br.com.yarazip.forumhub.modules.forum.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/forums")
@Tag(name = "Forum", description = "Operations related to forums")
public class ForumController {

	private final CreateForumUseCase createForumUseCase;
	private final ListForumsUseCase listForumsUseCase;
	private final GetForumUseCase getForumUseCase;
	private final UpdateForumUseCase updateForumUseCase;
	private final DeleteForumUseCase deleteForumUseCase;
	private final HighForumUseCase highForumUseCase;
	private final UnHighForumUseCase unHighForumUseCase;

	public ForumController(CreateForumUseCase createForumUseCase, ListForumsUseCase listForumsUseCase,
			GetForumUseCase getForumUseCase, UpdateForumUseCase updateForumUseCase,
			DeleteForumUseCase deleteForumUseCase, HighForumUseCase highForumUseCase,
			UnHighForumUseCase unHighForumUseCase) {
		this.createForumUseCase = createForumUseCase;
		this.listForumsUseCase = listForumsUseCase;
		this.getForumUseCase = getForumUseCase;
		this.updateForumUseCase = updateForumUseCase;
		this.deleteForumUseCase = deleteForumUseCase;
		this.highForumUseCase = highForumUseCase;
		this.unHighForumUseCase = unHighForumUseCase;
	}

	@PostMapping
	@Operation(summary = "Create a new forum")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Forum created"),
			@ApiResponse(responseCode = "400", description = "Invalid request data"),
			@ApiResponse(responseCode = "409", description = "Forum already exists") })
	public ResponseEntity<ForumResponseDTO> createForum(@Valid @RequestBody ForumCreateRequestDTO requestDTO) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		ForumResponseDTO responseDTO = createForumUseCase.execute(requestDTO, authenticatedUserId);

		URI uri = URI.create("/forums/" + responseDTO.id());
		return ResponseEntity.created(uri).body(responseDTO);
	}

	@GetMapping("/all")
	@Operation(summary = "List all forums")
	@ApiResponse(responseCode = "200", description = "Forums listed")
	public ResponseEntity<List<ForumResponseDTO>> listForums(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return ResponseEntity.ok(listForumsUseCase.execute(page, size));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get forum by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Forum retrieved"),
			@ApiResponse(responseCode = "404", description = "Forum not found") })
	public ResponseEntity<ForumResponseDTO> getForum(@PathVariable UUID id) {

		return ResponseEntity.ok(getForumUseCase.execute(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update forum by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Forum updated"),
			@ApiResponse(responseCode = "400", description = "Invalid request data"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Forum not found") })
	public ResponseEntity<ForumResponseDTO> updateForum(
			@Valid @PathVariable @org.hibernate.validator.constraints.UUID String id,
			@Valid @RequestBody ForumUpdateRequestDTO forumRequestDTO) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		return ResponseEntity.ok(updateForumUseCase.execute(UUID.fromString(id), forumRequestDTO, authenticatedUserId));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete forum by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Forum deleted"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Forum not found") })
	public ResponseEntity<Void> deleteForum(@Valid @PathVariable @org.hibernate.validator.constraints.UUID String id) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		deleteForumUseCase.execute(UUID.fromString(id), authenticatedUserId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "High forum by ID", description = "High a forum by its unique identifier")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Forum high"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Forum not found") })
	@PostMapping("/high/{id}")
	public ResponseEntity<Void> highForum(
			@Valid @PathVariable("id") @org.hibernate.validator.constraints.UUID String id) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		highForumUseCase.execute(UUID.fromString(id), authenticatedUserId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Unhigh forum by ID", description = "Unhigh a forum by its unique identifier")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Forum unhigh"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Forum not found") })
	@DeleteMapping("/unhigh/{id}")
	public ResponseEntity<Void> unHighForum(
			@Valid @PathVariable("id") @org.hibernate.validator.constraints.UUID String id) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		unHighForumUseCase.execute(UUID.fromString(id), authenticatedUserId);
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
