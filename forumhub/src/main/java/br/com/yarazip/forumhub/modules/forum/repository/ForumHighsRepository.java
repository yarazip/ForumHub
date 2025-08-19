package br.com.yarazip.forumhub.modules.forum.repository;

import br.com.yarazip.forumhub.modules.forum.entity.ForumHighsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ForumHighsRepository extends JpaRepository<ForumHighsEntity, String> {
	Optional<ForumHighsEntity> findByForum_IdAndUser_Id(UUID forumId, UUID userId);
}
