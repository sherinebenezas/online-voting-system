package com.example.online.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.online.Repository.PollOptionRepository;
import com.example.online.Repository.PollRepository;
import com.example.online.Repository.UserRepository;
import com.example.online.Repository.VoteRepository;
import com.example.online.dto.PollCreateRequest;
import com.example.online.dto.PollResponse;
import com.example.online.dto.PollResultsResponse;
import com.example.online.model.Poll;
import com.example.online.model.PollOption;
import com.example.online.model.User;
import com.example.online.model.Vote;
@Service
public class PollService {
     @Autowired private PollRepository pollRepo;
    @Autowired private PollOptionRepository optionRepo;
    @Autowired private VoteRepository voteRepo;
@Autowired private UserRepository userRepo;

  public Poll createPoll(PollCreateRequest request) {
    
User creator = userRepo.findById(request.creatorId)
    .orElseThrow(() -> new RuntimeException("Creator not found"));

    Poll poll = new Poll();
    poll.setQuestion(request.question);
    poll.setPrivacy(request.privacy);
    poll.setStatus("OPEN");
    poll.setCreator(creator);
    poll.setCreatedAt(LocalDateTime.now());

    if ("PRIVATE".equalsIgnoreCase(request.privacy)) {
        poll.setPrivateLink(UUID.randomUUID().toString());
    }

    final Poll savedPoll = pollRepo.save(poll); // fix: store as final
List<PollOption> options = request.options.stream().map(text -> {
    PollOption option = new PollOption();
    option.setPoll(savedPoll);
    option.setText(text);
    return option;
}).collect(Collectors.toList());

optionRepo.saveAll(options);


savedPoll.setOptions(options);

return savedPoll;

   
}


     public List<PollResponse> getPublicPolls(int page, int size) {
        return pollRepo.findByPrivacy("PUBLIC", PageRequest.of(page, size))
                .map(this::toPollResponse).toList();
    }

    public List<PollResponse> getPollsByCreator(Long creatorId) {
        return pollRepo.findByCreatorId(creatorId).stream().map(this::toPollResponse).toList();
    }

    public Poll getPoll(Long id) {
        return pollRepo.findById(id).orElseThrow(() -> new RuntimeException("Poll not found"));
    }

    public void closePoll(Long pollId, User creator) {
        Poll poll = getPoll(pollId);
        if (!poll.getCreator().getId().equals(creator.getId())) throw new RuntimeException("Unauthorized");
        poll.setStatus("CLOSED");
        pollRepo.save(poll);
    }

    public void changePrivacy(Long pollId, String newPrivacy) {
        Poll poll = getPoll(pollId);
        if (!voteRepo.findByPollId(pollId).isEmpty()) {
            throw new RuntimeException("Cannot change privacy after votes have been cast.");
        }
        poll.setPrivacy(newPrivacy);
        pollRepo.save(poll);
    }

    public PollResultsResponse getResults(Long pollId) {
        Poll poll = getPoll(pollId);
        if (!poll.getStatus().equals("OPEN") && !poll.getStatus().equals("CLOSED")) {
            throw new RuntimeException("Results not available for this poll.");
        }

        List<Vote> votes = voteRepo.findByPollId(pollId);
        int total = votes.size();

        List<PollResultsResponse.OptionResult> results = poll.getOptions().stream().map(opt -> {
            PollResultsResponse.OptionResult r = new PollResultsResponse.OptionResult();
            r.text = opt.getText();
            r.voteCount = opt.getVoteCount();
            r.percentage = total == 0 ? 0 : (opt.getVoteCount() * 100.0) / total;
            return r;
        }).collect(Collectors.toList());

        PollResultsResponse response = new PollResultsResponse();
        response.question = poll.getQuestion();
        response.results = results;
        return response;
    }

    private PollResponse toPollResponse(Poll poll) {
        PollResponse res = new PollResponse();
    res.id = poll.getId();
    res.question = poll.getQuestion();
    res.status = poll.getStatus();
    res.privacy = poll.getPrivacy();
    res.privateLink = poll.getPrivateLink();
    res.createdAt = poll.getCreatedAt();

    PollResponse.CreatorDTO creatorDTO = new PollResponse.CreatorDTO();
    creatorDTO.id = poll.getCreator().getId();
    creatorDTO.name = poll.getCreator().getName();
    creatorDTO.email = poll.getCreator().getEmail();
    creatorDTO.passwordHash = poll.getCreator().getPasswordHash();
    creatorDTO.role = poll.getCreator().getRole();
    res.creator = creatorDTO;

    
    res.options = poll.getOptions().stream().map(opt -> {
        PollResponse.OptionDTO dto = new PollResponse.OptionDTO();
        dto.id = opt.getId();
        dto.text = opt.getText();
        dto.voteCount = opt.getVoteCount();
        return dto;
    }).toList();

    return res;
    }

}
