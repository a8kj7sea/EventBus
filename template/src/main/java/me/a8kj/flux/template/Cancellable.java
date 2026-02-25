package me.a8kj.flux.template;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
