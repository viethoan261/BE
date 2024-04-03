package hoan.com.springboot.controllers;

import hoan.com.springboot.common.enums.TimeOffStatus;
import hoan.com.springboot.common.util.Response;
import hoan.com.springboot.models.entities.TimeOffEntity;
import hoan.com.springboot.models.entities.TimeOffHistoryEntity;
import hoan.com.springboot.models.entities.TimeOffRequestEntity;
import hoan.com.springboot.payload.request.TimeOffRequest;
import hoan.com.springboot.payload.response.TimeoffRequestResponse;
import hoan.com.springboot.services.TimeOffHistoryService;
import hoan.com.springboot.services.TimeOffRequestService;
import hoan.com.springboot.services.TimeOffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/time-off")
@Tag(name = "Time off resource")
@Validated
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/", "https://hrm-frontend-two.vercel.app/"})
public class TimeOffController {
    private final TimeOffHistoryService timeOffHistoryService;

    private final TimeOffRequestService timeOffRequestService;

    private final TimeOffService timeOffService;

    public TimeOffController(TimeOffHistoryService timeOffHistoryService, TimeOffRequestService timeOffRequestService, TimeOffService timeOffService) {
        this.timeOffHistoryService = timeOffHistoryService;
        this.timeOffRequestService = timeOffRequestService;
        this.timeOffService = timeOffService;
    }

    @Operation(summary = "Get all balance history")
    @GetMapping("balance")
    public Response<List<TimeOffHistoryEntity>> getAllBalance() {
        return Response.of(timeOffHistoryService.getAll());
    }

    @Operation(summary = "Request time off")
    @PostMapping("request")
    public Response<TimeOffRequestEntity> requestTimeOff(@RequestBody @Valid TimeOffRequest request) {
        return Response.of(timeOffRequestService.create(request));
    }

    @Operation(summary = "Get my request")
    @GetMapping("my-request")
    public Response<List<TimeOffRequestEntity>> getMyRequest() {
        return Response.of(timeOffRequestService.myRequest());
    }

    @Operation(summary = "Get all request")
    @GetMapping("")
    @PreAuthorize("hasPermission(null, 'timeoff:view')")
    public Response<List<TimeoffRequestResponse>> getAll() {
        return Response.of(timeOffRequestService.getAll());
    }

    @Operation(summary = "Get my timeoff")
    @GetMapping("/my-timeoff")
//    @PreAuthorize("hasPermission(null, 'timeoff:view')")
    public Response<List<TimeOffEntity>> getMyTimeOff() {
        return Response.of(timeOffService.getMyTimeOff());
    }

    @Operation(summary = "change status timeoff")
    @PostMapping("/{id}/change-status")
//    @PreAuthorize("hasPermission(null, 'timeoff:update')")
    public Response<Boolean> changeStatus(@PathVariable String id, TimeOffStatus status) {
        return Response.of(timeOffRequestService.changeRequestStatus(UUID.fromString(id), status));
    }
}
