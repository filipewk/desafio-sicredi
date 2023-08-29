package br.com.sicredi.coopvote.controller;

import br.com.sicredi.coopvote.controller.interfaces.TopicController;
import br.com.sicredi.coopvote.dto.TopicDto;
import br.com.sicredi.coopvote.record.ApiResult;
import br.com.sicredi.coopvote.service.interfaces.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("topics")
public class TopicControllerImpl extends BaseController implements TopicController {

  private final TopicService topicService;

  public TopicControllerImpl(TopicService topicService) {
    this.topicService = topicService;
  }

  @Override
  @PostMapping
  @Operation(summary = "Create a new topic", responses = @ApiResponse(responseCode = "201"))
  public ResponseEntity<ApiResult<TopicDto>> createTopic(@RequestBody TopicDto topic) {
    TopicDto createdTopic = topicService.createTopic(topic);
    return created(createdTopic);
  }
}
