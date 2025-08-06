package com.example.online.dto;

import java.util.List;

public class PollResultsResponse {
    public String question;
    public List<OptionResult> results;

    public static class OptionResult {
        public String text;
        public int voteCount;
        public double percentage;
    }
}
