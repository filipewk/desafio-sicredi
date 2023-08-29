package br.com.sicredi.coopvote.schedule;

import static br.com.sicredi.coopvote.util.Constants.ONE_MINUTE_IN_MILLISECONDS;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VoteCountsDto;
import br.com.sicredi.coopvote.enums.VoteEnum;
import br.com.sicredi.coopvote.enums.VotingResultEnum;
import br.com.sicredi.coopvote.repository.TopicRepository;
import br.com.sicredi.coopvote.repository.VoteRepository;
import br.com.sicredi.coopvote.repository.VotingSessionRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TopicSchedule {

  private final VotingSessionRepository votingSessionRepository;
  private final TopicRepository topicRepository;
  private final VoteRepository voteRepository;

  public TopicSchedule(
      VotingSessionRepository votingSessionRepository,
      TopicRepository topicRepository,
      VoteRepository voteRepository) {
    this.votingSessionRepository = votingSessionRepository;
    this.topicRepository = topicRepository;
    this.voteRepository = voteRepository;
  }

  @Transactional
  @Scheduled(fixedRate = ONE_MINUTE_IN_MILLISECONDS)
  public void updateExpiredVotingSessions() {
    List<VotingSession> openSessions = getOpenSessions();

    for (VotingSession session : openSessions) {
      if (hasSessionExpired(session)) {
        updateTopicWithVotingResults(session);
      }
    }
  }

  private List<VotingSession> getOpenSessions() {
    return votingSessionRepository.findByIsOpen(true);
  }

  private boolean hasSessionExpired(VotingSession session) {
    return LocalDateTime.now().isAfter(calculateSessionEndTime(session));
  }

  private LocalDateTime calculateSessionEndTime(VotingSession session) {
    return session.getOpeningDate().plusMinutes(session.getDurationMinutes());
  }

  private void updateTopicWithVotingResults(VotingSession session) {
    var voteCounts = getVoteCounts(session);
    var relatedTopic = updateRelatedTopicWithResults(session.getTopic(), voteCounts);
    topicRepository.save(relatedTopic);
    markSessionAsClosed(session);
  }

  private VoteCountsDto getVoteCounts(VotingSession session) {
    var yesVotes = voteRepository.countByVotingSessionAndVoteValue(session, VoteEnum.YES);
    var noVotes = voteRepository.countByVotingSessionAndVoteValue(session, VoteEnum.NO);
    return new VoteCountsDto(yesVotes, noVotes);
  }

  private Topic updateRelatedTopicWithResults(Topic topic, VoteCountsDto voteCounts) {
    var voteDifference = voteCounts.getYesVotes() - voteCounts.getNoVotes();
    topic.setVoteDifference(voteDifference);
    topic.setVotingResult(determineVotingResult(voteCounts));
    return topic;
  }

  private VotingResultEnum determineVotingResult(VoteCountsDto voteCounts) {
    if (voteCounts.getYesVotes() > voteCounts.getNoVotes()) {
      return VotingResultEnum.YES_WINS;
    } else if (voteCounts.getYesVotes() < voteCounts.getNoVotes()) {
      return VotingResultEnum.NO_WINS;
    } else {
      return VotingResultEnum.TIED;
    }
  }

  private void markSessionAsClosed(VotingSession session) {
    session.setIsOpen(false);
    votingSessionRepository.save(session);
  }
}
