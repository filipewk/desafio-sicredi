package br.com.sicredi.coopvote.repository;

import br.com.sicredi.coopvote.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {}
