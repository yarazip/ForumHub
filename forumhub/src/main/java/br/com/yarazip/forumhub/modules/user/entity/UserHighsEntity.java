package br.com.yarazip.forumhub.modules.user.entity;

import br.com.yarazip.forumhub.config.UlidGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "tb_user_highs")
public class UserHighsEntity {

	@Id
	private String id;

	@ManyToOne
	@JoinColumn(name = "highed_user_id", nullable = false)
	private UserEntity highedUser;

	@ManyToOne
	@JoinColumn(name = "highing_user_id", nullable = false)
	private UserEntity highingUser;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	public UserHighsEntity() {
		Instant now = Instant.now();
		this.id = UlidGenerator.generate();
		this.createdAt = now;
		this.updatedAt = now;
	}

	public UserHighsEntity(UserEntity highedUser, UserEntity highingUser) {
		this();
		this.highedUser = highedUser;
		this.highingUser = highingUser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserEntity getHighedUser() {
		return highedUser;
	}

	public void setHighedUser(UserEntity highedUser) {
		this.highedUser = highedUser;
	}

	public UserEntity getHighingUser() {
		return highingUser;
	}

	public void setHighingUser(UserEntity highingUser) {
		this.highingUser = highingUser;
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
		return "UserHighsEntity{" + "id='" + id + '\'' + ", highedUser=" + highedUser + ", highingUser=" + highingUser
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		UserHighsEntity that = (UserHighsEntity) obj;
		return Objects.equals(this.id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
