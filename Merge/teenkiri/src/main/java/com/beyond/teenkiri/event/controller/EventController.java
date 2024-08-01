package com.beyond.teenkiri.event.controller;

import com.beyond.teenkiri.event.dto.EventDetailDto;
import com.beyond.teenkiri.event.dto.EventSaveReqDto;
import com.beyond.teenkiri.event.dto.EventUpdateDto;
import com.beyond.teenkiri.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("event")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("create")
    public String createEventForm() {
        return "/board/event/create";
    }

    @PostMapping("create")
    public String createEvent(@ModelAttribute EventSaveReqDto dto, Model model) {
        try {
            eventService.createEvent(dto);
            return "redirect:/event/list";
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/board/event/create";
        }
    }

    @GetMapping("list")
    public String getAllEvents(Model model, @PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("eventList", eventService.eventList(pageable));
        return "/board/event/list";
    }

    @GetMapping("detail/{id}")
    public String getEventDetail(@PathVariable Long id, Model model) {
        EventDetailDto eventDetail = eventService.getEventDetail(id);
        model.addAttribute("event", eventDetail);
        return "/board/event/detail";
    }

    @PostMapping("update/{id}")
    public String EventUpdate(@PathVariable Long id, @ModelAttribute EventUpdateDto dto){
        eventService.eventUpdate(id, dto);
        return "redirect:/board/event/detail/" + id;
    }

    @GetMapping("delete/{id}")
    public String EventDelete(@PathVariable Long id, Model model){
        eventService.eventDelete(id);
        return "redirect:/board/event/list";
    }
}
