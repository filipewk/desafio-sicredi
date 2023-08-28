package br.com.sicredi.coopvote.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotingSessionDto {
  private Long id;
  private TopicDto topic;
  private LocalDateTime openingDate;
  private Integer durationMinutes;
}
