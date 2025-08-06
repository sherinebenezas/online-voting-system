package com.example.online.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.online.Repository.PollOptionRepository;
import com.example.online.Repository.PollRepository;
import com.example.online.Repository.UserRepository;
import com.example.online.Repository.VoteRepository;
import com.example.online.dto.PollVoteRequest;
import com.example.online.dto.VoteRequest;
import com.example.online.model.Poll;
import com.example.online.model.PollOption;
import com.example.online.model.User;
import com.example.online.model.Vote;
@Service
public class VoteService {
     @Autowired private PollRepository pollRepo;
     @Autowired private VoteRepository voteRepo;
    @Autowired private PollOptionRepository optionRepo;
    @Autowired private UserRepository userRepo;


    public void castVote(Poll poll, PollVoteRequest request, User user) {
        if (poll.getPrivacy().equals("PRIVATE") && user == null)
            throw new RuntimeException("Login required for private poll");

        if (user != null && voteRepo.existsByPollIdAndVoter_Id(poll.getId(), user.getId()))
            throw new RuntimeException("Already voted");

        if (user == null && voteRepo.existsByPollIdAndIpAddress(poll.getId(), request.ipAddress))
            throw new RuntimeException("Already voted from IP");

        PollOption option = optionRepo.findById(request.optionId).orElseThrow();
        option.setVoteCount(option.getVoteCount() + 1);
        optionRepo.save(option);

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setOption(option);
        vote.setVoter(user);
        vote.setIpAddress(user == null ? request.ipAddress : null);
        vote.setTimestamp(LocalDateTime.now());
        voteRepo.save(vote);
    }
    public void castVote(VoteRequest request) {
    Poll poll = pollRepo.findById(request.pollId)
        .orElseThrow(() -> new RuntimeException("Poll not found"));

    PollOption option = optionRepo.findById(request.optionId)
        .orElseThrow(() -> new RuntimeException("Option not found"));

    if (!option.getPoll().getId().equals(poll.getId())) {
        throw new RuntimeException("Option does not belong to the poll");
    }

    Vote vote = new Vote();
    vote.setPoll(poll);
    vote.setOption(option);

    if (request.voterId != null) {
        User user = userRepo.findById(request.voterId)
            .orElseThrow(() -> new RuntimeException("Voter not found"));
        vote.setVoter(user);
    }

    // Increment vote count
    option.setVoteCount(option.getVoteCount() + 1);
    optionRepo.save(option);

    voteRepo.save(vote);
}


    public List<Vote> getVotesByPoll(Long pollId) {
        return voteRepo.findByPollId(pollId);
    }

    public List<Vote> getVotesByUser(Long userId) {
        return voteRepo.findByVoter_Id(userId);
    }
}
