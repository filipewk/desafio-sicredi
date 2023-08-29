package br.com.sicredi.coopvote.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteEnum {
  YES("SIM"),
  NO("NÃO");

  private final String voteDescription;
}
