package br.com.yarazip.forumhub.modules.comment.usecase;

import br.com.yarazip.forumhub.modules.comment.controller.dto.CommentResponseDTO;
import br.com.yarazip.forumhub.modules.comment.entity.CommentEntity;
import br.com.yarazip.forumhub.modules.comment.mapper.CommentMapper;
import br.com.yarazip.forumhub.modules.comment.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCommentsUseCase {

	private final CommentMapper commentMapper;
	private final CommentRepository commentRepository;

	public ListCommentsUseCase(CommentMapper commentMapper, CommentRepository commentRepository) {
		this.commentMapper = commentMapper;
		this.commentRepository = commentRepository;
	}

	public List<CommentResponseDTO> execute(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<CommentEntity> entities = commentRepository.findAll(pageable);
		return entities.getContent().stream().filter(comment -> comment.getParentComment() == null)
				.map(commentMapper::toResponseDTO).toList();
	}
}
