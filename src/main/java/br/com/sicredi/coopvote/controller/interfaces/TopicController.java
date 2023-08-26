package br.com.sicredi.coopvote.controller.interfaces;

import br.com.sicredi.coopvote.dto.ApiResponse;
import br.com.sicredi.coopvote.dto.TopicDto;
import org.springframework.http.ResponseEntity;

public interface TopicController {
  ResponseEntity<ApiResponse<TopicDto>> createTopic(TopicDto topic);
}
