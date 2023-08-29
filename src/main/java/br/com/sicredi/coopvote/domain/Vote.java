package br.com.sicredi.coopvote.domain;

import br.com.sicredi.coopvote.enums.VoteEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "VOTE")
public class Vote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ID_VOTING_SESSION", nullable = false)
  private VotingSession votingSession;

  @Column(name = "ID_MEMBER", nullable = false)
  private Long memberId;

  @Column(name = "VOTE_VALUE", nullable = false, length = 3)
  @Enumerated(EnumType.STRING)
  private VoteEnum voteValue;
}
