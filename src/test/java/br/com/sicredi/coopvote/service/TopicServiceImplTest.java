package br.com.sicredi.coopvote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import br.com.sicredi.coopvote.domain.Topic;
import br.com.sicredi.coopvote.dto.TopicDto;
import br.com.sicredi.coopvote.mapper.TopicMapper;
import br.com.sicredi.coopvote.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TopicServiceImplTest {

  @Mock private TopicRepository topicRepository;

  @Mock private TopicMapper topicMapper;

  @InjectMocks private TopicServiceImpl topicService;

  private TopicDto testTopicDto;
  private Topic testTopicEntity;

  @BeforeEach
  void setUp() {
    testTopicDto = new TopicDto();
    testTopicEntity = new Topic();
  }

  @Test
  void shouldSuccessfullyCreateTopic() {
    when(topicMapper.toEntity(testTopicDto)).thenReturn(testTopicEntity);
    when(topicRepository.save(testTopicEntity)).thenReturn(testTopicEntity);
    when(topicMapper.toDto(testTopicEntity)).thenReturn(testTopicDto);

    TopicDto result = topicService.createTopic(testTopicDto);

    assertNotNull(result);
    assertEquals(testTopicDto, result);
  }
}
