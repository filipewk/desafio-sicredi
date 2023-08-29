package br.com.sicredi.coopvote.dto;

import br.com.sicredi.coopvote.enums.VoteEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDto {
  private Long votingSessionId;
  private Long memberId;
  private VoteEnum voteValue;
}
