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
                    V.ID_VOTING_SESSION AS sessionIdentifier,
                    COALESCE(SUM(CASE WHEN VOTE_VALUE = 'YES' THEN 1 ELSE 0 END), 0) AS yesVotes,
                    COALESCE(SUM(CASE WHEN VOTE_VALUE = 'NO' THEN 1 ELSE 0 END), 0) AS noVotes,
                    COALESCE(SUM(CASE WHEN VOTE_VALUE IS NOT NULL THEN 1 ELSE 0 END), 0) AS totalVotes
                FROM (SELECT :sessionId AS ID_VOTING_SESSION) tempSession
                LEFT JOIN VOTE V ON tempSession.ID_VOTING_SESSION = V.ID_VOTING_SESSION
                GROUP BY V.ID_VOTING_SESSION
            ),
            sessionStatus AS (
                SELECT
                    ID,
                    CASE
                        WHEN IS_OPEN THEN 'VOTING_OPEN'
                        ELSE 'VOTING_CLOSED'
                    END AS votingSessionStatus
                FROM VOTING_SESSION
                WHERE ID = :sessionId
            )
            SELECT
                T.ID AS topicId,
                T.DESCRIPTION AS topicDescription,
                COALESCE(vc.sessionIdentifier, :sessionId) AS sessionId,
                COALESCE(vc.totalVotes, 0) AS totalVotes,
                COALESCE(vc.yesVotes, 0) AS yesVotes,
                COALESCE(vc.noVotes, 0) AS noVotes,
                CASE
                    WHEN sessionStatus.votingSessionStatus = 'VOTING_OPEN' THEN 'NOT_DECIDED'
                    WHEN vc.yesVotes > vc.noVotes THEN 'YES_WINS'
                    WHEN vc.yesVotes < vc.noVotes THEN 'NO_WINS'
                    ELSE 'TIED'
                END AS result,
                sessionStatus.votingSessionStatus
            FROM VOTING_SESSION VS\s
            JOIN TOPIC T ON T.ID = VS.ID_TOPIC
            LEFT JOIN voteCounts vc ON VS.ID = vc.sessionIdentifier
            INNER JOIN sessionStatus ON sessionStatus.ID = :sessionId
            WHERE VS.ID = :sessionId
        """,
      nativeQuery = true)
  VotingResultProjection getVotingResult(@Param("sessionId") Long sessionId);

  List<VotingSession> findByIsOpen(boolean isOpen);

  Optional<VotingSession> findByTopicAndIsOpen(Topic topic, Boolean isOpen);
}
