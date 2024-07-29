package com.beyond.chat.chatting.service;

import com.beyond.chat.chatting.domain.ChatMessage;
import com.beyond.chat.chatting.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
public class ChatService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private Set<Pattern> forbiddenWordsPatterns = new HashSet<>();

    @PostConstruct
    public void loadForbiddenWords() throws IOException {
        // 'badWords.txt' 파일을 읽어와서 forbiddenWordsPatterns 리스트에 저장합니다.
        try (InputStream inputStream = getClass().getResourceAsStream("/badWords.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    forbiddenWordsPatterns.add(Pattern.compile("(?i)" + line.trim().replace("*", "\\*")));
                } catch (PatternSyntaxException e) {
                    System.err.println("Invalid regex pattern: " + line.trim());
                }
            }
        }
    }

    // 메시지에서 욕설을 필터링하는 함수
    private String filterMessage(String content) {
        for (Pattern pattern : forbiddenWordsPatterns) {
            content = pattern.matcher(content).replaceAll(m -> "*".repeat(m.group().length()));
        }
        return content;
    }

    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        // 메시지를 저장하기 전에 필터링을 적용합니다.
        chatMessage.setContent(filterMessage(chatMessage.getContent()));
        return chatMessageRepository.save(chatMessage);
    }
}
