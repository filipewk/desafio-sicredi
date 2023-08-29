package br.com.sicredi.coopvote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VotingResultDto;
import br.com.sicredi.coopvote.dto.VotingSessionDto;
import br.com.sicredi.coopvote.enums.VotingResultEnum;
import br.com.sicredi.coopvote.exception.NotFoundException;
import br.com.sicredi.coopvote.exception.ValidationException;
import br.com.sicredi.coopvote.mapper.VotingSessionMapper;
import br.com.sicredi.coopvote.mock.MockVotingResultProjection;
import br.com.sicredi.coopvote.repository.TopicRepository;
import br.com.sicredi.coopvote.repository.VotingSessionRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VotingSessionServiceImplTest {

  private static final Long DEFAULT_ID = 1L;
  private static final int DEFAULT_DURATION = 1;

  @Mock private TopicRepository topicRepository;

  @Mock private VotingSessionRepository votingSessionRepository;

  @Mock private VotingSessionMapper votingSessionMapper;

  @InjectMocks private VotingSessionServiceImpl votingSessionService;

  @Test
  void shouldThrowNotFoundExceptionWhenTopicNotFound() {
    when(topicRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> votingSessionService.openSession(DEFAULT_ID, DEFAULT_DURATION));
  }

  @Test
  void shouldThrowValidationExceptionWhenActiveSessionExists() {
    when(topicRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(new Topic()));
    when(votingSessionRepository.findByTopicAndIsOpen(any(Topic.class), eq(true)))
        .thenReturn(Optional.of(new VotingSession()));

    assertThrows(
        ValidationException.class,
        () -> votingSessionService.openSession(DEFAULT_ID, DEFAULT_DURATION));
  }

  @Test
  void shouldThrowValidationExceptionWhenTopicHasDecision() {
    var topicWithDecision = new Topic();
    topicWithDecision.setVotingResult(VotingResultEnum.YES_WINS);
    when(topicRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(topicWithDecision));

    assertThrows(
        ValidationException.class,
        () -> votingSessionService.openSession(DEFAULT_ID, DEFAULT_DURATION));
  }

  @Test
  void shouldOpenSessionSuccessfully() {
    when(topicRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(new Topic()));
    when(votingSessionMapper.toDto(any())).thenReturn(new VotingSessionDto());
    when(votingSessionRepository.save(any())).thenReturn(new VotingSession());

    assertNotNull(votingSessionService.openSession(DEFAULT_ID, DEFAULT_DURATION));
  }

  @Test
  void shouldThrowNotFoundExceptionWhenSessionNotFound() {
    when(votingSessionRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> votingSessionService.sessionResult(DEFAULT_ID));
  }

  @Test
  void shouldReturnSessionResultSuccessfully() {
    var projectedResult = MockVotingResultProjection.createMockVotingResultProjection();
    when(votingSessionRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(new VotingSession()));
    when(votingSessionRepository.getVotingResult(DEFAULT_ID)).thenReturn(projectedResult);
    when(votingSessionMapper.projectionToDto(projectedResult)).thenReturn(new VotingResultDto());

    var actualDto = votingSessionService.sessionResult(DEFAULT_ID);
    var expectedDto = new VotingResultDto();
    assertEquals(expectedDto.getSessionId(), actualDto.getSessionId());
  }
}
