package br.com.yarazip.forumhub.modules.topic.entity;

import br.com.yarazip.forumhub.modules.comment.entity.CommentEntity;
import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_topic")
public class TopicEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private UUID id;

	@Column(nullable = false, length = 50)
	private String title;

	@Column(nullable = false, length = 500)
	private String content;

	@Column(name = "highs_count")
	private Long highsCount = 0L;

	@Column(name = "comments_count")
	private Long commentsCount = 0L;

	@ManyToOne
	@JoinColumn(name = "forum_id", nullable = false)
	private ForumEntity forum;

	@ManyToOne
	@JoinColumn(name = "creator_id", nullable = false)
	private UserEntity creator;

	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<TopicHighsEntity> highs = new ArrayList<>();

	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<CommentEntity> comments = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	public TopicEntity() {
		Instant now = Instant.now();

		this.id = UUID.randomUUID();
		this.createdAt = now;
		this.updatedAt = now;
	}

	public TopicEntity(String title, String content) {
		this();
		this.title = title;
		this.content = content;
	}

	public TopicEntity(String title, String content, UserEntity creator, ForumEntity forum) {
		this(title, content);
		this.forum = forum;
		this.creator = creator;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Long getCommentsCount() {
		return commentsCount;
	}

	public void incrementComments() {
		this.commentsCount++;
	}

	public void decrementComments() {
		this.commentsCount--;
	}

	public ForumEntity getForum() {
		return forum;
	}

	public void setForum(ForumEntity forum) {
		if (forum != null && !forum.equals(this.forum)) {
			this.forum = forum;
			if (!forum.getTopics().contains(this)) {
				forum.addTopic(this);
			}
		}
	}

	public UserEntity getCreator() {
		return creator;
	}

	public void setCreator(UserEntity creator) {
		if (creator != null && !creator.equals(this.creator)) {
			this.creator = creator;
			if (!creator.getTopics().contains(this)) {
				creator.addTopic(this);
			}
		}
	}

	public List<TopicHighsEntity> getHighs() {
		return highs;
	}

	public List<CommentEntity> getComments() {
		return comments;
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
		return "TopicEntity{" + "id=" + id + ", title='" + title + '\'' + ", content='" + content + '\''
				+ ", highsCount=" + highsCount + ", commentsCount=" + commentsCount + ", forum=" + forum + ", creator="
				+ creator + ", highs=" + highs + ", comments=" + comments + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof TopicEntity that))
			return false;

		return Objects.equals(this.id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
