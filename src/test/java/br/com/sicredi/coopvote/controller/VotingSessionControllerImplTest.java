package br.com.sicredi.coopvote.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.sicredi.coopvote.base.BaseIntegrationUtil;
import br.com.sicredi.coopvote.dto.TopicDto;
import br.com.sicredi.coopvote.service.interfaces.TopicService;
import br.com.sicredi.coopvote.service.interfaces.VotingSessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class VotingSessionControllerImplTest extends BaseIntegrationUtil {

  @Autowired public MockMvc mockMvc;
  @Autowired private VotingSessionService votingSessionService;
  @Autowired private TopicService topicService;

  @Test
  void shouldOpenVotingSession() throws Exception {
    var topicDto = new TopicDto();
    topicDto.setDescription("Test");
    var createdTopic = topicService.createTopic(topicDto);
    var durationMinutes = "5";

    mockMvc
        .perform(
            post("/sessions/topic/" + createdTopic.getId())
                .param("durationMinutes", durationMinutes)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }
}
