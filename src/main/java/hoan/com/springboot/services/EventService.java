package hoan.com.springboot.services;

import hoan.com.springboot.payload.response.EventResponse;

import java.util.List;

public interface EventService {
    List<EventResponse> getEvents();
}
