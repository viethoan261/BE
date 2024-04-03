package hoan.com.springboot.controllers;

import hoan.com.springboot.common.util.Response;
import hoan.com.springboot.models.entities.PermissionEntity;
import hoan.com.springboot.payload.response.EventResponse;
import hoan.com.springboot.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Event resource")
@Validated
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/", "https://hrm-frontend-two.vercel.app/"})
public class EventController {
    private final EventService eventService;


    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Get events")
    @GetMapping("")
    public Response<List<EventResponse>> getAll() {
        return Response.of(eventService.getEvents());
    }
}
