package me.a8kj.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.eventbus.Event;

@RequiredArgsConstructor
@Getter
public class GreetingEvent extends Event {
    
    private final String name;
}
