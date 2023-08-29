package br.com.sicredi.coopvote.service;

import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VoteDto;
import br.com.sicredi.coopvote.exception.NotFoundException;
import br.com.sicredi.coopvote.exception.ValidationException;
import br.com.sicredi.coopvote.mapper.VoteMapper;
import br.com.sicredi.coopvote.repository.VoteRepository;
import br.com.sicredi.coopvote.repository.VotingSessionRepository;
import br.com.sicredi.coopvote.service.interfaces.VoteService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

  private final VoteRepository voteRepository;
  private final VotingSessionRepository votingSessionRepository;
  private final VoteMapper voteMapper;

  public VoteServiceImpl(
      VoteRepository voteRepository,
      VotingSessionRepository votingSessionRepository,
      VoteMapper voteMapper) {
    this.voteRepository = voteRepository;
    this.votingSessionRepository = votingSessionRepository;
    this.voteMapper = voteMapper;
  }

  @Override
  @Transactional
  public VoteDto castVote(VoteDto vote) {
    validateVoteCanBeCast(vote);
    voteRepository.save(voteMapper.toEntity(vote));
    return vote;
  }

  private void validateVoteCanBeCast(VoteDto vote) {
    ensureMemberHasNotVotedInSession(vote);
    ensureVotingSessionIsOpen(vote.getVotingSessionId());
  }

  private void ensureMemberHasNotVotedInSession(VoteDto vote) {
    if (voteRepository.existsByVotingSessionIdAndMemberId(
        vote.getVotingSessionId(), vote.getMemberId())) {
      throw new ValidationException("member.session.alreadyVoted");
    }
  }

  private void ensureVotingSessionIsOpen(Long votingSessionId) {
    VotingSession session = getVotingSession(votingSessionId);

    if (!session.getIsOpen()) {
      throw new ValidationException("voting.session.closed");
    }
  }

  private VotingSession getVotingSession(Long votingSessionId) {
    return votingSessionRepository
        .findById(votingSessionId)
        .orElseThrow(() -> new NotFoundException("voting.session.notFound", votingSessionId));
  }
}
