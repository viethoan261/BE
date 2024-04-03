package hoan.com.springboot.services;

import hoan.com.springboot.models.entities.AttendanceEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

public interface AttendanceService {
    boolean checkIn();

    boolean checkOut(String note);

    List<AttendanceEntity> getAll();

    void exportToExcel(HttpServletResponse response);

    List<AttendanceEntity> myAttendance();
}
