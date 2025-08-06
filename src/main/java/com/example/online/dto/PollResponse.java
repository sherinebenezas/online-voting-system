package com.example.online.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PollResponse {
     public Long id;
    public String question;
    public String status;
    public String privacy;
    public String privateLink;
    public LocalDateTime createdAt;
    public CreatorDTO creator;
    public List<OptionDTO> options;

    public static class CreatorDTO {
        public Long id;
        public String name;
        public String email;
        public String passwordHash;
        public String role;
    }

    public static class OptionDTO {
        public Long id;
        public String text;
        public int voteCount;
    }
    
}
