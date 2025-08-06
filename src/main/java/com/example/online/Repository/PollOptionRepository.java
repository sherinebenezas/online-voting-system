package com.example.online.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.model.PollOption;

public interface PollOptionRepository extends JpaRepository<PollOption, Long>{
 List<PollOption> findByPollId(Long pollId);
}
