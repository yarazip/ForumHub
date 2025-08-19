package br.com.yarazip.forumhub.modules.topic.repository;


import br.com.yarazip.forumhub.modules.topic.entity.TopicHighsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface TopicHighsRepository extends JpaRepository<TopicHighsEntity, String> {
    Optional<TopicHighsEntity> findByTopic_IdAndUser_Id(UUID topicId, UUID userId);
}
