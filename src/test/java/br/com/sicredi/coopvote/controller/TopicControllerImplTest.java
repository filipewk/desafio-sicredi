package br.com.sicredi.coopvote.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.sicredi.coopvote.base.BaseIntegrationUtil;
import br.com.sicredi.coopvote.dto.TopicDto;
import br.com.sicredi.coopvote.service.interfaces.TopicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class TopicControllerImplTest extends BaseIntegrationUtil {

  @Autowired public MockMvc mockMvc;
  @Autowired private TopicService topicService;

  @Test
  void shouldCreateTopic() throws Exception {
    var inputTopic = new TopicDto();
    inputTopic.setDescription("Test");

    mockMvc
        .perform(
            post("/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(inputTopic)))
        .andExpect(status().isCreated());
  }
}
