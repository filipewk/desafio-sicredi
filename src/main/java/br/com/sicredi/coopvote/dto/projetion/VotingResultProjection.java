package br.com.sicredi.coopvote.dto.projetion;

import br.com.sicredi.coopvote.enums.VotingResultEnum;

public interface VotingResultProjection {
  Long getSessionId();

  Integer getTotalVotes();

  Integer getYesVotes();

  Integer getNoVotes();

  VotingResultEnum getResult();
}
