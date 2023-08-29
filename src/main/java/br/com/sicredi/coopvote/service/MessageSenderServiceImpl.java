package br.com.sicredi.coopvote.service;

import br.com.sicredi.coopvote.dto.VotingResultDto;
import br.com.sicredi.coopvote.service.interfaces.MessageSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageSenderServiceImpl implements MessageSenderService {

  private final RabbitTemplate rabbitTemplate;

  public MessageSenderServiceImpl(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Value("${app-config.rabbit.exchange.vote}")
  private String voteTopicExchange;

  @Value("${app-config.rabbit.routingKey.vote}")
  private String voteKey;

  @Override
  public void sendVoteUpdateMessage(VotingResultDto message) {
    try {
      log.info("Send message: {}", new ObjectMapper().writeValueAsString(message));
      rabbitTemplate.convertAndSend(voteTopicExchange, voteKey, message);
      log.info("Message was sent successfully.");
    } catch (Exception ex) {
      log.info("Error while trying to send voting result message: ", ex);
    }
  }
}
