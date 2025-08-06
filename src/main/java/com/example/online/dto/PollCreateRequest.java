package com.example.online.dto;

import java.util.List;

public class PollCreateRequest {
    public Long creatorId;
    public String question;
    public String privacy;
    public List<String> options;
}
