package br.com.sicredi.coopvote.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  @Value("${app-config.rabbit.exchange.vote}")
  private String voteTopicExchange;

  @Value("${app-config.rabbit.routingKey.vote}")
  private String voteKey;

  @Value("${app-config.rabbit.queue.vote}")
  private String voteMq;

  @Bean
  public TopicExchange voteTopicExchange() {
    return new TopicExchange(voteTopicExchange);
  }

  @Bean
  public Queue voteMq() {
    return new Queue(voteMq, true);
  }

  @Bean
  public Binding voteMqBinding(TopicExchange topicExchange) {
    return BindingBuilder.bind(voteMq()).to(topicExchange).with(voteKey);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
