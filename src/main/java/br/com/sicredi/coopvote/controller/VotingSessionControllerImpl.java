package br.com.sicredi.coopvote.controller;

import br.com.sicredi.coopvote.controller.interfaces.VotingSessionController;
import br.com.sicredi.coopvote.dto.VotingSessionDto;
import br.com.sicredi.coopvote.record.ApiResult;
import br.com.sicredi.coopvote.service.interfaces.VotingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("session")
public class VotingSessionControllerImpl extends BaseController implements VotingSessionController {

  private final VotingSessionService votingSessionService;

  public VotingSessionControllerImpl(VotingSessionService votingSessionService) {
    this.votingSessionService = votingSessionService;
  }

  @Override
  @PostMapping("{topicId}")
  @Operation(summary = "Open a voting session")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Found the topic"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid topic ID supplied",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content)
      })
  public ResponseEntity<ApiResult<VotingSessionDto>> createTopic(
      @PathVariable Long topicId,
      @RequestParam(required = false, defaultValue = "1") Integer durationMinutes) {
    var votingSession = votingSessionService.openSession(topicId, durationMinutes);
    return ok(votingSession);
  }
}
