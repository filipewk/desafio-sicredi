package br.com.sicredi.coopvote.service.interfaces;

import br.com.sicredi.coopvote.dto.VotingResultDto;
import br.com.sicredi.coopvote.dto.VotingSessionDto;

public interface VotingSessionService {
  VotingSessionDto openSession(Long topicId, Integer durationMinutes);

  VotingResultDto sessionResult(Long sessionId);
}
