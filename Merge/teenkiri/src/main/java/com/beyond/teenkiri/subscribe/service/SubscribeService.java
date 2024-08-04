package com.beyond.teenkiri.subscribe.service;

import com.beyond.teenkiri.subscribe.domain.Subscribe;
import com.beyond.teenkiri.subscribe.dto.SubscribeCreateDto;
import com.beyond.teenkiri.subscribe.dto.SubscribeListResDto;
import com.beyond.teenkiri.subscribe.repository.SubscribeRepository;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.repository.SubjectRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubscribeService(SubscribeRepository subscribeRepository, UserRepository userRepository, SubjectRepository subjectRepository) {
        this.subscribeRepository = subscribeRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    public Subscribe createSubscribe(SubscribeCreateDto dto) {
        User user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Subject subject = subjectRepository.findByTitle(dto.getSubjectTitle())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));

        Subscribe subscribe = dto.toEntity(user, subject);
        return subscribeRepository.save(subscribe);
    }

    public Page<SubscribeListResDto> listSubscriptions(Pageable pageable) {
        return subscribeRepository.findAll(pageable)
                .map(Subscribe::listFromEntity);
    }

    public void deleteSubscription(Long id) {
        Subscribe subscribe = subscribeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        subscribeRepository.delete(subscribe);
    }
}
