package br.com.sicredi.coopvote.schedule;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VotingResultDto;
import br.com.sicredi.coopvote.enums.VoteEnum;
import br.com.sicredi.coopvote.repository.TopicRepository;
import br.com.sicredi.coopvote.repository.VoteRepository;
import br.com.sicredi.coopvote.repository.VotingSessionRepository;
import br.com.sicredi.coopvote.service.interfaces.MessageSenderService;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TopicScheduleTest {

  @Mock private VotingSessionRepository votingSessionRepository;
  @Mock private TopicRepository topicRepository;
  @Mock private VoteRepository voteRepository;
  @Mock private MessageSenderService messageSenderService;

  @InjectMocks private TopicSchedule topicSchedule;

  @Test
  void shouldProcessSessions() {
    var expiredSession = createExpiredSession();

    when(votingSessionRepository.findByIsOpen(true))
        .thenReturn(Collections.singletonList(expiredSession));
    when(voteRepository.countByVotingSessionAndVoteValue(expiredSession, VoteEnum.YES))
        .thenReturn(10);
    when(voteRepository.countByVotingSessionAndVoteValue(expiredSession, VoteEnum.NO))
        .thenReturn(5);

    topicSchedule.updateExpiredVotingSessions();

    verify(votingSessionRepository, times(1)).save(any(VotingSession.class));
    verify(topicRepository, times(1)).save(any(Topic.class));
    verify(messageSenderService, times(1)).sendVoteUpdateMessage(any(VotingResultDto.class));
  }

  @Test
  void shouldNotProcessSessionsThatAreNotExpired() {
    VotingSession notExpiredSession = createNotExpiredSession();

    when(votingSessionRepository.findByIsOpen(true))
        .thenReturn(Collections.singletonList(notExpiredSession));

    topicSchedule.updateExpiredVotingSessions();

    verify(topicRepository, never()).save(any());
    verify(votingSessionRepository, never()).save(any());
    verify(messageSenderService, never()).sendVoteUpdateMessage(any());
  }

  private VotingSession createExpiredSession() {
    var session = new VotingSession();
    session.setOpeningDate(LocalDateTime.now().minusHours(2));
    session.setDurationMinutes(60);
    session.setIsOpen(true);
    session.setTopic(new Topic());
    return session;
  }

  private VotingSession createNotExpiredSession() {
    var session = new VotingSession();
    session.setOpeningDate(LocalDateTime.now().minusMinutes(30));
    session.setDurationMinutes(60);
    session.setIsOpen(true);
    return session;
  }
}
