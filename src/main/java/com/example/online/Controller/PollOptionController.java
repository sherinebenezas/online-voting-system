package com.example.online.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.online.Service.PollOptionService;
import com.example.online.model.PollOption;
@RestController
@RequestMapping("/api/options")
public class PollOptionController {
     @Autowired private PollOptionService optionService;

    @GetMapping("/poll/{pollId}")
    public List<PollOption> getOptionsByPoll(@PathVariable Long pollId) {
        return optionService.getOptionsByPollId(pollId);
    }

    @GetMapping("/{id}")
    public PollOption getOptionById(@PathVariable Long id) {
        return optionService.getOptionById(id);
    }

    @PutMapping("/{id}")
    public PollOption updateOptionText(@PathVariable Long id, @RequestParam String text) {
        return optionService.updateOptionText(id, text);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.ok("Option deleted successfully");
    }
}
