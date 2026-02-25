package me.a8kj.flux.template;

public interface Toggleable {
    boolean isEnabled();
    void setEnabled(boolean enabled);

    default void toggle() {
        setEnabled(!isEnabled());
    }
}