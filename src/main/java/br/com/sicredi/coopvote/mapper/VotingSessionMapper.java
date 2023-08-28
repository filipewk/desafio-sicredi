package br.com.sicredi.coopvote.mapper;

import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VotingSessionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VotingSessionMapper {

  VotingSessionDto toDto(VotingSession entity);
}
