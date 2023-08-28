package br.com.sicredi.coopvote.controller.interfaces;

import br.com.sicredi.coopvote.dto.VotingSessionDto;
import br.com.sicredi.coopvote.record.ApiResult;
import org.springframework.http.ResponseEntity;

public interface VotingSessionController {

  ResponseEntity<ApiResult<VotingSessionDto>> createTopic(Long topicId, Integer durationMinutes);
}
