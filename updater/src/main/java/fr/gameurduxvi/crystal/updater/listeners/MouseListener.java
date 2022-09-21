package fr.gameurduxvi.crystal.updater.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.function.Consumer;

public abstract class MouseListener extends MouseAdapter {

    protected Consumer<MouseEvent> action;

    protected MouseListener(Consumer<MouseEvent> action) {
        this.action = action;
    }

    public static class Pressed extends MouseListener {
        public Pressed(Consumer<MouseEvent> action) {super(action);}
        @Override public void mousePressed(MouseEvent e) {action.accept(e);}
    }

    public static class Clicked extends MouseListener {
        public Clicked(Consumer<MouseEvent> action) {super(action);}
        @Override public void mouseClicked(MouseEvent e) {action.accept(e);}
    }

    public static class Released extends MouseListener {
        public Released(Consumer<MouseEvent> action) {super(action);}
        @Override public void mouseReleased(MouseEvent e) {action.accept(e);}
    }

    public static class Entered extends MouseListener {
        public Entered(Consumer<MouseEvent> action) {super(action);}
        @Override public void mouseEntered(MouseEvent e) {action.accept(e);}
    }

    public static class Exited extends MouseListener {
        public Exited(Consumer<MouseEvent> action) {super(action);}
        @Override public void mousePressed(MouseEvent e) {action.accept(e);}
    }

    public static class WheelMoved extends MouseListener {
        public WheelMoved(Consumer<MouseEvent> action) {super(action);}
        @Override public void mouseWheelMoved(MouseWheelEvent e) {action.accept(e);}
    }

    public static class Dragged extends MouseListener {
        public Dragged(Consumer<MouseEvent> action) {super(action);}
        @Override public void mouseDragged(MouseEvent e) {action.accept(e);}
    }

    public static class Moved extends MouseListener {
        public Moved(Consumer<MouseEvent> action) {super(action);}
        @Override public void mouseMoved(MouseEvent e) {action.accept(e);}
    }
}
