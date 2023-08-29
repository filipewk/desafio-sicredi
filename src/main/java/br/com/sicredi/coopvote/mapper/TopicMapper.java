package br.com.sicredi.coopvote.mapper;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.dto.TopicDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicMapper {

  @Mapping(target = "voteDifference", ignore = true)
  @Mapping(target = "votingResult", ignore = true)
  Topic toEntity(TopicDto dto);

  TopicDto toDto(Topic entity);
}
