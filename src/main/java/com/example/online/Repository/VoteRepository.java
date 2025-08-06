package com.example.online.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
 boolean existsByPollIdAndVoter_Id(Long pollId, Long voterId);
    boolean existsByPollIdAndIpAddress(Long pollId, String ipAddress);
    List<Vote> findByPollId(Long pollId);
    List<Vote> findByVoter_Id(Long voterId);
}
