package br.com.sicredi.coopvote.service;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VotingSessionDto;
import br.com.sicredi.coopvote.exception.NotFoundException;
import br.com.sicredi.coopvote.mapper.VotingSessionMapper;
import br.com.sicredi.coopvote.repository.TopicRepository;
import br.com.sicredi.coopvote.repository.VotingSessionRepository;
import br.com.sicredi.coopvote.service.interfaces.VotingSessionService;
import org.springframework.stereotype.Service;

@Service
public class VotingSessionServiceImpl implements VotingSessionService {

  private final TopicRepository topicRepository;
  private final VotingSessionRepository votingRepository;
  private final VotingSessionMapper votingSessionMapper;

  private static final int DEFAULT_DURATION_MINUTES = 1;

  public VotingSessionServiceImpl(
      TopicRepository topicRepository,
      VotingSessionRepository votingRepository,
      VotingSessionMapper votingSessionMapper) {
    this.topicRepository = topicRepository;
    this.votingRepository = votingRepository;
    this.votingSessionMapper = votingSessionMapper;
  }

  public VotingSessionDto openSession(Long topicId, Integer durationMinutes) {
    var effectiveDuration = getEffectiveDuration(durationMinutes);
    var topic = getTopicById(topicId);
    return createVotingSession(topic, effectiveDuration);
  }

  private int getEffectiveDuration(Integer durationMinutes) {
    return (durationMinutes == null) ? DEFAULT_DURATION_MINUTES : durationMinutes;
  }

  private Topic getTopicById(Long topicId) {
    return topicRepository
        .findById(topicId)
        .orElseThrow(() -> new NotFoundException("topic.notFound", topicId));
  }

  private VotingSessionDto createVotingSession(Topic topic, int durationMinutes) {
    var session = new VotingSession();
    session.setTopic(topic);
    session.setDurationMinutes(durationMinutes);
    return votingSessionMapper.toDto(votingRepository.save(session));
  }
}
