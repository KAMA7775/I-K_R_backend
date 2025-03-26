package org.example.bookingservice.service;

import org.example.bookingservice.dto.BookingDto;
import org.example.bookingservice.repository.BookingHandler;
import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.entity.BookingType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BookingRouter {
    private final Map<BookingType, BookingHandler<?>> handlerMap = new HashMap<>();

    public BookingRouter(List<BookingHandler<?>> handlers) {
        for (BookingHandler<?> handler : handlers) {
            handlerMap.put(handler.getSupportedType(), handler);
        }
    }

    public BookingResponse route(BookingType type, BookingDto dto) {
        BookingHandler handler = handlerMap.get(type);
        if (handler == null) {
            throw new IllegalArgumentException("No handler for type: " + type);
        }
        return handler.handleBooking(dto);
    }
}
