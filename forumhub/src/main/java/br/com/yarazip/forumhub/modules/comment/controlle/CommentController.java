package br.com.yarazip.forumhub.modules.comment.controlle;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentCreateRequestDTO;
import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentResponseDTO;
import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentUpdateRequestDTO;
import br.com.yarazip.forumhub.modules.comment.usecase.CreateCommentUseCase;
import br.com.yarazip.forumhub.modules.comment.usecase.DeleteCommentUseCase;
import br.com.yarazip.forumhub.modules.comment.usecase.HighCommentUseCase;
import br.com.yarazip.forumhub.modules.comment.usecase.ListCommentsUseCase;
import br.com.yarazip.forumhub.modules.comment.usecase.UnHighCommentUseCase;
import br.com.yarazip.forumhub.modules.comment.usecase.UpdateCommentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "Comments", description = "Operations related to comments")
public class CommentController {

	private final CreateCommentUseCase createCommentUseCase;
	private final ListCommentsUseCase listCommentsUseCase;
	private final UpdateCommentUseCase updateCommentUseCase;
	private final DeleteCommentUseCase deleteCommentUseCase;
	private final HighCommentUseCase highCommentUseCase;
	private final UnHighCommentUseCase unHighCommentUseCase;

	public CommentController(CreateCommentUseCase createCommentUseCase, ListCommentsUseCase listCommentsUseCase,
			UpdateCommentUseCase updateCommentUseCase, DeleteCommentUseCase deleteCommentUseCase,
			HighCommentUseCase highCommentUseCase, UnHighCommentUseCase unHighCommentUseCase) {
		this.createCommentUseCase = createCommentUseCase;
		this.listCommentsUseCase = listCommentsUseCase;
		this.updateCommentUseCase = updateCommentUseCase;
		this.deleteCommentUseCase = deleteCommentUseCase;
		this.highCommentUseCase = highCommentUseCase;
		this.unHighCommentUseCase = unHighCommentUseCase;
	}

	@PostMapping
	@Operation(summary = "Create a new comment")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Comment created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request data"),
			@ApiResponse(responseCode = "409", description = "Comment already exists") })
	public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CommentCreateRequestDTO requestDTO) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		return ResponseEntity.ok(createCommentUseCase.execute(requestDTO, authenticatedUserId));
	}

	@GetMapping("/all")
	@Operation(summary = "List all comments")
	@ApiResponse(responseCode = "200", description = "Comments listed successfully")
	public ResponseEntity<List<CommentResponseDTO>> listComments(
			@Valid @RequestParam(defaultValue = "0") @Min(0) int page,
			@Valid @RequestParam(defaultValue = "5") @Min(5) int size) {
		return ResponseEntity.ok(listCommentsUseCase.execute(page, size));
	}

    @PutMapping("/{id}")
	@Operation(summary = "Update an existing comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<CommentResponseDTO> updateComment(@Valid @PathVariable
                                                            @org.hibernate.validator.constraints.UUID String id,
                                                            @RequestBody CommentUpdateRequestDTO requestDTO) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        return ResponseEntity.ok(updateCommentUseCase.execute(UUID.fromString(id), requestDTO, authenticatedUserId));
    }

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete comment by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Comment deleted"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Comment not found") })
	public ResponseEntity<Void> deleteComment(
			@Valid @PathVariable @org.hibernate.validator.constraints.UUID String id) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		deleteCommentUseCase.execute(UUID.fromString(id), authenticatedUserId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "High a comment", description = "High a comment by its unique identifier")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Comment highed"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Comment not found") })
	@PostMapping("/high/{id}")
	public ResponseEntity<Void> highComment(
			@Valid @PathVariable("id") @org.hibernate.validator.constraints.UUID String commentId) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		highCommentUseCase.execute(UUID.fromString(commentId), authenticatedUserId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Unhigh a comment", description = "Unhigh a comment by its unique identifier")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Comment unhighed"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "Comment not found") })
	@DeleteMapping("/unhigh/{id}")
	public ResponseEntity<Void> unHighComment(
			@Valid @PathVariable("id") @org.hibernate.validator.constraints.UUID String commentId) {
		UUID authenticatedUserId = getAuthenticatedUserId();
		unHighCommentUseCase.execute(UUID.fromString(commentId), authenticatedUserId);
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