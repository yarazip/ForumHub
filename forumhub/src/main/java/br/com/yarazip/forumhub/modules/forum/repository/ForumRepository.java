package br.com.yarazip.forumhub.modules.forum.repository;

import br.com.yarazip.forumhub.modules.forum.entity.ForumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ForumRepository extends JpaRepository<ForumEntity, UUID> {

	Boolean existsByName(String name);
}
