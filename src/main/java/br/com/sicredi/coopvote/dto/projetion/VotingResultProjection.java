package br.com.sicredi.coopvote.dto.projetion;

public interface VotingResultProjection {
  Long getSessionId();

  Integer getTotalVotes();

  Integer getYesVotes();

  Integer getNoVotes();

  String getResult();
}
