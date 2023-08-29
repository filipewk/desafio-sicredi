package br.com.sicredi.coopvote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoteCountsDto {
  private Integer yesVotes;
  private Integer noVotes;
}
