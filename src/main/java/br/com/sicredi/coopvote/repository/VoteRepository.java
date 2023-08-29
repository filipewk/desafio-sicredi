package br.com.sicredi.coopvote.repository;

import br.com.sicredi.coopvote.domain.Vote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
  boolean existsByVotingSessionIdAndMemberId(Long votingSessionId, Long memberID);

  @Query(value = "SELECT V FROM Vote V WHERE V.votingSession.id = :sessionId")
  List<Vote> findBySessionId(Long sessionId);
}
