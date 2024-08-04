package com.beyond.teenkiri.subscribe.controller;

import com.beyond.teenkiri.subscribe.dto.SubscribeCreateDto;
import com.beyond.teenkiri.subscribe.dto.SubscribeListResDto;
import com.beyond.teenkiri.subscribe.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @Autowired
    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping
    public String createSubscription(@RequestBody SubscribeCreateDto dto) {
        subscribeService.createSubscribe(dto);
        return "Subscription created successfully";
    }

    @GetMapping
    public Page<SubscribeListResDto> listSubscriptions(Pageable pageable) {
        return subscribeService.listSubscriptions(pageable);
    }

    @DeleteMapping("/{id}")
    public String deleteSubscription(@PathVariable Long id) {
        subscribeService.deleteSubscription(id);
        return "Subscription deleted successfully";
    }
}
