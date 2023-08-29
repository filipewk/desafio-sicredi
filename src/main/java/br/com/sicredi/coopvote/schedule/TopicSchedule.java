package br.com.sicredi.coopvote.schedule;

import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.mapper.VotingSessionMapper;
import br.com.sicredi.coopvote.repository.TopicRepository;
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
  private final VotingSessionMapper votingSessionMapper;

  public TopicSchedule(
      VotingSessionRepository votingSessionRepository,
      TopicRepository topicRepository,
      VotingSessionMapper votingSessionMapper) {
    this.votingSessionRepository = votingSessionRepository;
    this.topicRepository = topicRepository;
    this.votingSessionMapper = votingSessionMapper;
  }

  @Transactional
  @Scheduled(fixedRate = 60000)
  public void updateExpiredVotingSessions() {
    List<VotingSession> openSessions = votingSessionRepository.findByIsOpen(true);

    for (VotingSession session : openSessions) {
      if (hasSessionExpired(session)) {
        updateTopicWithVotingResults(session);
      }
    }
  }

  private boolean hasSessionExpired(VotingSession session) {
    var endTime = session.getOpeningDate().plusMinutes(session.getDurationMinutes());
    return LocalDateTime.now().isAfter(endTime);
  }

  private void updateTopicWithVotingResults(VotingSession session) {
    var voteCounts =
        votingSessionMapper.projectionToDto(
            votingSessionRepository.getVotingResult(session.getId()));

    var relatedTopic = session.getTopic();
    relatedTopic.setVoteDifference(voteCounts.getYesVotes() - voteCounts.getNoVotes());
    relatedTopic.setVotingResult(voteCounts.getResult());

    topicRepository.save(relatedTopic);
    markSessionAsClosed(session);
  }

  private void markSessionAsClosed(VotingSession session) {
    session.setIsOpen(false);
    votingSessionRepository.save(session);
  }
}
