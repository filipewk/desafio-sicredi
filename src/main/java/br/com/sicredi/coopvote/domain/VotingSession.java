package br.com.sicredi.coopvote.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "VOTING_SESSION")
public class VotingSession implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ID_TOPIC", nullable = false)
  private Topic topic;

  @CreationTimestamp
  @Column(name = "OPENING_DATE")
  private LocalDateTime openingDate;

  @Column(name = "DURATION_MINUTES")
  private Integer durationMinutes;

  @Column(name = "IS_OPEN")
  private Boolean isOpen = true;
}
