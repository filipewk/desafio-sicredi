package br.com.sicredi.coopvote.dto;

import br.com.sicredi.coopvote.enums.VotingResultEnum;
import br.com.sicredi.coopvote.enums.VotingSessionStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotingResultDto {
  private Long topicId;
  private String topicDescription;
  private Long sessionId;
  private Integer totalVotes;
  private Integer yesVotes;
  private Integer noVotes;
  private VotingResultEnum result;
  private VotingSessionStatusEnum votingSessionStatus;
}
