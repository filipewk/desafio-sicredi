package br.com.sicredi.coopvote.mapper;

import br.com.sicredi.coopvote.domain.Vote;
import br.com.sicredi.coopvote.dto.VoteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {

  @Mapping(source = "votingSessionId", target = "votingSession.id")
  @Mapping(target = "id", ignore = true)
  Vote toEntity(VoteDto dto);

  @Mapping(source = "votingSession.id", target = "votingSessionId")
  VoteDto toDto(Vote entity);
}
