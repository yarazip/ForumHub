package br.com.yarazip.forumhub.modules.user.entity;

import br.com.yarazip.forumhub.modules.comment.entity.CommentEntity;
import br.com.yarazip.forumhub.modules.comment.entity.CommentHighsEntity;
import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;
import br.com.yarazip.forumhub.modules.forum.entity.ForumHighsEntity;
import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import br.com.yarazip.forumhub.modules.topic.entity.TopicHighsEntity;
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
@Table(name = "tb_user")
public class UserEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private UUID id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, length = 50, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(name = "highs_count")
	private Long highsCount = 0L;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private final List<ForumEntity> ownedForums = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "forum_participants", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "forum_id"))
	private final List<ForumEntity> participatingForums = new ArrayList<>();

	@OneToMany(mappedBy = "highingUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private final List<UserHighsEntity> highsInUsers = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private final List<ForumHighsEntity> highsInForums = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private final List<TopicHighsEntity> highsInTopics = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private final List<CommentHighsEntity> highsInComments = new ArrayList<>();

	@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private final List<TopicEntity> topics = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private final List<CommentEntity> comments = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	public UserEntity() {
		Instant now = Instant.now();

		this.id = UUID.randomUUID();
		this.createdAt = now;
		this.updatedAt = now;
	}

	public UserEntity(String name, String username, String email, String password) {
		this();
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public List<ForumEntity> getOwnedForums() {
		return ownedForums;
	}

	public void addOwnedForum(ForumEntity forum) {
		if (!ownedForums.contains(forum)) {
			ownedForums.add(forum);
			if (forum.getOwner() != this) {
				forum.addOwner(this);
			}
		}
	}

	public void removeOwnedForum(ForumEntity forum) {
		if (ownedForums.contains(forum)) {
			ownedForums.remove(forum);
			if (forum.getOwner() == this) {
				forum.addOwner(null);
			}
		}
	}

	public boolean participatingInForum(ForumEntity forum) {
		return participatingForums.contains(forum);
	}

	public List<ForumEntity> getParticipatingForums() {
		return participatingForums;
	}

	public void addParticipatingForum(ForumEntity forum) {
		if (!participatingForums.contains(forum)) {
			participatingForums.add(forum);
			forum.addParticipant(this);
		}
	}

	public void removeParticipatingForum(ForumEntity forum) {
		if (!participatingForums.contains(forum)) {
			participatingForums.remove(forum);
			forum.removeParticipant(this);
		}
	}

	public List<UserHighsEntity> getHighsInUsers() {
		return highsInUsers;
	}

	public List<ForumHighsEntity> getHighsInForums() {
		return highsInForums;
	}

	public List<TopicHighsEntity> getHighsInTopics() {
		return highsInTopics;
	}

	public List<CommentHighsEntity> getHighsInComments() {
		return highsInComments;
	}

	public List<TopicEntity> getTopics() {
		return topics;
	}

	public void addTopic(TopicEntity topic) {
		if (!topics.contains(topic)) {
			topics.add(topic);
			if (topic.getCreator() != this) {
				topic.setCreator(this);
			}
		}
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
		return "UserEntity{" + "id=" + id + ", name='" + name + '\'' + ", username='" + username + '\'' + ", email='"
				+ email + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		UserEntity that = (UserEntity) obj;
		return Objects.equals(this.id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
