package br.com.sicredi.coopvote.controller.interfaces;

import org.springframework.http.ResponseEntity;

public interface TopicController {
  ResponseEntity<Void> createTopic();
}
