package br.com.sicredi.coopvote.domain;

import br.com.sicredi.coopvote.enums.VotingResultEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TOPIC")
public class Topic implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(name = "DESCRIPTION", nullable = false)
  private String description;

  @Column(name = "VOTE_DIFFERENCE", columnDefinition = "INT DEFAULT 0")
  private Integer voteDifference = 0;

  @Column(name = "VOTING_RESULT")
  @Enumerated(EnumType.STRING)
  private VotingResultEnum votingResult = VotingResultEnum.NOT_DECIDED;
}
