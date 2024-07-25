package com.beyond.board_demo.event.controller;

import com.beyond.board_demo.event.dto.EventDetailDto;
import com.beyond.board_demo.event.dto.EventSaveReqDto;
import com.beyond.board_demo.event.dto.EventUpdateDto;
import com.beyond.board_demo.event.service.EventService;
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
        return "event/create";
    }

    @PostMapping("create")
    public String createEvent(@ModelAttribute EventSaveReqDto dto, Model model) {
        try {
            eventService.createEvent(dto, dto.getUserEmail());
            return "redirect:/event/list";
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "event/create";
        }
    }

    @GetMapping("list")
    public String getAllEvents(Model model, @PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("eventList", eventService.eventList(pageable));
        return "event/list";
    }

    @GetMapping("detail/{id}")
    public String getEventDetail(@PathVariable Long id, Model model) {
        EventDetailDto eventDetail = eventService.getEventDetail(id);
        model.addAttribute("event", eventDetail);
        return "event/detail";
    }

    @PostMapping("update/{id}")
    public String EventUpdate(@PathVariable Long id, @ModelAttribute EventUpdateDto dto){
        eventService.eventUpdate(id, dto);
        return "redirect:/event/detail/" + id;
    }

    @GetMapping("delete/{id}")
    public String EventDelete(@PathVariable Long id, Model model){
        eventService.eventDelete(id);
        return "redirect:/event/list";
    }
}
