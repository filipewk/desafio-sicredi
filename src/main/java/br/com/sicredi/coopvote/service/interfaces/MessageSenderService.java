package br.com.sicredi.coopvote.service.interfaces;

import br.com.sicredi.coopvote.dto.VotingResultDto;

public interface MessageSenderService {
  void sendVoteUpdateMessage(VotingResultDto message);
}
