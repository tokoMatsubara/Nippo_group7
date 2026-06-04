package com.daily_app.demo.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class CallLlmService {

    ChatClient _client;

    public CallLlmService(ChatClient.Builder builder){
        _client = builder.build();
    }

    public String chatResponse(String prompt){
        return _client.prompt()
            .user(prompt)
            .call()
            .content();
    }
}
