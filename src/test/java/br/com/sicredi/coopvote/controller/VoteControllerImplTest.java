package br.com.sicredi.coopvote.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.sicredi.coopvote.base.BaseIntegrationUtil;
import br.com.sicredi.coopvote.dto.TopicDto;
import br.com.sicredi.coopvote.dto.VoteDto;
import br.com.sicredi.coopvote.enums.VoteEnum;
import br.com.sicredi.coopvote.service.interfaces.TopicService;
import br.com.sicredi.coopvote.service.interfaces.VoteService;
import br.com.sicredi.coopvote.service.interfaces.VotingSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class VoteControllerImplTest extends BaseIntegrationUtil {

  @Autowired private VoteService voteService;
  @Autowired public MockMvc mockMvc;
  @Autowired private VotingSessionService votingSessionService;
  @Autowired private TopicService topicService;

  @BeforeEach
  void setUp() {
    var topic = new TopicDto();
    topic.setId(1L);
    topic.setDescription("Test");
    topicService.createTopic(topic);
    votingSessionService.openSession(topic.getId(), 1);
  }

  @Test
  void shouldCastVote() throws Exception {
    var inputVote = new VoteDto();
    inputVote.setVotingSessionId(1L);
    inputVote.setMemberId(2L);
    inputVote.setVoteValue(VoteEnum.YES);

    mockMvc
        .perform(
            post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(inputVote)))
        .andExpect(status().isCreated());
  }
}
