package br.com.sicredi.coopvote.mapper;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.dto.TopicDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {

  Topic toEntity(TopicDto dto);

  TopicDto toDto(Topic entity);
}
