package br.com.sicredi.coopvote.repository;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.projetion.VotingResultProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {

  @Query(
      value =
          """
            WITH voteCounts AS (
                SELECT
                    v.ID_VOTING_SESSION AS sessionId,
                    COALESCE(SUM(CASE WHEN VOTE_VALUE = 'YES' THEN 1 ELSE 0 END), 0) AS yesVotes,
                    COALESCE(SUM(CASE WHEN VOTE_VALUE = 'NO' THEN 1 ELSE 0 END), 0) AS noVotes,
                    COALESCE(SUM(CASE WHEN VOTE_VALUE IS NOT NULL THEN 1 ELSE 0 END), 0) AS totalVotes
                FROM (SELECT :sessionId AS ID_VOTING_SESSION) s -- Virtual table with sessionId
                LEFT JOIN VOTE v ON s.ID_VOTING_SESSION = v.ID_VOTING_SESSION
                GROUP BY v.ID_VOTING_SESSION
            )
            SELECT
                COALESCE(sessionId, :sessionId) AS sessionId,
                totalVotes,
                yesVotes,
                noVotes,
                CASE
                    WHEN yesVotes > noVotes THEN 'YES_WINS'
                    WHEN yesVotes < noVotes THEN 'NO_WINS'
                    ELSE 'TIED'
                END AS result
            FROM voteCounts
            WHERE COALESCE(sessionId, :sessionId) = :sessionId
        """,
      nativeQuery = true)
  VotingResultProjection getVotingResult(@Param("sessionId") Long sessionId);

  List<VotingSession> findByIsOpen(boolean isOpen);

  Optional<VotingSession> findByTopicAndIsOpen(Topic topic, Boolean isOpen);
}
