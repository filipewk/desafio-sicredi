package br.com.sicredi.coopvote.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.sicredi.coopvote.dto.VotingResultDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MessageSenderServiceImplTest {

  @Mock private RabbitTemplate rabbitTemplate;

  @InjectMocks private MessageSenderServiceImpl messageSenderService;

  private final String testExchange = "testExchange";
  private final String testRoutingKey = "testRoutingKey";

  private VotingResultDto votingResultDto;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(messageSenderService, "voteTopicExchange", testExchange);
    ReflectionTestUtils.setField(messageSenderService, "voteKey", testRoutingKey);

    votingResultDto = new VotingResultDto();
    votingResultDto.setTotalVotes(10);
  }

  @Test
  void shouldSuccessfullySendVoteUpdateMessage() {
    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(eq(testExchange), eq(testRoutingKey), eq(votingResultDto));

    assertDoesNotThrow(() -> messageSenderService.sendVoteUpdateMessage(votingResultDto));

    verify(rabbitTemplate, times(1))
        .convertAndSend(eq(testExchange), eq(testRoutingKey), eq(votingResultDto));
  }

  @Test
  void shouldLogErrorWhenExceptionOccurs() {
    doThrow(new AmqpException("Test"))
        .when(rabbitTemplate)
        .convertAndSend(eq(testExchange), eq(testRoutingKey), eq(votingResultDto));

    assertDoesNotThrow(() -> messageSenderService.sendVoteUpdateMessage(votingResultDto));
  }
}
