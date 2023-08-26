package br.com.sicredi.coopvote.controller;

import br.com.sicredi.coopvote.controller.interfaces.TopicController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicControllerImpl implements TopicController {

  @Override
  public ResponseEntity<Void> createTopic() {
    return null;
  }
}
