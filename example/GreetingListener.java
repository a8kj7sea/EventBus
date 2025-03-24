package me.a8kj.example;

import me.a8kj.eventbus.Listener;
import me.a8kj.eventbus.annotation.EventSubscribe;

public class GreetingListener implements Listener {

    @EventSubscribe(description = "Simple greetings event!")
    public void onGreet(GreetingEvent event) {
        System.out.println("Hi " + event.getName());
    }

    @EventSubscribe
    public void onCancellableGreeting(CancellableGreetingEvent event) {

        if (!event.getName().equalsIgnoreCase("a8kj7sea")) {
            event.setCancelled(true);
            return;
        }

        System.out.println("Hi " + event.getName());
    }

}
