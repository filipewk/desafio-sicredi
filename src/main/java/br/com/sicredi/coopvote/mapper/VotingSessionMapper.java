package br.com.sicredi.coopvote.mapper;

import br.com.sicredi.coopvote.domain.VotingSession;
import br.com.sicredi.coopvote.dto.VotingResultDto;
import br.com.sicredi.coopvote.dto.VotingSessionDto;
import br.com.sicredi.coopvote.dto.projetion.VotingResultProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VotingSessionMapper {

  VotingSessionDto toDto(VotingSession entity);

  VotingResultDto projectionToDto(VotingResultProjection projection);
}
