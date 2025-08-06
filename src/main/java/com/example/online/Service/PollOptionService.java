package com.example.online.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.online.Repository.PollOptionRepository;
import com.example.online.model.PollOption;
@Service
public class PollOptionService {
     @Autowired private PollOptionRepository optionRepo;

    public List<PollOption> getOptionsByPollId(Long pollId) {
        return optionRepo.findByPollId(pollId);
    }

    public PollOption getOptionById(Long id) {
        return optionRepo.findById(id).orElseThrow(() -> new RuntimeException("Option not found"));
    }

    public PollOption updateOptionText(Long id, String newText) {
        PollOption option = getOptionById(id);
        option.setText(newText);
        return optionRepo.save(option);
    }

    public void deleteOption(Long id) {
        optionRepo.deleteById(id);
    }
}
