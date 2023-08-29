package br.com.sicredi.coopvote.repository;

import br.com.sicredi.coopvote.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByVotingSessionIdAndMemberId(Long votingSessionId, Long memberID);
}
