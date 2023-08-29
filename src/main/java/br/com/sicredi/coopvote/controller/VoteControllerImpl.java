package br.com.sicredi.coopvote.controller;

import br.com.sicredi.coopvote.controller.interfaces.VoteController;
import br.com.sicredi.coopvote.dto.VoteDto;
import br.com.sicredi.coopvote.record.ApiResult;
import br.com.sicredi.coopvote.service.interfaces.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("votes")
public class VoteControllerImpl extends BaseController implements VoteController {

  private final VoteService voteService;

  public VoteControllerImpl(VoteService voteService) {
    this.voteService = voteService;
  }

  @Override
  @PostMapping
  @Operation(summary = "Open a voting session", responses = @ApiResponse(responseCode = "200"))
  public ResponseEntity<ApiResult<VoteDto>> castVote(@RequestBody VoteDto vote) {
    return ok(voteService.castVote(vote));
  }
}
