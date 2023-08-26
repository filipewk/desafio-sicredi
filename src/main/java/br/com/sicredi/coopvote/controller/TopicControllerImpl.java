package br.com.sicredi.coopvote.controller;

import br.com.sicredi.coopvote.controller.interfaces.TopicController;
import br.com.sicredi.coopvote.dto.ApiResponse;
import br.com.sicredi.coopvote.dto.TopicDto;
import br.com.sicredi.coopvote.service.interfaces.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("topic")
public class TopicControllerImpl extends BaseController implements TopicController {

  private final TopicService topicService;

  public TopicControllerImpl(TopicService topicService) {
    this.topicService = topicService;
  }

  @Override
  @PostMapping
  public ResponseEntity<ApiResponse<TopicDto>> createTopic(@RequestBody TopicDto topic) {
    TopicDto createdTopic = topicService.createTopic(topic);
    return createdResponse(createdTopic);
  }
}
