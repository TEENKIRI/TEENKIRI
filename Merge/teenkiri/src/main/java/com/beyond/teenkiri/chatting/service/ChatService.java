package com.beyond.teenkiri.chatting.service;

import com.beyond.teenkiri.chatting.domain.ChatMessage;
import com.beyond.teenkiri.chatting.repository.ChatMessageRepository;
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

@Service // Spring 서비스 컴포넌트임을 나타내는 애너테이션
public class ChatService {
    @Autowired // Spring의 의존성 주입을 통해 ChatMessageRepository를 자동으로 주입
    private ChatMessageRepository chatMessageRepository;

    private Set<Pattern> forbiddenWordsPatterns = new HashSet<>(); // 금지된 단어를 저장할 패턴의 집합

    @PostConstruct // 빈 초기화 후 실행될 메서드임을 나타내는 애너테이션
    public void loadForbiddenWords() throws IOException {
        // 'badWords.txt' 파일을 읽어와서 forbiddenWordsPatterns 리스트에 저장합니다.
        try (InputStream inputStream = getClass().getResourceAsStream("/badWords.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    forbiddenWordsPatterns.add(Pattern.compile("(?i)" + line.trim().replace("*", "\\*"))); // 읽어온 단어를 패턴으로 변환하여 리스트에 추가
                } catch (PatternSyntaxException e) {
                    System.err.println("Invalid regex pattern: " + line.trim()); // 잘못된 정규식 패턴이 있을 경우 에러 메시지 출력
                }
            }
        }
    }

    // 메시지에서 욕설을 필터링하는 함수
    private String filterMessage(String content) {
        for (Pattern pattern : forbiddenWordsPatterns) { // 모든 금지된 단어 패턴에 대해
            content = pattern.matcher(content).replaceAll(m -> "*".repeat(m.group().length())); // 패턴과 일치하는 부분을 *로 대체
        }
        return content; // 필터링된 메시지 반환
    }

    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        // 메시지를 저장하기 전에 필터링을 적용합니다.
        chatMessage.setContent(filterMessage(chatMessage.getContent())); // 메시지 내용을 필터링하여 설정
        return chatMessageRepository.save(chatMessage); // 필터링된 메시지를 저장하고 반환
    }
}
