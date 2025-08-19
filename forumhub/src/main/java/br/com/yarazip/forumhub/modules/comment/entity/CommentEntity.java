package br.com.yarazip.forumhub.modules.comment.entity;

import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_comment")
public class CommentEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private UUID id;

	@Column(nullable = false, length = 500)
	private String content;

	@Column(name = "highs_count", nullable = false)
	private Long highsCount = 0L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "topic_id", nullable = false)
	private TopicEntity topic;

	@ManyToOne
	@JoinColumn(name = "parent_comment_id")
	private CommentEntity parentComment;

	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<CommentHighsEntity> highs = new ArrayList<>();

	@OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<CommentEntity> replies = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	public CommentEntity() {
		Instant now = Instant.now();

		this.id = UUID.randomUUID();
		this.createdAt = now;
		this.updatedAt = now;
	}

	public CommentEntity(String content, UserEntity user, TopicEntity topic) {
		this();
		this.content = content;
		this.user = user;
		this.topic = topic;
	}

	public CommentEntity(String content, UserEntity user, TopicEntity topic, CommentEntity parentComment) {
		this(content, user, topic);
		this.setParentComment(parentComment);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getHighsCount() {
		return highsCount;
	}

	public void incrementHighs() {
		this.highsCount++;
	}

	public void decrementHighs() {
		this.highsCount--;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public TopicEntity getTopic() {
		return topic;
	}

	public void setTopic(TopicEntity topic) {
		this.topic = topic;
	}

	public CommentEntity getParentComment() {
		return parentComment;
	}

	public void setParentComment(CommentEntity parentComment) {
		this.parentComment = parentComment;
	}

	public List<CommentHighsEntity> getHighs() {
		return highs;
	}

	public List<CommentEntity> getReplies() {
		return replies;
	}

	public void addReplies(CommentEntity reply) {
		if (reply != null && !this.replies.contains(reply)) {
			this.replies.add(reply);
			reply.parentComment = this;
		}
	}

	public void removeReplies(CommentEntity reply) {
		if (reply != null && this.replies.contains(reply)) {
			this.replies.remove(reply);
			reply.parentComment = null;
		}
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
