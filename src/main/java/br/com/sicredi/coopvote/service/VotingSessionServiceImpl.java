package br.com.sicredi.coopvote.service;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VotingResultDto;
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

  public VotingSessionServiceImpl(
      TopicRepository topicRepository,
      VotingSessionRepository votingRepository,
      VotingSessionMapper votingSessionMapper) {
    this.topicRepository = topicRepository;
    this.votingRepository = votingRepository;
    this.votingSessionMapper = votingSessionMapper;
  }

  @Override
  public VotingSessionDto openSession(Long topicId, Integer durationMinutes) {
    var topic = getTopicById(topicId);
    return createVotingSession(topic, durationMinutes);
  }

  @Override
  public VotingResultDto sessionResult(Long sessionId) {
    var sessionResult = votingRepository.getVotingResult(sessionId);
    return votingSessionMapper.projectionToDto(sessionResult);
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
