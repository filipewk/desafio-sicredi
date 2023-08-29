package br.com.sicredi.coopvote.schedule;

import static br.com.sicredi.coopvote.util.Constants.ONE_MINUTE_IN_MILLISECONDS;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VoteCountsDto;
import br.com.sicredi.coopvote.dto.VotingResultDto;
import br.com.sicredi.coopvote.enums.VoteEnum;
import br.com.sicredi.coopvote.enums.VotingResultEnum;
import br.com.sicredi.coopvote.enums.VotingSessionStatusEnum;
import br.com.sicredi.coopvote.repository.TopicRepository;
import br.com.sicredi.coopvote.repository.VoteRepository;
import br.com.sicredi.coopvote.repository.VotingSessionRepository;
import br.com.sicredi.coopvote.service.interfaces.MessageSenderService;
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
  private final MessageSenderService messageSenderService;

  public TopicSchedule(
      VotingSessionRepository votingSessionRepository,
      TopicRepository topicRepository,
      VoteRepository voteRepository,
      MessageSenderService messageSenderService) {
    this.votingSessionRepository = votingSessionRepository;
    this.topicRepository = topicRepository;
    this.voteRepository = voteRepository;
    this.messageSenderService = messageSenderService;
  }

  @Transactional
  @Scheduled(fixedRate = ONE_MINUTE_IN_MILLISECONDS)
  public void updateExpiredVotingSessions() {
    var openSessions = getOpenSessions();
    for (VotingSession session : openSessions) {
      if (hasSessionExpired(session)) {
        processExpiredSession(session);
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

  private void processExpiredSession(VotingSession session) {
    var voteCounts = getVoteCounts(session);
    var relatedTopic = updateRelatedTopicWithResults(session.getTopic(), voteCounts);
    topicRepository.save(relatedTopic);
    markSessionAsClosed(session);
    sendVotingResultMessage(session, relatedTopic, voteCounts);
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

  private void sendVotingResultMessage(
      VotingSession session, Topic topic, VoteCountsDto voteCounts) {
    var result = createVotingResultDto(session, topic, voteCounts);
    messageSenderService.sendVoteUpdateMessage(result);
  }

  private VotingResultDto createVotingResultDto(
      VotingSession session, Topic topic, VoteCountsDto voteCounts) {

    return VotingResultDto.builder()
        .topicId(topic.getId())
        .topicDescription(topic.getDescription())
        .sessionId(session.getId())
        .totalVotes(calculateTotalVotes(voteCounts))
        .yesVotes(voteCounts.getYesVotes())
        .noVotes(voteCounts.getNoVotes())
        .result(determineVotingResult(voteCounts))
        .votingSessionStatus(determineVotingSessionStatus(session))
        .build();
  }

  private int calculateTotalVotes(VoteCountsDto voteCounts) {
    return voteCounts.getYesVotes() + voteCounts.getNoVotes();
  }

  private VotingSessionStatusEnum determineVotingSessionStatus(VotingSession session) {
    return session.getIsOpen()
        ? VotingSessionStatusEnum.VOTING_OPEN
        : VotingSessionStatusEnum.VOTING_CLOSED;
  }
}
