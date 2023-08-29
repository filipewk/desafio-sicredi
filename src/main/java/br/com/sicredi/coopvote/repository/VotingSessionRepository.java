package br.com.sicredi.coopvote.repository;

import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.projetion.VotingResultProjection;
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
                ID_VOTING_SESSION AS sessionId,
                SUM(CASE WHEN VOTE_VALUE = 'YES' THEN 1 ELSE 0 END) AS yesVotes,
                SUM(CASE WHEN VOTE_VALUE = 'NO' THEN 1 ELSE 0 END) AS noVotes,
                COUNT(*) AS totalVotes
            FROM VOTE
            WHERE ID_VOTING_SESSION = :sessionId
            GROUP BY ID_VOTING_SESSION
        )
        SELECT
            sessionId,
            totalVotes,
            yesVotes,
            noVotes,
            CASE
                WHEN yesVotes > noVotes THEN 'YES VOTES WINS'
                WHEN yesVotes < noVotes THEN 'NO VOTES WINS'
                ELSE 'TIED'
            END AS result
        FROM voteCounts
        WHERE sessionId = :sessionId
        """,
      nativeQuery = true)
  VotingResultProjection getVotingResult(@Param("sessionId") Long sessionId);
}
