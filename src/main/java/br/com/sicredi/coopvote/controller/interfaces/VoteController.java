package br.com.sicredi.coopvote.controller.interfaces;

import br.com.sicredi.coopvote.dto.VoteDto;
import br.com.sicredi.coopvote.record.ApiResult;
import org.springframework.http.ResponseEntity;

public interface VoteController {
  ResponseEntity<ApiResult<VoteDto>> castVote(VoteDto vote);
}
