package br.com.yarazip.forumhub.modules.comment.entity;

import br.com.yarazip.forumhub.config.UlidGenerator;
import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "tb_comment_highs")
public class CommentHighsEntity {

	@Id
	private String id;

	@ManyToOne
	@JoinColumn(name = "comment_id", nullable = false)
	private CommentEntity comment;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	public CommentHighsEntity() {
		Instant now = Instant.now();
		this.id = UlidGenerator.generate();
		this.createdAt = now;
		this.updatedAt = now;
	}

	public CommentHighsEntity(CommentEntity comment, UserEntity user) {
		this();
		this.comment = comment;
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CommentEntity getComment() {
		return comment;
	}

	public void setComment(CommentEntity comment) {
		this.comment = comment;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
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

	@Override
	public String toString() {
		return "CommentHighsEntity{" + "id='" + id + '\'' + ", comment=" + comment + ", user=" + user + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		CommentHighsEntity that = (CommentHighsEntity) obj;
		return Objects.equals(this.id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
