package com.example.online.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.Service.VoteService;
import com.example.online.dto.VoteRequest;
import com.example.online.model.Vote;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
     @Autowired private VoteService voteService;
  @PostMapping("/cast")
    public String castVote(@RequestBody VoteRequest request) {
        voteService.castVote(request);
        return "Vote cast successfully";
    }
    @GetMapping("/poll/{pollId}")
    public List<Vote> getVotesByPoll(@PathVariable Long pollId) {
        return voteService.getVotesByPoll(pollId);
    }

    @GetMapping("/user/{userId}")
    public List<Vote> getVotesByUser(@PathVariable Long userId) {
        return voteService.getVotesByUser(userId);
    }
}
