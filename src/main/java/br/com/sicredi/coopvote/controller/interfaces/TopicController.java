package br.com.sicredi.coopvote.controller.interfaces;

import br.com.sicredi.coopvote.dto.TopicDto;
import br.com.sicredi.coopvote.record.ApiResult;
import org.springframework.http.ResponseEntity;

public interface TopicController {

  ResponseEntity<ApiResult<TopicDto>> createTopic(TopicDto topic);
}
