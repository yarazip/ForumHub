package br.com.yarazip.forumhub.modules.forum.entity;

import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.user.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
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
@Table(name = "tb_forum")
@Transactional
public class ForumEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private UUID id;

	@Column(nullable = false, length = 50, unique = true)
	private String name;

	@Column(nullable = false, length = 50)
	private String description;

	@Column(name = "highs_count", nullable = false)
	private Long highsCount = 0L;

	@Column(name = "topics_count", nullable = false)
	private Long topicsCount = 0L;

	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private UserEntity owner;

	@OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<ForumHighsEntity> highs = new ArrayList<>();

	@ManyToMany(mappedBy = "participatingForums", fetch = FetchType.EAGER)
	private final List<UserEntity> participants = new ArrayList<>();

	@OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private final List<TopicEntity> topics = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	public ForumEntity() {
		Instant now = Instant.now();

		this.id = UUID.randomUUID();
		this.createdAt = now;
		this.updatedAt = now;
	}

	public ForumEntity(String name, String description, UserEntity owner) {
		this();
		this.name = name;
		this.description = description;
		this.owner = owner;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Long getTopicsCount() {
		return topicsCount;
	}

	public void incrementTopicsCount() {
		this.topicsCount++;
	}

	public void decrementTopicsCount() {
		this.topicsCount--;
	}

	public UserEntity getOwner() {
		return owner;
	}

	public void addOwner(UserEntity owner) {
		if (owner != null && !owner.equals(this.owner)) {
			this.owner = owner;
			if (!owner.getOwnedForums().contains(this)) {
				owner.addOwnedForum(this);
			}
		}
	}

	public void removeOwner() {
		if (owner != null) {
			UserEntity oldOwner = owner;
			owner = null;
			oldOwner.removeOwnedForum(this);
		}
	}

	public List<ForumHighsEntity> getHighs() {
		return highs;
	}

	public List<UserEntity> getParticipants() {
		return participants;
	}

	public void addParticipant(UserEntity user) {
		if (!participants.contains(user)) {
			participants.add(user);
			user.addParticipatingForum(this);
		}
	}

	public void removeParticipant(UserEntity user) {
		if (participants.contains(user)) {
			participants.remove(user);
			user.removeParticipatingForum(this);
		}
	}

	public List<TopicEntity> getTopics() {
		return topics;
	}

	public void addTopic(TopicEntity topic) {
		if (topic != null && !topics.contains(topic)) {
			topics.add(topic);
			if (!this.equals(topic.getForum())) {
				topic.setForum(this);
			}
		}
	}

	public void removeTopic(TopicEntity topic) {
		if (topic != null && topics.contains(topic)) {
			topics.remove(topic);
			if (this.equals(topic.getForum())) {
				topic.setForum(null);
			}
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

	@Override
	public String toString() {
		return "ForumEntity{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\''
				+ ", highsCount=" + highsCount + ", topicsCount=" + topicsCount + ", owner=" + owner + ", participants="
				+ participants + ", topics=" + topics + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		ForumEntity that = (ForumEntity) obj;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
