package br.com.sicredi.coopvote.service;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VotingResultDto;
import br.com.sicredi.coopvote.dto.VotingSessionDto;
import br.com.sicredi.coopvote.enums.VotingResultEnum;
import br.com.sicredi.coopvote.exception.NotFoundException;
import br.com.sicredi.coopvote.exception.ValidationException;
import br.com.sicredi.coopvote.mapper.VotingSessionMapper;
import br.com.sicredi.coopvote.repository.TopicRepository;
import br.com.sicredi.coopvote.repository.VotingSessionRepository;
import br.com.sicredi.coopvote.service.interfaces.VotingSessionService;
import org.springframework.stereotype.Service;

@Service
public class VotingSessionServiceImpl implements VotingSessionService {

  private final TopicRepository topicRepository;
  private final VotingSessionRepository votingSessionRepository;
  private final VotingSessionMapper votingSessionMapper;

  public VotingSessionServiceImpl(
      TopicRepository topicRepository,
      VotingSessionRepository votingSessionRepository,
      VotingSessionMapper votingSessionMapper) {
    this.topicRepository = topicRepository;
    this.votingSessionRepository = votingSessionRepository;
    this.votingSessionMapper = votingSessionMapper;
  }

  @Override
  public VotingSessionDto openSession(Long topicId, Integer durationMinutes) {
    Topic topic = findTopicOrThrow(topicId);
    validateTopicIsEligibleForNewSession(topic);
    return createAndSaveVotingSession(topic, durationMinutes);
  }

  @Override
  public VotingResultDto sessionResult(Long sessionId) {
    ensureSessionExists(sessionId);
    var sessionResult = votingSessionRepository.getVotingResult(sessionId);
    return votingSessionMapper.projectionToDto(sessionResult);
  }

  private Topic findTopicOrThrow(Long topicId) {
    return topicRepository
        .findById(topicId)
        .orElseThrow(() -> new NotFoundException("topic.notFound", topicId));
  }

  private void validateTopicIsEligibleForNewSession(Topic topic) {
    validateNoActiveSessionForTopic(topic);
    validateTopicHasNoDecision(topic);
  }

  private void validateNoActiveSessionForTopic(Topic topic) {
    votingSessionRepository
        .findByTopicAndIsOpen(topic, true)
        .ifPresent(
            s -> {
              throw new ValidationException("topic.session.active.exists");
            });
  }

  private void validateTopicHasNoDecision(Topic topic) {
    if (!VotingResultEnum.NOT_DECIDED.equals(topic.getVotingResult())) {
      throw new ValidationException("topic.result.exists");
    }
  }

  private void ensureSessionExists(Long sessionId) {
    if (votingSessionRepository.findById(sessionId).isEmpty()) {
      throw new NotFoundException("session.notFound", sessionId);
    }
  }

  private VotingSessionDto createAndSaveVotingSession(Topic topic, int durationMinutes) {
    var session = new VotingSession();
    session.setTopic(topic);
    session.setDurationMinutes(durationMinutes);
    return votingSessionMapper.toDto(votingSessionRepository.save(session));
  }
}
