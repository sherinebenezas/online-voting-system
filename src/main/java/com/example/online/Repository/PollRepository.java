package com.example.online.Repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.online.model.Poll;
@Repository
public interface PollRepository extends JpaRepository<Poll, Long>{
      Page<Poll> findByPrivacy(String privacy, Pageable pageable);
    List<Poll> findByCreatorId(Long creatorId);
}
