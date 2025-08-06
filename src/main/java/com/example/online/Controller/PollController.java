package com.example.online.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.Service.PollService;
import com.example.online.Service.VoteService;
import com.example.online.dto.PollCreateRequest;
import com.example.online.dto.PollResponse;
import com.example.online.dto.PollResultsResponse;
import com.example.online.dto.PollVoteRequest;
import com.example.online.model.Poll;
import com.example.online.model.User;
@RestController
@RequestMapping("/api/polls")
public class PollController {
     @Autowired private PollService pollService;
    @Autowired private VoteService voteService;

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody PollCreateRequest request) {
        User creator = getAuthenticatedUser();
        return new ResponseEntity<>(pollService.createPoll(request), HttpStatus.CREATED);
    }

    @GetMapping("/public")
    public List<PollResponse> getPublicPolls(@RequestParam int page, @RequestParam int size) {
        return pollService.getPublicPolls(page, size);
    }

    @GetMapping("/{id}")
    public Poll getPollDetails(@PathVariable Long id) {
        return pollService.getPoll(id);
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<String> vote(@PathVariable Long id, @RequestBody PollVoteRequest request) {
        Poll poll = pollService.getPoll(id);
        User user = getOptionalUser();
        voteService.castVote(poll, request, user);
        return ResponseEntity.ok("Vote recorded successfully");
    }

    @GetMapping("/{id}/results")
    public PollResultsResponse getResults(@PathVariable Long id) {
        return pollService.getResults(id);
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<String> closePoll(@PathVariable Long id) {
        User creator = getAuthenticatedUser();
        pollService.closePoll(id, creator);
        return ResponseEntity.ok("Poll closed successfully");
    }

    @PostMapping("/{id}/privacy")
    public ResponseEntity<String> updatePrivacy(@PathVariable Long id, @RequestParam String privacy) {
        pollService.changePrivacy(id, privacy);
        return ResponseEntity.ok("Privacy updated successfully");
    }
     private User getAuthenticatedUser() {
        User u = new User();
        u.setId(1L);
        u.setName("Mock User");
        u.setRole("CREATOR");
        return u;
    }

    private User getOptionalUser() {
        return null; 
    }
}
