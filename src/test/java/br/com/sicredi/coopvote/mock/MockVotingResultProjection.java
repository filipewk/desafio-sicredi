package br.com.sicredi.coopvote.mock;

import br.com.sicredi.coopvote.dto.projetion.VotingResultProjection;
import br.com.sicredi.coopvote.enums.VotingResultEnum;
import br.com.sicredi.coopvote.enums.VotingSessionStatusEnum;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MockVotingResultProjection {

  private static final Long DEFAULT_ID = 1L;

  public static VotingResultProjection createMockVotingResultProjection() {
    return new VotingResultProjection() {
      @Override
      public Long getTopicId() {
        return DEFAULT_ID;
      }

      @Override
      public String getTopicDescription() {
        return "Topic Test";
      }

      @Override
      public Long getSessionId() {
        return DEFAULT_ID;
      }

      @Override
      public Integer getTotalVotes() {
        return 10;
      }

      @Override
      public Integer getYesVotes() {
        return 9;
      }

      @Override
      public Integer getNoVotes() {
        return 1;
      }

      @Override
      public VotingResultEnum getResult() {
        return VotingResultEnum.YES_WINS;
      }

      @Override
      public VotingSessionStatusEnum getVotingSessionStatus() {
        return null;
      }
    };
  }
}
