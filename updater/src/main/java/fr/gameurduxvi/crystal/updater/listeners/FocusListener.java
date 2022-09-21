package fr.gameurduxvi.crystal.updater.listeners;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Consumer;

public abstract class FocusListener extends FocusAdapter {

    protected Consumer<FocusEvent> action;

    protected FocusListener(Consumer<FocusEvent> action) {
        this.action = action;
    }

    public static class Gained extends FocusListener {
        public Gained(Consumer<FocusEvent> action) {super(action);}
        @Override public void focusGained(FocusEvent e) {action.accept(e);}
    }

    public static class Lost extends FocusListener {
        public Lost(Consumer<FocusEvent> action) {super(action);}
        @Override public void focusLost(FocusEvent e) {action.accept(e);}
    }
}
