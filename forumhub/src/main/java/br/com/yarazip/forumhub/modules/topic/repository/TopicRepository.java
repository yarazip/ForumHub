package br.com.yarazip.forumhub.modules.topic.repository;

import br.com.yarazip.forumhub.modules.topic.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, UUID> {
}
