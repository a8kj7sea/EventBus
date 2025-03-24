package me.a8kj.example;

import java.util.Scanner;

import me.a8kj.eventbus.manager.EventManager;

public class Main {

    private static EventManager eventManager;

    public static void main(String[] args) {

        eventManager = new EventManager();
        eventManager.register(new GreetingListener());

        Scanner scanner = new Scanner(System.in);

        var name = scanner.nextLine();
        if (name != null) {

            eventManager.callEvent(new GreetingEvent(name));
            triggerCancellableGreetingEvent(name);
        }
    }

    private static void triggerCancellableGreetingEvent(String name) {

        CancellableGreetingEvent event = new CancellableGreetingEvent(name);

        eventManager.callEvent(event);

        if (event.isCancelled()) {
            System.out.println("No greetings for you :) !");
            return;
        }

        System.out.println("Hi " + name);

    }
}
