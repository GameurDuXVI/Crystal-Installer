package fr.gameurduxvi.crystal.updater.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public abstract class WindowListener extends WindowAdapter {
    protected Consumer<WindowEvent> action;

    protected WindowListener(Consumer<WindowEvent> action) {
        this.action = action;
    }

    public static class Opened extends WindowListener {
        public Opened(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowOpened(WindowEvent e) {action.accept(e);}
    }

    public static class Closing extends WindowListener {
        public Closing(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowClosing(WindowEvent e) {action.accept(e);}
    }

    public static class Closed extends WindowListener {
        public Closed(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowClosed(WindowEvent e) {action.accept(e);}
    }

    public static class Iconified extends WindowListener {
        public Iconified(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowIconified(WindowEvent e) {action.accept(e);}
    }

    public static class Deiconified extends WindowListener {
        public Deiconified(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowDeiconified(WindowEvent e) {action.accept(e);}
    }

    public static class Activated extends WindowListener {
        public Activated(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowActivated(WindowEvent e) {action.accept(e);}
    }

    public static class Deactivated extends WindowListener {
        public Deactivated(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowDeactivated(WindowEvent e) {action.accept(e);}
    }

    public static class StateChanged extends WindowListener {
        public StateChanged(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowStateChanged(WindowEvent e) {action.accept(e);}
    }

    public static class GainedFocus extends WindowListener {
        public GainedFocus(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowGainedFocus(WindowEvent e) {action.accept(e);}
    }

    public static class LostFocus extends WindowListener {
        public LostFocus(Consumer<WindowEvent> action) {super(action);}
        @Override public void windowLostFocus(WindowEvent e) {action.accept(e);}
    }

}
