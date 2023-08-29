package br.com.sicredi.coopvote.dto.projetion;

import br.com.sicredi.coopvote.enums.VotingResultEnum;
import br.com.sicredi.coopvote.enums.VotingSessionStatusEnum;

public interface VotingResultProjection {
  Long getTopicId();

  String getTopicDescription();

  Long getSessionId();

  Integer getTotalVotes();

  Integer getYesVotes();

  Integer getNoVotes();

  VotingResultEnum getResult();

  VotingSessionStatusEnum getVotingSessionStatus();
}
