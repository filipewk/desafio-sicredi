package br.com.sicredi.coopvote.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotingResultDto {
  private Long sessionId;
  private Integer totalVotes;
  private Integer yesVotes;
  private Integer noVotes;
  private String result;
}
