package br.com.sicredi.coopvote.service;

import br.com.sicredi.coopvote.dto.TopicDto;
import br.com.sicredi.coopvote.mapper.TopicMapper;
import br.com.sicredi.coopvote.repository.TopicRepository;
import br.com.sicredi.coopvote.service.interfaces.TopicService;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {

  private final TopicRepository topicRepository;
  private final TopicMapper topicMapper;

  public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper) {
    this.topicRepository = topicRepository;
    this.topicMapper = topicMapper;
  }

  @Override
  public TopicDto createTopic(TopicDto request) {
    var topic = topicMapper.toEntity(request);
    var savedTopic = topicRepository.save(topic);
    return topicMapper.toDto(savedTopic);
  }
}
