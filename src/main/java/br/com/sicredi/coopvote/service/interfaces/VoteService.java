package br.com.sicredi.coopvote.service.interfaces;

import br.com.sicredi.coopvote.dto.VoteDto;

public interface VoteService {
  VoteDto castVote(VoteDto vote);
}
