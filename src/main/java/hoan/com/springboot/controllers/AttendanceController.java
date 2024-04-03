package hoan.com.springboot.controllers;

import hoan.com.springboot.common.util.Response;
import hoan.com.springboot.models.entities.AttendanceEntity;
import hoan.com.springboot.services.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/api/attendances")
@Tag(name = "Attendance resource")
@Validated
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/", "https://hrm-frontend-two.vercel.app/"})
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @Operation(summary = "Checkin")
    @GetMapping("check-in")
    public Response<Boolean> checkIn() {
        return Response.of(attendanceService.checkIn());
    }

    @Operation(summary = "Checkout")
    @GetMapping("check-out")
    public Response<Boolean> checkOut(String note) {
        return Response.of(attendanceService.checkOut(note));
    }

    @Operation(summary = "get all")
    @GetMapping()
    @PreAuthorize("hasPermission(null, 'attendance:view')")
    public Response<List<AttendanceEntity>> getAll() {
        return Response.of(attendanceService.getAll());
    }

    @Operation(summary = "Export attendance to excel")
    @GetMapping("export")
    @PreAuthorize("hasPermission(null, 'attendance:view')")
    public void export(HttpServletResponse response) {
        attendanceService.exportToExcel(response);
    }

    @Operation(summary = "get my attendance")
    @GetMapping("my-attendance")
    public Response<List<AttendanceEntity>> getMyAttendance() {
        return Response.of(attendanceService.myAttendance());
    }
}
