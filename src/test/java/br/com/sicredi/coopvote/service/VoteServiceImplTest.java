package br.com.sicredi.coopvote.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sicredi.coopvote.domain.Vote;
import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VoteDto;
import br.com.sicredi.coopvote.exception.NotFoundException;
import br.com.sicredi.coopvote.exception.ValidationException;
import br.com.sicredi.coopvote.mapper.VoteMapper;
import br.com.sicredi.coopvote.repository.VoteRepository;
import br.com.sicredi.coopvote.repository.VotingSessionRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VoteServiceImplTest {

  private static final Long DEFAULT_ID = 1L;

  @Mock private VoteRepository voteRepository;

  @Mock private VotingSessionRepository votingSessionRepository;

  @Mock private VoteMapper voteMapper;

  @InjectMocks private VoteServiceImpl voteService;

  private VoteDto testVote;
  private Vote testVoteEntity;
  private VotingSession testSession;

  @BeforeEach
  void setUp() {
    testVote = new VoteDto();
    testVote.setMemberId(DEFAULT_ID);
    testVote.setVotingSessionId(DEFAULT_ID);

    testVoteEntity = new Vote();

    testSession = new VotingSession();
    testSession.setIsOpen(true);
  }

  @Test
  void shouldSuccessfullyCastVote() {
    when(voteRepository.existsByVotingSessionIdAndMemberId(anyLong(), anyLong())).thenReturn(false);
    when(votingSessionRepository.findById(anyLong())).thenReturn(Optional.of(testSession));
    when(voteMapper.toEntity(testVote)).thenReturn(testVoteEntity);
    when(voteRepository.save(testVoteEntity)).thenReturn(testVoteEntity);

    voteService.castVote(testVote);

    verify(voteRepository).save(testVoteEntity);
  }

  @Test
  void shouldThrowValidationExceptionWhenMemberHasAlreadyVoted() {
    when(voteRepository.existsByVotingSessionIdAndMemberId(anyLong(), anyLong())).thenReturn(true);

    assertThrows(ValidationException.class, () -> voteService.castVote(testVote));
  }

  @Test
  void shouldThrowValidationExceptionWhenVotingSessionIsClosed() {
    testSession.setIsOpen(false);

    when(voteRepository.existsByVotingSessionIdAndMemberId(anyLong(), anyLong())).thenReturn(false);
    when(votingSessionRepository.findById(anyLong())).thenReturn(Optional.of(testSession));

    assertThrows(ValidationException.class, () -> voteService.castVote(testVote));
  }

  @Test
  void shouldThrowNotFoundExceptionWhenVotingSessionNotFound() {
    when(voteRepository.existsByVotingSessionIdAndMemberId(anyLong(), anyLong())).thenReturn(false);
    when(votingSessionRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> voteService.castVote(testVote));
  }
}
